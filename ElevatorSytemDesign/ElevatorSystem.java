package ElevatorSytemDesign;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ElevatorSytemDesign.command.Command;
import ElevatorSytemDesign.command.ElevatorRequest;
import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.ElevatorType;
import ElevatorSytemDesign.observer.ElevatorObserver;
import ElevatorSytemDesign.observer.FloorDisplay;
import ElevatorSytemDesign.strategy.DispatchStrategy;

class ElevatorSystem {
    private static ElevatorSystem instance;
    private List<ElevatorController> controllers;
    private List<Floor> floors;
    private ExecutorService executorService;
    private int numFloors;
    private DispatchStrategy dispatchStrategy;
    private Queue<Command> commandHistory;
    private List<ElevatorObserver> globalObservers;

    ElevatorSystem(int numFloors, int numElevators, int elevatorCapacity,
            DispatchStrategy dispatchStrategy, boolean emergencyProtocol,
            List<ElevatorType> elevatorTypes) {
        this.numFloors = numFloors;
        this.floors = new ArrayList<>();
        this.controllers = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(numElevators);
        this.dispatchStrategy = dispatchStrategy;
        this.commandHistory = new LinkedList<>();
        this.globalObservers = new ArrayList<>();

        for (int i = 0; i < numFloors; i++) {
            floors.add(new Floor(i));
        }

        MonitoringSystem monitoring = new MonitoringSystem();
        globalObservers.add(monitoring);

        for (int i = 0; i < numFloors; i++) {
            globalObservers.add(new FloorDisplay(i));
        }

        for (int i = 0; i < numElevators; i++) {
            ElevatorType type = i < elevatorTypes.size() ? elevatorTypes.get(i) : ElevatorType.PASSENGER;
            Elevator elevator = ElevatorFactory.createElevator(type, i + 1);

            for (ElevatorObserver observer : globalObservers) {
                elevator.attach(observer);
            }

            ElevatorController controller = new ElevatorController(elevator);
            controllers.add(controller);
            executorService.submit(controller);
        }
    }

    public static synchronized ElevatorSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ElevatorSystem not initialized. Use builder to create.");
        }
        return instance;
    }

    static synchronized void setInstance(ElevatorSystem system) {
        instance = system;
    }

    public void requestElevator(int floor, Direction direction) {
        if (floor < 0 || floor >= numFloors) {
            System.out.println("Invalid floor number");
            return;
        }

        ElevatorRequest request = new ElevatorRequest(floor,
                direction == Direction.UP ? floor + 1 : floor - 1);
        commandHistory.offer(request);
        request.execute();

        List<Elevator> elevators = new ArrayList<>();
        for (ElevatorController controller : controllers) {
            elevators.add(controller.getElevator());
        }

        Elevator bestElevator = dispatchStrategy.findBestElevator(floor, direction, elevators);
        if (bestElevator != null) {
            bestElevator.handleRequest(floor);
            System.out.println("Elevator " + bestElevator.getId() + " assigned to floor " + floor);
        }
    }

    public void selectFloor(int elevatorId, int destinationFloor) {
        for (ElevatorController controller : controllers) {
            if (controller.getElevator().getId() == elevatorId) {
                controller.getElevator().handleRequest(destinationFloor);
                return;
            }
        }
    }

    public void triggerEmergency(int elevatorId) {
        for (ElevatorController controller : controllers) {
            if (controller.getElevator().getId() == elevatorId) {
                controller.getElevator().triggerEmergency();
                return;
            }
        }
    }

    public void displayStatus() {
        System.out.println("\n=== Elevator System Status ===");
        for (ElevatorController controller : controllers) {
            Elevator e = controller.getElevator();
            System.out.println("Elevator " + e.getId() +
                    " [" + e.getType() + "]" +
                    ": Floor " + e.getCurrentFloor() +
                    ", Direction: " + e.getCurrentDirection() +
                    ", State: " + e.getStateType());
        }
        System.out.println("==============================\n");
    }

    public void shutdown() {
        for (ElevatorController controller : controllers) {
            controller.stop();
        }
        executorService.shutdown();
    }
}
