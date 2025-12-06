package Uber;

import java.util.ArrayList;
import java.util.List;

public class Rider extends User {
    private List<Ride> rideHistory;
    private double rating;

    public Rider(String id, String name, String phone, String email) {
        super(id, name, phone, email);
        this.rideHistory = new ArrayList<>();
        this.rating = 5.0;
    }

    public void addRide(Ride ride) {
        rideHistory.add(ride);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}