package ElevatorSytemDesign.state;

import ElevatorSytemDesign.elevator.ElevatorContext;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class MaintenanceState implements ElevatorState {
    @Override
    public void handleRequest(ElevatorContext context, int floor) {
        System.out.println("[State] Elevator " + context.getId() + " cannot accept request (MAINTENANCE mode)");
    }

    @Override
    public void move(ElevatorContext context) {
        // No movement in maintenance
    }

    @Override
    public boolean canAcceptRequest() {
        return false;
    }

    @Override
    public ElevatorStateType getStateType() {
        return ElevatorStateType.MAINTENANCE;
    }
}