package ElevatorSytemDesign.strategy;

import java.util.List;

import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.Direction;
import ElevatorSytemDesign.enums.Constants.ElevatorStateType;

public class FCFSStrategy implements DispatchStrategy {
    @Override
    public Elevator findBestElevator(int floor, Direction direction, List<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            if (elevator.getStateType() == ElevatorStateType.IDLE) {
                return elevator;
            }
        }
        return elevators.get(0);
    }
}