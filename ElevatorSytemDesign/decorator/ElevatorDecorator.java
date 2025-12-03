package ElevatorSytemDesign.decorator;

import ElevatorSytemDesign.elevator.Elevator;

public abstract class ElevatorDecorator extends Elevator {
    protected Elevator decoratedElevator;

    public ElevatorDecorator(Elevator elevator) {
        super(elevator.getId(), elevator.getCapacity(), elevator.getType());
        this.decoratedElevator = elevator;
    }
}