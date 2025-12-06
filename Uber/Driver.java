package Uber;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User {
    private String licenseNumber;
    private Vehicle vehicle;
    private Location currentLocation;
    private boolean available;
    private List<Ride> rideHistory;
    private double rating;

    public Driver(String id, String name, String phone, String email,
            String licenseNumber, Vehicle vehicle) {
        super(id, name, phone, email);
        this.licenseNumber = licenseNumber;
        this.vehicle = vehicle;
        this.available = true;
        this.rideHistory = new ArrayList<>();
        this.rating = 5.0;
    }

    public void updateLocation(Location location) {
        this.currentLocation = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void addRide(Ride ride) {
        rideHistory.add(ride);
    }
}