package Uber;

import java.util.List;

import Uber.Constants.VehicleType;

public class HighRatedDriverMatching implements DriverMatchingStrategy {
    @Override
    public Driver findDriver(List<Driver> drivers, Location pickup, VehicleType type) {
        return drivers.stream()
                .filter(d -> d.isAvailable() && d.getVehicle().getType() == type)
                .filter(d -> d.getCurrentLocation().distanceTo(pickup) < 5.0) // within 5km
                .max((d1, d2) -> Double.compare(d1.getRating(), d2.getRating()))
                .orElse(null);
    }
}