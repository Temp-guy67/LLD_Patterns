package ElevatorSytemDesign.decorator;

import ElevatorSytemDesign.elevator.Elevator;

public class MusicDecorator extends ElevatorDecorator {
    public MusicDecorator(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void openDoors() {
        playMusic();
        super.openDoors();
    }

    private void playMusic() {
        System.out.println("♪ Playing elevator music ♪");
    }
}