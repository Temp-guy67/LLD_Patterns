package Uber;

public class Constants {
    public static enum RideStatus {
        REQUESTED, ACCEPTED, STARTED, COMPLETED, CANCELLED
    }

    public static enum VehicleType {
        BIKE, SEDAN, SUV, LUXURY
    }

    public static enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
}
