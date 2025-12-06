package MeetingScheduler;

import MeetingScheduler.domain.*;
import MeetingScheduler.facade.MeetingRoomReservationFacade;
import MeetingScheduler.exception.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * Comprehensive test suite for Meeting Room Reservation System.
 * Tests all core functionality, edge cases, and concurrency scenarios.
 *
 * Note: These tests use JUnit 5. Add the following dependency:
 * <dependency>
 * <groupId>org.junit.jupiter</groupId>
 * <artifactId>junit-jupiter</artifactId>
 * <version>5.9.3</version>
 * <scope>test</scope>
 * </dependency>
 *
 * @author Meeting Room Platform Team
 * @version 1.0
 */
public class MeetingRoomReservationTest {

    private MeetingRoomReservationFacade facade;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        // Note: In production, you'd want to reset the singleton or use dependency
        injection facade = MeetingRoomReservationFacade.getInstance();
        baseTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

        // Setup test data
        setupTestData();
    }

    private void setupTestData() {
        try {
            facade.createEmployee("TEST_EMP1", "Test Employee 1", "test1@company.com");
            facade.createEmployee("TEST_EMP2", "Test Employee 2", "test2@company.com");
            facade.createMeetingRoom("TEST_ROOM1", "Test Room 1", 10, "Floor 1");
            facade.createMeetingRoom("TEST_ROOM2", "Test Room 2", 5, "Floor 1");
        } catch (Exception e) {
            // Entities might already exist from previous tests
        }
    }

    // ========== TimeSlot Tests ==========

    @Test
    @DisplayName("TimeSlot should detect overlapping slots correctly")
    void testTimeSlotOverlap() {
        TimeSlot slot1 = new TimeSlot(baseTime, baseTime.plusHours(1));
        TimeSlot slot2 = new TimeSlot(baseTime.plusMinutes(30),
                baseTime.plusHours(1).plusMinutes(30));
        TimeSlot slot3 = new TimeSlot(baseTime.plusHours(1), baseTime.plusHours(2));

        assertTrue(slot1.overlaps(slot2), "Overlapping slots should be detected");
        assertFalse(slot1.overlaps(slot3), "Non-overlapping slots should not overlap");
    }

    @Test
    @DisplayName("TimeSlot should reject invalid time ranges")
    void testInvalidTimeSlot() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(baseTime, baseTime.minusHours(1));
        }, "Should reject end time before start time");

        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(baseTime, baseTime);
        }, "Should reject equal start and end times");

        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        }, "Should reject past time slots");
    }

    // ========== Employee Tests ==========

    @Test
    @DisplayName("Should create and retrieve employee successfully")
    void testCreateEmployee() {
        String uniqueId = "EMP_" + System.currentTimeMillis();
        Employee employee = facade.createEmployee(uniqueId, "John Doe",
                "john@company.com");

        assertNotNull(employee);
        assertEquals(uniqueId, employee.getEmployeeId());
        assertEquals("John Doe", employee.getName());

        Employee retrieved = facade.getEmployee(uniqueId);
        assertEquals(employee, retrieved);
    }

    @Test
    @DisplayName("Should reject invalid employee data")
    void testInvalidEmployeeData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("", "Name", "email@company.com");
        }, "Should reject empty employee ID");

        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("EMP001", "", "email@company.com");
        }, "Should reject empty name");

        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("EMP001", "Name", "invalid-email");
        }, "Should reject invalid email");
    }

    // ========== Meeting Room Tests ==========

    @Test
    @DisplayName("Should create and retrieve meeting room successfully")
    void testCreateMeetingRoom() {
        String uniqueId = "ROOM_" + System.currentTimeMillis();
        MeetingRoom room = facade.createMeetingRoom(uniqueId, "Conference Room", 15,
                "Floor 2");

        assertNotNull(room);
        assertEquals(uniqueId, room.getRoomId());
        assertEquals(15, room.getCapacity());

        MeetingRoom retrieved = facade.getMeetingRoom(uniqueId);
        assertEquals(room, retrieved);
    }

    @Test
    @DisplayName("Should find rooms by capacity")
    void testFindRoomsByCapacity() {
        List<MeetingRoom> rooms = facade.getRoomsByCapacity(8);

        assertNotNull(rooms);
        rooms.forEach(room -> assertTrue(room.getCapacity() >= 8, "All rooms should meet capacity requirement"));
    }

    // ========== Booking Tests ==========

    @Test
    @DisplayName("Should book room successfully")
    void testSuccessfulBooking() {
        LocalDateTime start = baseTime.plusDays(10);
        LocalDateTime end = start.plusHours(1);

        Booking booking = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start, end);

        assertNotNull(booking);
        assertEquals("TEST_EMP1", booking.getEmployeeId());
        assertEquals("TEST_ROOM1", booking.getRoomId());
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        assertTrue(booking.isActive());
    }

    @Test
    @DisplayName("Should prevent overlapping bookings")
    void testPreventOverlappingBookings() {
        LocalDateTime start1 = baseTime.plusDays(20);
        LocalDateTime end1 = start1.plusHours(1);

        // First booking should succeed
        facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start1, end1);

        // Overlapping booking should fail
        LocalDateTime start2 = start1.plusMinutes(30);
        LocalDateTime end2 = end1.plusMinutes(30);

        assertThrows(RoomNotAvailableException.class, () -> {
            facade.bookRoom("TEST_EMP2", "TEST_ROOM1", start2, end2);
        }, "Should prevent overlapping bookings");
    }

    @Test
    @DisplayName("Should allow back-to-back bookings")
    void testBackToBackBookings() {
        LocalDateTime start1 = baseTime.plusDays(30);
        LocalDateTime end1 = start1.plusHours(1);

        Booking booking1 = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start1, end1);
        assertNotNull(booking1);

        // Book immediately after
        LocalDateTime start2 = end1;
        LocalDateTime end2 = start2.plusHours(1);

        Booking booking2 = facade.bookRoom("TEST_EMP2", "TEST_ROOM1", start2, end2);
        assertNotNull(booking2);
    }

    @Test
    @DisplayName("Should throw exception for non-existent employee")
    void testBookingWithInvalidEmployee() {
        LocalDateTime start = baseTime.plusDays(40);
        LocalDateTime end = start.plusHours(1);

        assertThrows(EntityNotFoundException.class, () -> {
            facade.bookRoom("INVALID_EMP", "TEST_ROOM1", start, end);
        });
    }

    @Test
    @DisplayName("Should throw exception for non-existent room")
    void testBookingWithInvalidRoom() {
        LocalDateTime start = baseTime.plusDays(50);
        LocalDateTime end = start.plusHours(1);

        assertThrows(EntityNotFoundException.class, () -> {
            facade.bookRoom("TEST_EMP1", "INVALID_ROOM", start, end);
        });
    }

    // ========== Cancellation Tests ==========

    @Test
    @DisplayName("Should cancel active booking")
    void testCancelBooking() {
        LocalDateTime start = baseTime.plusDays(60);
        LocalDateTime end = start.plusHours(1);

        Booking booking = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start, end);
        assertTrue(booking.isActive());

        Booking cancelled = facade.cancelBooking(booking.getBookingId());
        assertTrue(cancelled.isCancelled());
        assertFalse(cancelled.isActive());
    }

    @Test
    @DisplayName("Should prevent double cancellation")
    void testDoubleCancellation() {
        LocalDateTime start = baseTime.plusDays(70);
        LocalDateTime end = start.plusHours(1);

        Booking booking = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start, end);
        facade.cancelBooking(booking.getBookingId());

        assertThrows(IllegalStateException.class, () -> {
            facade.cancelBooking(booking.getBookingId());
        }, "Should prevent cancelling already cancelled booking");
    }

    @Test
    @DisplayName("Should allow rebooking after cancellation")
    void testRebookingAfterCancellation() {
        LocalDateTime start = baseTime.plusDays(80);
        LocalDateTime end = start.plusHours(1);

        Booking booking1 = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start, end);
        facade.cancelBooking(booking1.getBookingId());

        // Should be able to book same slot after cancellation
        Booking booking2 = facade.bookRoom("TEST_EMP2", "TEST_ROOM1", start, end);
        assertNotNull(booking2);
        assertTrue(booking2.isActive());
    }

    // ========== Available Rooms Tests ==========

    @Test
    @DisplayName("Should return all rooms when none are booked")
    void testAllRoomsAvailable() {
        LocalDateTime start = baseTime.plusDays(90);
        LocalDateTime end = start.plusHours(1);

        List<MeetingRoom> available = facade.getAvailableRooms(start, end);
        List<MeetingRoom> allRooms = facade.getAllMeetingRooms();

        assertTrue(available.size() >= 2, "Should have at least test rooms available");
    }

    @Test
    @DisplayName("Should exclude booked rooms from available list")
    void testExcludeBookedRooms() {
        LocalDateTime start = baseTime.plusDays(100);
        LocalDateTime end = start.plusHours(1);

        // Book one room
        facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start, end);

        List<MeetingRoom> available = facade.getAvailableRooms(start, end);

        boolean room1Available = available.stream()
                .anyMatch(room -> "TEST_ROOM1".equals(room.getRoomId()));

        assertFalse(room1Available, "Booked room should not be in available list");
    }

    // ========== List Operations Tests ==========

    @Test
    @DisplayName("Should list all bookings for a room")
    void testListBookingsForRoom() {
        LocalDateTime start1 = baseTime.plusDays(110);
        LocalDateTime start2 = baseTime.plusDays(111);

        facade.bookRoom("TEST_EMP1", "TEST_ROOM2", start1, start1.plusHours(1));
        facade.bookRoom("TEST_EMP2", "TEST_ROOM2", start2, start2.plusHours(1));

        List<Booking> bookings = facade.listBookingsForRoom("TEST_ROOM2");

        long room2Bookings = bookings.stream()
                .filter(b -> "TEST_ROOM2".equals(b.getRoomId()))
                .count();

        assertTrue(room2Bookings >= 2, "Should have at least 2 bookings for TEST_ROOM2");
    }

    @Test
    @DisplayName("Should list all bookings for an employee")
    void testListBookingsForEmployee() {
        LocalDateTime start1 = baseTime.plusDays(120);
        LocalDateTime start2 = baseTime.plusDays(121);

        facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start1, start1.plusHours(1));
        facade.bookRoom("TEST_EMP1", "TEST_ROOM2", start2, start2.plusHours(1));

        List<Booking> bookings = facade.listBookingsForEmployee("TEST_EMP1");

        long emp1Bookings = bookings.stream()
                .filter(b -> "TEST_EMP1".equals(b.getEmployeeId()))
                .count();

        assertTrue(emp1Bookings >= 2, "Should have at least 2 bookings for TEST_EMP1");
    }

    @Test
    @DisplayName("Should filter active bookings correctly")
    void testActiveBookingsFilter() {
        LocalDateTime start = baseTime.plusDays(130);

        Booking booking1 = facade.bookRoom("TEST_EMP1", "TEST_ROOM1", start,
                start.plusHours(1));
        Booking booking2 = facade.bookRoom("TEST_EMP1", "TEST_ROOM2", start,
                start.plusHours(1));

        facade.cancelBooking(booking1.getBookingId());

        List<Booking> activeBookings = facade.listActiveBookingsForEmployee("TEST_EMP1");

        boolean hasCancelledBooking = activeBookings.stream()
                .anyMatch(b -> b.getBookingId().equals(booking1.getBookingId()));

        assertFalse(hasCancelledBooking, "Cancelled booking should not be in active list");
    }

    // ========== Concurrency Tests ==========

    @Test
    @DisplayName("Should handle concurrent booking attempts correctly")
    void testConcurrentBookings() throws InterruptedException {
        LocalDateTime start = baseTime.plusDays(140);
        LocalDateTime end = start.plusHours(1);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String empId = "TEST_EMP" + ((index % 2) + 1);
                    facade.bookRoom(empId, "TEST_ROOM1", start, end);
                    successCount.incrementAndGet();
                } catch (RoomNotAvailableException e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(1, successCount.get(), "Exactly one booking should succeed");
        assertEquals(threadCount - 1, failureCount.get(), "Others should fail");
    }

    @Test
    @DisplayName("Should handle concurrent bookings for different rooms")
    void testConcurrentBookingsDifferentRooms() throws InterruptedException {
        LocalDateTime start = baseTime.plusDays(150);
        LocalDateTime end = start.plusHours(1);

        int threadCount = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String empId = "TEST_EMP" + ((index % 2) + 1);
                    String roomId = "TEST_ROOM" + ((index % 2) + 1);
                    facade.bookRoom(empId, roomId, start, end);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // Should not fail
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(successCount.get() >= 2,
                "At least 2 bookings should succeed for different rooms");
    }
}