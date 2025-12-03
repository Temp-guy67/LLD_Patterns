package ElevatorSytemDesign;

import ElevatorSytemDesign.decorator.MirrorDecorator;
import ElevatorSytemDesign.decorator.MusicDecorator;
import ElevatorSytemDesign.decorator.VIPAccessDecorator;
import ElevatorSytemDesign.elevator.Elevator;
import ElevatorSytemDesign.enums.Constants.ElevatorType;

// ==================== FACTORY PATTERN ====================
public class ElevatorFactory {
    public static Elevator createElevator(ElevatorType type, int id) {
        switch (type) {
            case PASSENGER:
                return new Elevator(id, 8, ElevatorType.PASSENGER);
            case FREIGHT:
                return new Elevator(id, 20, ElevatorType.FREIGHT);
            case EXPRESS:
                return new Elevator(id, 10, ElevatorType.EXPRESS);
            case VIP:
                Elevator vipElevator = new Elevator(id, 6, ElevatorType.VIP);
                return new VIPAccessDecorator(new MusicDecorator(new MirrorDecorator(vipElevator)));
            default:
                return new Elevator(id, 8, ElevatorType.PASSENGER);
        }
    }
}
