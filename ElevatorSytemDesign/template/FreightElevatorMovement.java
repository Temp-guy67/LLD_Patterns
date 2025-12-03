package ElevatorSytemDesign.template;

public class FreightElevatorMovement extends ElevatorMovementTemplate {
    @Override
    protected void allowPassengers() {
        System.out.println("Loading/unloading freight...");
        try {
            Thread.sleep(5000); // Freight takes longer
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void beforeDoorOpen() {
        System.out.println("Activating loading ramp...");
    }
}
