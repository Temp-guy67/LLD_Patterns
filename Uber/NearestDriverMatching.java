package Uber;

import java.util.List;

import Uber.Constants.VehicleType;

public class NearestDriverMatching implements DriverMatchingStrategy {
    @Override
    public Driver findDriver(List<Driver> drivers, Location pickup, VehicleType type) {
        return drivers.stream()
                .filter(d -> d.isAvailable() && d.getVehicle().getType() == type)
                .min((d1, d2) -> Double.compare(
                        d1.getCurrentLocation().distanceTo(pickup),
                        d2.getCurrentLocation().distanceTo(pickup)))
                .orElse(null);
    }
}
