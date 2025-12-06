package Uber;

import java.util.List;

import Uber.Constants.VehicleType;

public interface DriverMatchingStrategy {
    Driver findDriver(List<Driver> drivers, Location pickup, VehicleType type);
}
