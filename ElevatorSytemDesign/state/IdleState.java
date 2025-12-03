package ElevatorSytemDesign.state;

import ElevatorSytemDesign.elevator.ElevatorContext;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class IdleState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context, int floor) {
        System.out.println("[State] Elevator " + context.getId() + " accepting request (IDLE state)");
        context.addDestination(floor);
        context.setState(new MovingState());
    }

    @Override
    public void move(ElevatorContext context) {
        // Idle - no movement
    }

    @Override
    public boolean canAcceptRequest() {
        return true;
    }

    @Override
    public ElevatorStateType getStateType() {
        return ElevatorStateType.IDLE;
    }
}
