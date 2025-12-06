package Uber;

import Uber.Constants.VehicleType;

public class RideSharingDemo {
    public static void main(String[] args) {
        // Initialize service
        RideService rideService = new RideService();

        // Create drivers
        Vehicle vehicle1 = new Vehicle("V1", "KA01AB1234", VehicleType.SEDAN,
                "Honda City", "White");
        Driver driver1 = new Driver("D1", "John Doe", "1234567890",
                "john@example.com", "DL123456", vehicle1);
        driver1.updateLocation(new Location(12.9716, 77.5946));
        rideService.registerDriver(driver1);

        // Create rider
        Rider rider1 = new Rider("R1", "Alice Smith", "9876543210",
                "alice@example.com");

        // Request ride
        Location pickup = new Location(12.9716, 77.5946);
        Location drop = new Location(12.2958, 76.6394);

        Ride ride = rideService.requestRide(rider1, pickup, drop, VehicleType.SEDAN);
        System.out.println("Ride requested: " + ride.getRideId());

        // Start ride
        rideService.startRide(ride.getRideId());

        // Complete ride
        rideService.completeRide(ride.getRideId());
        System.out.println("Ride completed. Fare: ₹" + ride.getFare());
    }
}