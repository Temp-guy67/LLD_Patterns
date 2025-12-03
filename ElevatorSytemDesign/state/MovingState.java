package ElevatorSytemDesign.state;

import ElevatorSytemDesign.elevator.ElevatorContext;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class MovingState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context, int floor) {
        System.out.println("[State] Elevator " + context.getId() + " accepting request (MOVING state)");
        context.addDestination(floor);
    }

    @Override
    public void move(ElevatorContext context) {
        context.processMovement();

        if (!context.hasRequests()) {
            context.setState(new IdleState());
        }
    }

    @Override
    public boolean canAcceptRequest() {
        return true;
    }

    @Override
    public ElevatorStateType getStateType() {
        return ElevatorStateType.MOVING;
    }
}