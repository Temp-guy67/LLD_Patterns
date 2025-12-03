package MeetingScheduler;

import MeetingScheduler.domain.Booking;
import MeetingScheduler.domain.Employee;
import MeetingScheduler.domain.MeetingRoom;
import MeetingScheduler.repository.Repo.BookingRepository;
import MeetingScheduler.repository.Repo.EmployeeRepository;
import MeetingScheduler.repository.Repo.MeetingRoomRepository;
import MeetingScheduler.service.Service.BookingService;
import MeetingScheduler.service.Service.EmployeeService;
import MeetingScheduler.service.Service.MeetingRoomService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Facade pattern implementation for the Meeting Room Reservation System.
 * Provides a simplified interface to the complex subsystem.
 * Singleton pattern ensures only one instance exists.
 * 
 * This is the main entry point for clients using the system.
 * 
 * @author Meeting Room Platform Team
 * @version 1.0
 */
public class MeetingRoomReservationFacade {

    private static volatile MeetingRoomReservationFacade instance;

    private final BookingService bookingService;
    private final EmployeeService employeeService;
    private final MeetingRoomService roomService;

    /**
     * Private constructor for Singleton pattern.
     * Initializes all services with their dependencies.
     */
    private MeetingRoomReservationFacade() {
        // Initialize repositories
        EmployeeRepository employeeRepo = new EmployeeRepository();
        MeetingRoomRepository roomRepo = new MeetingRoomRepository();
        BookingRepository bookingRepo = new BookingRepository();

        // Initialize services
        this.employeeService = new EmployeeService(employeeRepo);
        this.roomService = new MeetingRoomService(roomRepo);
        this.bookingService = new BookingService(bookingRepo, employeeService, roomService);
    }

    /**
     * Gets the singleton instance of the facade.
     * Thread-safe double-checked locking.
     * 
     * @return the singleton instance
     */
    public static MeetingRoomReservationFacade getInstance() {
        if (instance == null) {
            synchronized (MeetingRoomReservationFacade.class) {
                if (instance == null) {
                    instance = new MeetingRoomReservationFacade();
                }
            }
        }
        return instance;
    }

    // ========== Employee Operations ==========

    /**
     * Creates a new employee in the system.
     * 
     * @param employeeId unique identifier
     * @param name       employee name
     * @param email      employee email
     * @return the created employee
     */
    public Employee createEmployee(String employeeId, String name, String email) {
        return employeeService.createEmployee(employeeId, name, email);
    }

    /**
     * Retrieves an employee by ID.
     * 
     * @param employeeId the employee ID
     * @return the employee
     */
    public Employee getEmployee(String employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    /**
     * Gets all employees in the system.
     * 
     * @return list of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // ========== Meeting Room Operations ==========

    /**
     * Creates a new meeting room in the system.
     * 
     * @param roomId   unique identifier
     * @param name     room name
     * @param capacity room capacity
     * @param location room location
     * @return the created meeting room
     */
    public MeetingRoom createMeetingRoom(
            String roomId, String name, int capacity, String location) {
        return roomService.createRoom(roomId, name, capacity, location);
    }

    /**
     * Retrieves a meeting room by ID.
     * 
     * @param roomId the room ID
     * @return the meeting room
     */
    public MeetingRoom getMeetingRoom(String roomId) {
        return roomService.getRoom(roomId);
    }

    /**
     * Gets all meeting rooms in the system.
     * 
     * @return list of all meeting rooms
     */
    public List<MeetingRoom> getAllMeetingRooms() {
        return roomService.getAllRooms();
    }

    /**
     * Finds rooms with minimum capacity requirement.
     * 
     * @param minCapacity minimum capacity needed
     * @return list of suitable rooms
     */
    public List<MeetingRoom> getRoomsByCapacity(int minCapacity) {
        return roomService.getRoomsByCapacity(minCapacity);
    }

    /**
     * Finds rooms by location.
     * 
     * @param location the location
     * @return list of rooms at location
     */
    public List<MeetingRoom> getRoomsByLocation(String location) {
        return roomService.getRoomsByLocation(location);
    }

    // ========== Core Booking Operations ==========

    /**
     * Books a meeting room for an employee.
     * Core operation - thread-safe implementation.
     * 
     * @param employeeId ID of employee making booking
     * @param roomId     ID of room to book
     * @param startTime  start time of meeting
     * @param endTime    end time of meeting
     * @return the created booking
     * @throws com.meetingroom.exception.EntityNotFoundException   if employee or
     *                                                             room not found
     * @throws com.meetingroom.exception.RoomNotAvailableException if room is not
     *                                                             available
     */
    public Booking bookRoom(
            String employeeId,
            String roomId,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return bookingService.bookRoom(employeeId, roomId, startTime, endTime);
    }

    /**
     * Gets all available rooms for a given time interval.
     * Core operation for checking availability.
     * 
     * @param startTime start time of desired interval
     * @param endTime   end time of desired interval
     * @return list of available meeting rooms
     */
    public List<MeetingRoom> getAvailableRooms(
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return bookingService.getAvailableRooms(startTime, endTime);
    }

    /**
     * Cancels an existing booking.
     * 
     * @param bookingId the booking ID to cancel
     * @return the cancelled booking
     * @throws com.meetingroom.exception.EntityNotFoundException if booking not
     *                                                           found
     */
    public Booking cancelBooking(String bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    /**
     * Lists all bookings for a specific room.
     * 
     * @param roomId the room ID
     * @return list of bookings for the room
     */
    public List<Booking> listBookingsForRoom(String roomId) {
        return bookingService.listBookingsForRoom(roomId);
    }

    /**
     * Lists only active bookings for a specific room.
     * 
     * @param roomId the room ID
     * @return list of active bookings for the room
     */
    public List<Booking> listActiveBookingsForRoom(String roomId) {
        return bookingService.listActiveBookingsForRoom(roomId);
    }

    /**
     * Lists all bookings made by a specific employee.
     * 
     * @param employeeId the employee ID
     * @return list of bookings made by employee
     */
    public List<Booking> listBookingsForEmployee(String employeeId) {
        return bookingService.listBookingsForEmployee(employeeId);
    }

    /**
     * Lists only active bookings for a specific employee.
     * 
     * @param employeeId the employee ID
     * @return list of active bookings for employee
     */
    public List<Booking> listActiveBookingsForEmployee(String employeeId) {
        return bookingService.listActiveBookingsForEmployee(employeeId);
    }

    /**
     * Gets a booking by ID.
     * 
     * @param bookingId the booking ID
     * @return the booking
     */
    public Booking getBooking(String bookingId) {
        return bookingService.getBooking(bookingId);
    }

    /**
     * Gets all bookings in the system.
     * 
     * @return list of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }
}