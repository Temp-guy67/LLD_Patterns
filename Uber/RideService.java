package Uber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import Uber.Constants.RideStatus;
import Uber.Constants.VehicleType;

public class RideService {
    private Map<String, Ride> rides;
    private List<Driver> drivers;
    private PricingStrategy pricingStrategy;
    private DriverMatchingStrategy matchingStrategy;
    private NotificationService notificationService;

    public RideService() {
        this.rides = new ConcurrentHashMap<>();
        this.drivers = new ArrayList<>();
        this.pricingStrategy = new StandardPricing();
        this.matchingStrategy = new NearestDriverMatching();
        this.notificationService = new NotificationService();
    }

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    public void setMatchingStrategy(DriverMatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public Ride requestRide(Rider rider, Location pickup, Location drop,
            VehicleType vehicleType) {
        String rideId = UUID.randomUUID().toString();
        Ride ride = new Ride(rideId, rider, pickup, drop, vehicleType);

        Driver driver = matchingStrategy.findDriver(drivers, pickup, vehicleType);
        if (driver == null) {
            throw new RuntimeException("No driver available");
        }

        ride.assignDriver(driver);
        driver.setAvailable(false);
        rides.put(rideId, ride);

        notificationService.notifyDriver(driver, ride);
        notificationService.notifyRider(rider, ride);

        return ride;
    }

    public void startRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride != null && ride.getStatus() == RideStatus.ACCEPTED) {
            ride.startRide();
            notificationService.notifyRider(ride.getRider(), ride);
        }
    }

    public void completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride != null && ride.getStatus() == RideStatus.STARTED) {
            double fare = pricingStrategy.calculateFare(
                    ride.getPickupLocation(),
                    ride.getDropLocation(),
                    ride.getVehicleType());
            ride.completeRide(fare);
            ride.getDriver().setAvailable(true);

            ride.getRider().addRide(ride);
            ride.getDriver().addRide(ride);

            notificationService.notifyRider(ride.getRider(), ride);
            notificationService.notifyDriver(ride.getDriver(), ride);
        }
    }

    public void cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride != null) {
            ride.cancelRide();
            if (ride.getDriver() != null) {
                ride.getDriver().setAvailable(true);
            }
        }
    }

    public void registerDriver(Driver driver) {
        drivers.add(driver);
    }

    public Ride getRide(String rideId) {
        return rides.get(rideId);
    }
}