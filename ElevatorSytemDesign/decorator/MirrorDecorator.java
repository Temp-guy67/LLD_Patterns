package ElevatorSytemDesign.decorator;

import ElevatorSytemDesign.elevator.Elevator;

public class MirrorDecorator extends ElevatorDecorator {
    public MirrorDecorator(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void openDoors() {
        System.out.println("✨ Mirrors reflecting passengers ✨");
        super.openDoors();
    }
}