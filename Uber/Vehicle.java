package Uber;

import Uber.Constants.VehicleType;

public class Vehicle {
    private String vehicleId;
    private String licensePlate;
    private VehicleType type;
    private String model;
    private String color;

    public Vehicle(String vehicleId, String licensePlate, VehicleType type,
            String model, String color) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.type = type;
        this.model = model;
        this.color = color;
    }

    public VehicleType getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}