package ElevatorSytemDesign.command;

import ElevatorSytemDesign.enums.Constants.Direction;

public class ElevatorRequest implements Command {
    private int sourceFloor;
    private int destinationFloor;
    private Direction direction;
    private long timestamp;
    private boolean executed;

    public ElevatorRequest(int sourceFloor, int destinationFloor) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = destinationFloor > sourceFloor ? Direction.UP : Direction.DOWN;
        this.timestamp = System.currentTimeMillis();
        this.executed = false;
    }

    @Override
    public void execute() {
        executed = true;
        System.out.println("[Command] Executing request: Floor " + sourceFloor + " -> " + destinationFloor);
    }

    @Override
    public void undo() {
        executed = false;
        System.out.println("[Command] Undoing request: Floor " + sourceFloor + " -> " + destinationFloor);
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isExecuted() {
        return executed;
    }
}
