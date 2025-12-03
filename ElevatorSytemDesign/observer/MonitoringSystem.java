package ElevatorSytemDesign.observer;

class MonitoringSystem implements ElevatorObserver {
    @Override
    public void onElevatorArrived(int elevatorId, int floor) {
        System.out.println("[Monitoring] Elevator " + elevatorId + " arrived at floor " + floor);
    }

    @Override
    public void onDoorOpened(int elevatorId, int floor) {
    }

    @Override
    public void onDoorClosed(int elevatorId, int floor) {
    }

    @Override
    public void onEmergency(int elevatorId, String message) {
        System.out.println("[MONITORING ALERT] Elevator " + elevatorId + " - " + message);
    }
}
