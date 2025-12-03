package MeetingScheduler;

import MeetingScheduler.domain.Booking;
import MeetingScheduler.domain.MeetingRoom;

import MeetingScheduler.exception.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demo application showcasing the Meeting Room Reservation System.
 * Demonstrates all core features and edge cases.
 * 
 * @author Meeting Room Platform Team
 * @version 1.0
 */
public class MeetingRoomReservationDemo {

    private static final MeetingRoomReservationFacade facade = MeetingRoomReservationFacade.getInstance();

    public static void main(String[] args) {
        System.out.println("=== Meeting Room Reservation System Demo ===\n");

        try {
            // Setup initial data
            setupInitialData();

            // Demonstrate core features
            demonstrateBasicBooking();
            demonstrateOverlappingBookings();
            demonstrateAvailableRooms();
            demonstrateBookingCancellation();
            demonstrateListOperations();

            // Demonstrate concurrency handling
            demonstrateConcurrentBookings();

            System.out.println("\n=== Demo completed successfully ===");

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up initial test data: employees and meeting rooms.
     */
    private static void setupInitialData() {
        System.out.println("--- Setting up initial data ---");

        // Create employees
        facade.createEmployee("EMP001", "Alice Johnson", "alice@company.com");
        facade.createEmployee("EMP002", "Bob Smith", "bob@company.com");
        facade.createEmployee("EMP003", "Charlie Brown", "charlie@company.com");
        System.out.println("✓ Created 3 employees");

        // Create meeting rooms
        facade.createMeetingRoom("ROOM-A", "Conference Room A", 10, "Floor 1");
        facade.createMeetingRoom("ROOM-B", "Conference Room B", 6, "Floor 1");
        facade.createMeetingRoom("ROOM-C", "Board Room", 20, "Floor 2");
        System.out.println("✓ Created 3 meeting rooms\n");
    }

    /**
     * Demonstrates basic room booking functionality.
     */
    private static void demonstrateBasicBooking() {
        System.out.println("--- Demonstrating Basic Booking ---");

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(1);

        Booking booking = facade.bookRoom("EMP001", "ROOM-A", start, end);
        System.out.println("✓ Successfully booked: " + booking);
        System.out.println("  Room: " + booking.getRoomId());
        System.out.println("  Time: " + booking.getTimeSlot());
        System.out.println();
    }

    /**
     * Demonstrates handling of overlapping booking requests.
     */
    private static void demonstrateOverlappingBookings() {
        System.out.println("--- Demonstrating Overlapping Booking Prevention ---");

        // First booking: 2:00 PM - 3:00 PM
        LocalDateTime start1 = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        LocalDateTime end1 = start1.plusHours(1);

        Booking booking1 = facade.bookRoom("EMP001", "ROOM-B", start1, end1);
        System.out.println("✓ Booking 1 successful: " + booking1.getTimeSlot());

        // Try overlapping booking: 2:30 PM - 3:30 PM (should fail)
        LocalDateTime start2 = start1.plusMinutes(30);
        LocalDateTime end2 = end1.plusMinutes(30);

        try {
            facade.bookRoom("EMP002", "ROOM-B", start2, end2);
            System.out.println("✗ Should have failed but didn't!");
        } catch (RoomNotAvailableException e) {
            System.out.println("✓ Overlapping booking correctly prevented: " + e.getMessage());
        }

        // Non-overlapping booking: 3:00 PM - 4:00 PM (should succeed)
        LocalDateTime start3 = end1;
        LocalDateTime end3 = start3.plusHours(1);

        Booking booking3 = facade.bookRoom("EMP002", "ROOM-B", start3, end3);
        System.out.println("✓ Non-overlapping booking successful: " + booking3.getTimeSlot());
        System.out.println();
    }

    /**
     * Demonstrates checking available rooms for a time slot.
     */
    private static void demonstrateAvailableRooms() {
        System.out.println("--- Demonstrating Available Rooms Query ---");

        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(9).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        // Book one room
        facade.bookRoom("EMP001", "ROOM-A", start, end);

        // Check available rooms for same time
        List<MeetingRoom> availableRooms = facade.getAvailableRooms(start, end);

        System.out.println("✓ For time slot " + start + " to " + end);
        System.out.println("  Available rooms: " + availableRooms.size());
        availableRooms.forEach(
                room -> System.out.println("    - " + room.getName() + " (Capacity: " + room.getCapacity() + ")"));
        System.out.println();
    }

    /**
     * Demonstrates booking cancellation.
     */
    private static void demonstrateBookingCancellation() {
        System.out.println("--- Demonstrating Booking Cancellation ---");

        LocalDateTime start = LocalDateTime.now().plusDays(3).withHour(11).withMinute(0);
        LocalDateTime end = start.plusHours(1);

        Booking booking = facade.bookRoom("EMP003", "ROOM-C", start, end);
        System.out.println("✓ Created booking: " + booking.getBookingId());
        System.out.println("  Status: " + booking.getStatus());

        facade.cancelBooking(booking.getBookingId());
        Booking cancelled = facade.getBooking(booking.getBookingId());
        System.out.println("✓ Cancelled booking: " + cancelled.getBookingId());
        System.out.println("  New status: " + cancelled.getStatus());

        // Try to cancel again (should fail)
        try {
            facade.cancelBooking(booking.getBookingId());
            System.out.println("✗ Should have failed but didn't!");
        } catch (IllegalStateException e) {
            System.out.println("✓ Double cancellation correctly prevented: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Demonstrates listing bookings for rooms and employees.
     */
    private static void demonstrateListOperations() {
        System.out.println("--- Demonstrating List Operations ---");

        // List bookings for a room
        List<Booking> roomBookings = facade.listBookingsForRoom("ROOM-A");
        System.out.println("✓ All bookings for ROOM-A: " + roomBookings.size());
        roomBookings.forEach(b -> System.out.println("    - " + b.getTimeSlot() + " [" + b.getStatus() + "]"));

        // List active bookings for a room
        List<Booking> activeRoomBookings = facade.listActiveBookingsForRoom("ROOM-A");
        System.out.println("✓ Active bookings for ROOM-A: " + activeRoomBookings.size());

        // List bookings for an employee
        List<Booking> employeeBookings = facade.listBookingsForEmployee("EMP001");
        System.out.println("✓ All bookings for EMP001: " + employeeBookings.size());
        employeeBookings.forEach(b -> System.out.println("    - Room: " + b.getRoomId() + ", " + b.getTimeSlot()));
        System.out.println();
    }

    /**
     * Demonstrates concurrent booking handling with race conditions.
     */
    private static void demonstrateConcurrentBookings() {
        System.out.println("--- Demonstrating Concurrent Booking Handling ---");

        LocalDateTime start = LocalDateTime.now().plusDays(5).withHour(15).withMinute(0);
        LocalDateTime end = start.plusHours(1);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Simulate 5 users trying to book the same room at the same time
        for (int i = 0; i < 5; i++) {
            final int userId = i + 1;
            executor.submit(() -> {
                try {
                    String employeeId = "EMP00" + (userId % 3 + 1);
                    Booking booking = facade.bookRoom(employeeId, "ROOM-C", start, end);
                    System.out.println("✓ User " + userId + " successfully booked: " +
                            booking.getBookingId());
                } catch (RoomNotAvailableException e) {
                    System.out.println("✗ User " + userId + " failed: Room already booked");
                } catch (Exception e) {
                    System.out.println("✗ User " + userId + " error: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("✓ Concurrent booking test completed - only 1 should succeed");
        } catch (InterruptedException e) {
            System.err.println("Executor interrupted: " + e.getMessage());
        }
        System.out.println();
    }
}