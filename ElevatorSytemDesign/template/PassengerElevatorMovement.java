package ElevatorSytemDesign.template;

public class PassengerElevatorMovement extends ElevatorMovementTemplate {
    @Override
    protected void allowPassengers() {
        System.out.println("Passengers boarding/exiting...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
