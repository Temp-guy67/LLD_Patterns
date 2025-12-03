package ElevatorSytemDesign.state;

import ElevatorSytemDesign.elevator.ElevatorContext;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class EmergencyState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context, int floor) {
        System.out.println("[State] Elevator " + context.getId() + " cannot accept request (EMERGENCY mode)");
    }

    @Override
    public void move(ElevatorContext context) {
        // Move to ground floor in emergency
        context.emergencyMoveToGround();
    }

    @Override
    public boolean canAcceptRequest() {
        return false;
    }

    @Override
    public ElevatorStateType getStateType() {
        return ElevatorStateType.EMERGENCY;
    }
}