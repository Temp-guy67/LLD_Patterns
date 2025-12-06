package Uber;

public class NotificationService {
    public void notifyDriver(Driver driver, Ride ride) {
        System.out.println("Notifying driver " + driver.getName() +
                " about ride " + ride.getRideId());
    }

    public void notifyRider(Rider rider, Ride ride) {
        System.out.println("Notifying rider " + rider.getName() +
                " about ride " + ride.getRideId() +
                " - Status: " + ride.getStatus());
    }
}