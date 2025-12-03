package ElevatorSytemDesign.observer;

public class FloorDisplay implements ElevatorObserver {
    private int floorNumber;

    public FloorDisplay(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public void onElevatorArrived(int elevatorId, int floor) {
        if (floor == floorNumber) {
            System.out.println("[Floor " + floorNumber + " Display] Elevator " + elevatorId + " has arrived");
        }
    }

    @Override
    public void onDoorOpened(int elevatorId, int floor) {
        if (floor == floorNumber) {
            System.out.println("[Floor " + floorNumber + " Display] Elevator " + elevatorId + " doors opening");
        }
    }

    @Override
    public void onDoorClosed(int elevatorId, int floor) {
    }

    @Override
    public void onEmergency(int elevatorId, String message) {
        System.out.println("[Floor " + floorNumber + " Display] EMERGENCY - Elevator " + elevatorId + ": " + message);
    }
}
