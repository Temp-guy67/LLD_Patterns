package ElevatorSytemDesign.template;

// ==================== TEMPLATE METHOD PATTERN ====================
public abstract class ElevatorMovementTemplate {
    public final void processFloorStop(int floor) {
        beforeDoorOpen();
        openDoors();
        allowPassengers();
        closeDoors();
        afterDoorClose();
    }

    protected abstract void allowPassengers();

    protected void beforeDoorOpen() {
        System.out.println("Preparing to open doors...");
    }

    protected void openDoors() {
        System.out.println("Doors opening...");
    }

    protected void closeDoors() {
        System.out.println("Doors closing...");
    }

    protected void afterDoorClose() {
        System.out.println("Ready to move...");
    }
}
