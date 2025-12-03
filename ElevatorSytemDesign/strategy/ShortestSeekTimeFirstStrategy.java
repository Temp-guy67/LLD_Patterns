package ElevatorSytemDesign.strategy;

import java.util.List;

import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class ShortestSeekTimeFirstStrategy implements DispatchStrategy {
    @Override
    public Elevator findBestElevator(int floor, Direction direction, List<Elevator> elevators) {
        Elevator bestElevator = elevators.get(0);
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (!elevator.canTakeRequest(floor, direction))
                continue;

            int distance = elevator.getDistanceFromFloor(floor);
            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }
}