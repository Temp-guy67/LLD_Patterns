package ElevatorSytemDesign.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.DoorState;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;
import ElevatorSytemDesign.enums.Constants.ElevatorType;
import ElevatorSytemDesign.observer.ElevatorObserver;
import ElevatorSytemDesign.state.ElevatorState;
import ElevatorSytemDesign.state.EmergencyState;
import ElevatorSytemDesign.state.IdleState;
import ElevatorSytemDesign.template.ElevatorMovementTemplate;
import ElevatorSytemDesign.template.FreightElevatorMovement;
import ElevatorSytemDesign.template.PassengerElevatorMovement;

public class Elevator implements ElevatorContext {
    private int id;
    private int currentFloor;
    private Direction currentDirection;
    private ElevatorState state;
    private DoorState doorState;
    private int capacity;
    private ElevatorType type;
    private TreeSet<Integer> upRequests;
    private TreeSet<Integer> downRequests;
    private List<ElevatorObserver> observers;
    private ElevatorMovementTemplate movementTemplate;

    public Elevator(int id, int capacity, ElevatorType type) {
        this.id = id;
        this.currentFloor = 0;
        this.currentDirection = Direction.IDLE;
        this.state = new IdleState();
        this.doorState = DoorState.CLOSED;
        this.capacity = capacity;
        this.type = type;
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>(Collections.reverseOrder());
        this.observers = new ArrayList<>();

        if (type == ElevatorType.FREIGHT) {
            this.movementTemplate = new FreightElevatorMovement();
        } else {
            this.movementTemplate = new PassengerElevatorMovement();
        }
    }

    public void attach(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void detach(ElevatorObserver observer) {
        observers.remove(observer);
    }

    private void notifyArrival(int floor) {
        for (ElevatorObserver observer : observers) {
            observer.onElevatorArrived(id, floor);
        }
    }

    private void notifyDoorOpened(int floor) {
        for (ElevatorObserver observer : observers) {
            observer.onDoorOpened(id, floor);
        }
    }

    private void notifyEmergency(String message) {
        for (ElevatorObserver observer : observers) {
            observer.onEmergency(id, message);
        }
    }

    @Override
    public void addDestination(int floor) {
        if (floor > currentFloor) {
            upRequests.add(floor);
        } else if (floor < currentFloor) {
            downRequests.add(floor);
        }
    }

    @Override
    public void setState(ElevatorState newState) {
        this.state = newState;
        System.out.println("Elevator " + id + " state changed to: " + newState.getStateType());
    }

    public void handleRequest(int floor) {
        state.handleRequest(this, floor);
    }

    public void move() {
        state.move(this);
    }

    @Override
    public void processMovement() {
        if (currentDirection == Direction.UP && !upRequests.isEmpty()) {
            Integer nextFloor = upRequests.first();
            if (nextFloor != null) {
                moveToFloor(nextFloor);
                upRequests.remove(nextFloor);
                movementTemplate.processFloorStop(nextFloor);
            }

            if (upRequests.isEmpty() && !downRequests.isEmpty()) {
                currentDirection = Direction.DOWN;
            } else if (upRequests.isEmpty()) {
                currentDirection = Direction.IDLE;
            }
        } else if (currentDirection == Direction.DOWN && !downRequests.isEmpty()) {
            Integer nextFloor = downRequests.first();
            if (nextFloor != null) {
                moveToFloor(nextFloor);
                downRequests.remove(nextFloor);
                movementTemplate.processFloorStop(nextFloor);
            }

            if (downRequests.isEmpty() && !upRequests.isEmpty()) {
                currentDirection = Direction.UP;
            } else if (downRequests.isEmpty()) {
                currentDirection = Direction.IDLE;
            }
        } else if (!upRequests.isEmpty()) {
            currentDirection = Direction.UP;
        } else if (!downRequests.isEmpty()) {
            currentDirection = Direction.DOWN;
        }
    }

    private void moveToFloor(int floor) {
        System.out.println("Elevator " + id + " moving from " + currentFloor + " to " + floor);
        try {
            Thread.sleep(Math.abs(floor - currentFloor) * 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        currentFloor = floor;
        notifyArrival(floor);
    }

    public void openDoors() {
        doorState = DoorState.OPEN;
        System.out.println("Elevator " + id + " doors opening at floor " + currentFloor);
        notifyDoorOpened(currentFloor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void closeDoors() {
        doorState = DoorState.CLOSED;
        System.out.println("Elevator " + id + " doors closing");
    }

    @Override
    public void emergencyMoveToGround() {
        System.out.println("⚠️ EMERGENCY: Elevator " + id + " moving to ground floor");
        notifyEmergency("Moving to ground floor");
        upRequests.clear();
        downRequests.clear();
        moveToFloor(0);
    }

    public void triggerEmergency() {
        setState(new EmergencyState());
        emergencyMoveToGround();
    }

    public boolean canTakeRequest(int floor, Direction direction) {
        if (!state.canAcceptRequest()) {
            return false;
        }

        if (state.getStateType() == ElevatorStateType.IDLE) {
            return true;
        }

        if (currentDirection == Direction.UP && direction == Direction.UP && floor > currentFloor) {
            return true;
        }

        if (currentDirection == Direction.DOWN && direction == Direction.DOWN && floor < currentFloor) {
            return true;
        }

        return false;
    }

    public int getDistanceFromFloor(int floor) {
        return Math.abs(currentFloor - floor);
    }

    @Override
    public boolean hasRequests() {
        return !upRequests.isEmpty() || !downRequests.isEmpty();
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public ElevatorStateType getStateType() {
        return state.getStateType();
    }

    public int getCapacity() {
        return capacity;
    }

    public ElevatorType getType() {
        return type;
    }
}
