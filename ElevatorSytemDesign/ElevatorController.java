package ElevatorSytemDesign;

import ElevatorSytemDesign.elevator.Elevator;

class ElevatorController implements Runnable {
    private Elevator elevator;
    private volatile boolean running;

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            elevator.move();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }

    public Elevator getElevator() {
        return elevator;
    }
}
