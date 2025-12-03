package ElevatorSytemDesign.strategy;

import java.util.List;

import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class SCANStrategy implements DispatchStrategy {
    @Override
    public Elevator findBestElevator(int floor, Direction direction, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (elevator.canTakeRequest(floor, direction)) {
                int distance = elevator.getDistanceFromFloor(floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;
                }
            }
        }

        if (bestElevator == null) {
            for (Elevator elevator : elevators) {
                if (elevator.getStateType() == ElevatorStateType.IDLE) {
                    return elevator;
                }
            }
        }

        return bestElevator != null ? bestElevator : elevators.get(0);
    }
}