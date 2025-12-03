package ElevatorSytemDesign.observer;

// ==================== OBSERVER PATTERN ====================
public interface ElevatorObserver {
    public void onElevatorArrived(int elevatorId, int floor);

    public void onDoorOpened(int elevatorId, int floor);

    public void onDoorClosed(int elevatorId, int floor);

    public void onEmergency(int elevatorId, String message);

}
