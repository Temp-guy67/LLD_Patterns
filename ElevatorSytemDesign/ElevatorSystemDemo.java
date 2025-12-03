package ElevatorSytemDesign;

import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.ElevatorType;
import ElevatorSytemDesign.strategy.SCANStrategy;

public class ElevatorSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🏢 Building Elevator System with All Design Patterns 🏢\n");

        // BUILDER PATTERN
        ElevatorSystem system = new ElevatorSystemBuilder()
                .withFloors(15)
                .withElevators(4)
                .withCapacity(8)
                .withDispatchStrategy(new SCANStrategy())
                .withEmergencyProtocol(true)
                .addElevatorType(ElevatorType.PASSENGER)
                .addElevatorType(ElevatorType.PASSENGER)
                .addElevatorType(ElevatorType.FREIGHT)
                .addElevatorType(ElevatorType.VIP)
                .build();

        ElevatorSystem.setInstance(system);

        System.out.println("✅ System initialized with SCAN strategy\n");
        Thread.sleep(1000);

        // Test requests
        system.requestElevator(5, Direction.UP);
        Thread.sleep(500);

        system.requestElevator(3, Direction.UP);
        Thread.sleep(500);

        system.requestElevator(7, Direction.DOWN);
        Thread.sleep(2000);

        system.selectFloor(1, 10);
        system.selectFloor(2, 8);
        system.selectFloor(3, 12);

        Thread.sleep(3000);
        system.displayStatus();

        // Test emergency
        System.out.println("\n🚨 Triggering Emergency on Elevator 2 🚨\n");
        system.triggerEmergency(2);

        Thread.sleep(3000);
        system.displayStatus();

        Thread.sleep(5000);
        system.displayStatus();

        system.shutdown();
        System.out.println("\n✅ System shutdown complete");
    }
}
