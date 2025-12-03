package ElevatorSytemDesign.strategy;

import java.util.List;

import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.Direction;

public interface DispatchStrategy {
    Elevator findBestElevator(int floor, Direction direction, List<Elevator> elevators);

}
