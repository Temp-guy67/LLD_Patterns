package ElevatorSytemDesign;

import java.util.ArrayList;
import java.util.List;

import ElevatorSytemDesign.enums.Constants.ElevatorType;
import ElevatorSytemDesign.strategy.DispatchStrategy;
import ElevatorSytemDesign.strategy.SCANStrategy;

// ==================== BUILDER PATTERN ====================
public class ElevatorSystemBuilder {
    private int numFloors = 10;
    private int numElevators = 3;
    private int elevatorCapacity = 8;
    private DispatchStrategy dispatchStrategy = new SCANStrategy();
    private boolean emergencyProtocol = true;
    private List<ElevatorType> elevatorTypes = new ArrayList<>();

    public ElevatorSystemBuilder withFloors(int numFloors) {
        this.numFloors = numFloors;
        return this;
    }

    public ElevatorSystemBuilder withElevators(int numElevators) {
        this.numElevators = numElevators;
        return this;
    }

    public ElevatorSystemBuilder withCapacity(int capacity) {
        this.elevatorCapacity = capacity;
        return this;
    }

    public ElevatorSystemBuilder withDispatchStrategy(DispatchStrategy strategy) {
        this.dispatchStrategy = strategy;
        return this;
    }

    public ElevatorSystemBuilder withEmergencyProtocol(boolean enabled) {
        this.emergencyProtocol = enabled;
        return this;
    }

    public ElevatorSystemBuilder addElevatorType(ElevatorType type) {
        this.elevatorTypes.add(type);
        return this;
    }

    public ElevatorSystem build() {
        return new ElevatorSystem(numFloors, numElevators, elevatorCapacity,
                dispatchStrategy, emergencyProtocol, elevatorTypes);
    }
}
