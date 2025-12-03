package ElevatorSytemDesign.elevator;

import ElevatorSytemDesign.state.ElevatorState;

public interface ElevatorContext {
    void addDestination(int floor);

    void setState(ElevatorState state);

    void processMovement();

    boolean hasRequests();

    void emergencyMoveToGround();

    int getId();
}
