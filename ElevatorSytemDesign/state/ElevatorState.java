package ElevatorSytemDesign.state;

import ElevatorSytemDesign.elevator.ElevatorContext;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

// ==================== STATE PATTERN ====================
public interface ElevatorState {
    void handleRequest(ElevatorContext context, int floor);

    void move(ElevatorContext context);

    boolean canAcceptRequest();

    ElevatorStateType getStateType();
}
