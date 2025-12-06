package Uber;

import Uber.Constants.VehicleType;

public class SurgePricing implements PricingStrategy {
    private PricingStrategy basePricing;
    private double surgeMultiplier;

    public SurgePricing(PricingStrategy basePricing, double surgeMultiplier) {
        this.basePricing = basePricing;
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public double calculateFare(Location pickup, Location drop, VehicleType type) {
        return basePricing.calculateFare(pickup, drop, type) * surgeMultiplier;
    }
}