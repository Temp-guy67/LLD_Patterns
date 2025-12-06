package Uber;

import java.util.Map;

import Uber.Constants.VehicleType;

public class StandardPricing implements PricingStrategy {
    private static final Map<VehicleType, Double> BASE_FARE = Map.of(
            VehicleType.BIKE, 20.0,
            VehicleType.SEDAN, 50.0,
            VehicleType.SUV, 80.0,
            VehicleType.LUXURY, 150.0);

    private static final Map<VehicleType, Double> PER_KM = Map.of(
            VehicleType.BIKE, 8.0,
            VehicleType.SEDAN, 12.0,
            VehicleType.SUV, 15.0,
            VehicleType.LUXURY, 25.0);

    @Override
    public double calculateFare(Location pickup, Location drop, VehicleType type) {
        double distance = pickup.distanceTo(drop);
        return BASE_FARE.get(type) + (distance * PER_KM.get(type));
    }
}