package Uber;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Uber.Constants.RideStatus;
import Uber.Constants.VehicleType;

public class Ride {
    private String rideId;
    private Rider rider;
    private Driver driver;
    private Location pickupLocation;
    private Location dropLocation;
    private VehicleType vehicleType;
    private RideStatus status;
    private double fare;
    private LocalDateTime requestTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int riderRating;
    private int driverRating;

    public Ride(String rideId, Rider rider, Location pickup,
            Location drop, VehicleType vehicleType) {
        this.rideId = rideId;
        this.rider = rider;
        this.pickupLocation = pickup;
        this.dropLocation = drop;
        this.vehicleType = vehicleType;
        this.status = RideStatus.REQUESTED;
        this.requestTime = LocalDateTime.now();
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.ACCEPTED;
    }

    public void startRide() {
        this.status = RideStatus.STARTED;
        this.startTime = LocalDateTime.now();
    }

    public void completeRide(double fare) {
        this.status = RideStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
        this.fare = fare;
    }

    public void cancelRide() {
        this.status = RideStatus.CANCELLED;
    }

    public void rateDriver(int rating) {
        this.driverRating = rating;
    }

    public void rateRider(int rating) {
        this.riderRating = rating;
    }

    public String getRideId() {
        return rideId;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public RideStatus getStatus() {
        return status;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropLocation() {
        return dropLocation;
    }

    public double getFare() {
        return fare;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}