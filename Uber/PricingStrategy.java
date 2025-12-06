package Uber;

import Uber.Constants.VehicleType;

public interface PricingStrategy {
    double calculateFare(Location pickup, Location drop, VehicleType type);
}
