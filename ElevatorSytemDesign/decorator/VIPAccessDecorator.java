package ElevatorSytemDesign.decorator;

import java.util.HashSet;
import java.util.Set;

import ElevatorSytemDesign.elevator.Elevator;

public class VIPAccessDecorator extends ElevatorDecorator {
    private Set<String> authorizedUsers;

    public VIPAccessDecorator(Elevator elevator) {
        super(elevator);
        this.authorizedUsers = new HashSet<>();
    }

    public void addAuthorizedUser(String userId) {
        authorizedUsers.add(userId);
        System.out.println("🔑 VIP access granted to: " + userId);
    }

    @Override
    public void handleRequest(int floor) {
        System.out.println("🔒 VIP Elevator - Checking authorization...");
        super.handleRequest(floor);
    }
}