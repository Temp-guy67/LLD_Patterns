package ElevatorSytemDesign;

// core class
public class Floor {
    private int floorNumber;
    private boolean upButtonPressed;
    private boolean downButtonPressed;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void pressUpButton() {
        this.upButtonPressed = true;
        System.out.println("Floor " + floorNumber + ": UP button pressed");
    }

    public void pressDownButton() {
        this.downButtonPressed = true;
        System.out.println("Floor " + floorNumber + ": DOWN button pressed");
    }

    public void resetButtons() {
        this.upButtonPressed = false;
        this.downButtonPressed = false;
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
