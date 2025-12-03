package ElevatorSytemDesign.enums;

public class Constants {
    public enum Direction {
        UP, DOWN, IDLE
    }

    public enum ElevatorStateType {
        MOVING, IDLE, MAINTENANCE, EMERGENCY
    }

    public enum DoorState {
        OPEN, CLOSED
    }

    public enum ElevatorType {
        PASSENGER, FREIGHT, EXPRESS, VIP
    }
}
