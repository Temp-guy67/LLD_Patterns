package MeetingScheduler.service;

import MeetingScheduler.domain.*;
import MeetingScheduler.repository.Repo.EmployeeRepository;
import MeetingScheduler.repository.Repo.BookingRepository;
import MeetingScheduler.repository.Repo.MeetingRoomRepository;
import MeetingScheduler.service.Service.EmployeeService;
import MeetingScheduler.service.Service.MeetingRoomService;
import MeetingScheduler.exception.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Service {

    /**
     * Service class for managing employees.
     * Provides business logic layer for employee operations.
     * 
     * @author Meeting Room Platform Team
     * @version 1.0
     */
    public static class EmployeeService {
        private final EmployeeRepository employeeRepository;

        public EmployeeService(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        /**
         * Creates a new employee.
         * 
         * @param employeeId unique identifier
         * @param name       employee name
         * @param email      employee email
         * @return the created employee
         */
        public Employee createEmployee(String employeeId, String name, String email) {
            if (employeeRepository.exists(employeeId)) {
                throw new InvalidBookingException("Employee with ID '" + employeeId + "' already exists");
            }

            Employee employee = new Employee(employeeId, name, email);
            return employeeRepository.save(employee);
        }

        /**
         * Retrieves an employee by ID.
         * 
         * @param employeeId the employee ID
         * @return the employee
         * @throws EntityNotFoundException if employee not found
         */
        public Employee getEmployee(String employeeId) {
            return employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
        }

        /**
         * Retrieves all employees.
         * 
         * @return list of all employees
         */
        public List<Employee> getAllEmployees() {
            return employeeRepository.findAll();
        }

        /**
         * Checks if an employee exists.
         * 
         * @param employeeId the employee ID
         * @return true if exists, false otherwise
         */
        public boolean employeeExists(String employeeId) {
            return employeeRepository.exists(employeeId);
        }
    }

    /**
     * Service class for managing meeting rooms.
     * Provides business logic layer for room operations.
     */
    public static class MeetingRoomService {
        private final MeetingRoomRepository roomRepository;

        public MeetingRoomService(MeetingRoomRepository roomRepository) {
            this.roomRepository = roomRepository;
        }

        /**
         * Creates a new meeting room.
         * 
         * @param roomId   unique identifier
         * @param name     room name
         * @param capacity room capacity
         * @param location room location
         * @return the created meeting room
         */
        public MeetingRoom createRoom(String roomId, String name, int capacity, String location) {
            if (roomRepository.exists(roomId)) {
                throw new InvalidBookingException(
                        "Room with ID '" + roomId + "' already exists");
            }

            MeetingRoom room = new MeetingRoom(roomId, name, capacity, location);
            return roomRepository.save(room);
        }

        /**
         * Retrieves a room by ID.
         * 
         * @param roomId the room ID
         * @return the meeting room
         * @throws EntityNotFoundException if room not found
         */
        public MeetingRoom getRoom(String roomId) {
            return roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room", roomId));
        }

        /**
         * Retrieves all meeting rooms.
         * 
         * @return list of all rooms
         */
        public List<MeetingRoom> getAllRooms() {
            return roomRepository.findAll();
        }

        /**
         * Finds rooms by minimum capacity requirement.
         * 
         * @param minCapacity minimum capacity needed
         * @return list of suitable rooms
         */
        public List<MeetingRoom> getRoomsByCapacity(int minCapacity) {
            return roomRepository.findByMinCapacity(minCapacity);
        }

        /**
         * Finds rooms by location.
         * 
         * @param location the location to search
         * @return list of rooms at location
         */
        public List<MeetingRoom> getRoomsByLocation(String location) {
            return roomRepository.findByLocation(location);
        }

        /**
         * Checks if a room exists.
         * 
         * @param roomId the room ID
         * @return true if exists, false otherwise
         */
        public boolean roomExists(String roomId) {
            return roomRepository.exists(roomId);
        }
    }

    /**
     * Service class for managing bookings.
     * Handles thread-safe booking operations with proper locking.
     * 
     * This is the core service that implements the main business logic.
     */
    public static class BookingService {
        private final BookingRepository bookingRepository;
        private final EmployeeService employeeService;
        private final MeetingRoomService roomService;

        // Fine-grained locking per room for better concurrency
        private final Map<String, ReadWriteLock> roomLocks;

        public BookingService(
                BookingRepository bookingRepository,
                EmployeeService employeeService,
                MeetingRoomService roomService) {
            this.bookingRepository = bookingRepository;
            this.employeeService = employeeService;
            this.roomService = roomService;
            this.roomLocks = new HashMap<>();
        }

        /**
         * Gets or creates a lock for a specific room.
         * Synchronized to ensure thread-safe lock creation.
         */
        private synchronized ReadWriteLock getLockForRoom(String roomId) {
            return roomLocks.computeIfAbsent(roomId, k -> new ReentrantReadWriteLock());
        }

        /**
         * Books a meeting room for an employee.
         * Thread-safe implementation using room-level locking.
         * 
         * @param employeeId ID of the employee making the booking
         * @param roomId     ID of the room to book
         * @param startTime  start time of the meeting
         * @param endTime    end time of the meeting
         * @return the created booking
         * @throws EntityNotFoundException   if employee or room not found
         * @throws RoomNotAvailableException if room is already booked
         */
        public Booking bookRoom(
                String employeeId,
                String roomId,
                LocalDateTime startTime,
                LocalDateTime endTime) {

            // Validate employee and room exist
            if (!employeeService.employeeExists(employeeId)) {
                throw new EntityNotFoundException("Employee", employeeId);
            }
            if (!roomService.roomExists(roomId)) {
                throw new EntityNotFoundException("Room", roomId);
            }

            // Create and validate time slot
            TimeSlot requestedSlot = new TimeSlot(startTime, endTime);

            // Get room-specific lock for thread safety
            ReadWriteLock roomLock = getLockForRoom(roomId);
            roomLock.writeLock().lock();

            try {
                // Check for overlapping bookings
                List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomId, requestedSlot);

                if (!overlappingBookings.isEmpty()) {
                    throw new RoomNotAvailableException(roomId, requestedSlot.toString());
                }

                // Create and save booking
                String bookingId = generateBookingId();
                Booking booking = new Booking(bookingId, employeeId, roomId, requestedSlot);

                return bookingRepository.save(booking);

            } finally {
                roomLock.writeLock().unlock();
            }
        }

        /**
         * Gets all available rooms for a given time slot.
         * Uses read locks for thread-safe concurrent reads.
         * 
         * @param startTime start time of desired slot
         * @param endTime   end time of desired slot
         * @return list of available rooms
         */
        public List<MeetingRoom> getAvailableRooms(
                LocalDateTime startTime,
                LocalDateTime endTime) {

            TimeSlot requestedSlot = new TimeSlot(startTime, endTime);
            List<MeetingRoom> allRooms = roomService.getAllRooms();

            return allRooms.stream()
                    .filter(room -> isRoomAvailable(room.getRoomId(), requestedSlot))
                    .collect(Collectors.toList());
        }

        /**
         * Checks if a specific room is available for a time slot.
         * Thread-safe with read lock.
         * 
         * @param roomId   the room ID
         * @param timeSlot the time slot to check
         * @return true if available, false otherwise
         */
        private boolean isRoomAvailable(String roomId, TimeSlot timeSlot) {
            ReadWriteLock roomLock = getLockForRoom(roomId);
            roomLock.readLock().lock();

            try {
                return bookingRepository.isRoomAvailable(roomId, timeSlot);
            } finally {
                roomLock.readLock().unlock();
            }
        }

        /**
         * Cancels an existing booking.
         * 
         * @param bookingId the booking ID to cancel
         * @return the cancelled booking
         * @throws EntityNotFoundException if booking not found
         * @throws IllegalStateException   if booking cannot be cancelled
         */
        public Booking cancelBooking(String bookingId) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking", bookingId));

            ReadWriteLock roomLock = getLockForRoom(booking.getRoomId());
            roomLock.writeLock().lock();

            try {
                booking.cancel();
                return booking;
            } finally {
                roomLock.writeLock().unlock();
            }
        }

        /**
         * Lists all bookings for a specific room.
         * 
         * @param roomId the room ID
         * @return list of bookings
         */
        public List<Booking> listBookingsForRoom(String roomId) {
            if (!roomService.roomExists(roomId)) {
                throw new EntityNotFoundException("Room", roomId);
            }

            return bookingRepository.findByRoomId(roomId);
        }

        /**
         * Lists all active bookings for a specific room.
         * 
         * @param roomId the room ID
         * @return list of active bookings
         */
        public List<Booking> listActiveBookingsForRoom(String roomId) {
            if (!roomService.roomExists(roomId)) {
                throw new EntityNotFoundException("Room", roomId);
            }

            return bookingRepository.findActiveBookingsByRoomId(roomId);
        }

        /**
         * Lists all bookings made by a specific employee.
         * 
         * @param employeeId the employee ID
         * @return list of bookings
         */
        public List<Booking> listBookingsForEmployee(String employeeId) {
            if (!employeeService.employeeExists(employeeId)) {
                throw new EntityNotFoundException("Employee", employeeId);
            }

            return bookingRepository.findByEmployeeId(employeeId);
        }

        /**
         * Lists all active bookings for a specific employee.
         * 
         * @param employeeId the employee ID
         * @return list of active bookings
         */
        public List<Booking> listActiveBookingsForEmployee(String employeeId) {
            if (!employeeService.employeeExists(employeeId)) {
                throw new EntityNotFoundException("Employee", employeeId);
            }

            return bookingRepository.findActiveBookingsByEmployeeId(employeeId);
        }

        /**
         * Gets a booking by ID.
         * 
         * @param bookingId the booking ID
         * @return the booking
         * @throws EntityNotFoundException if booking not found
         */
        public Booking getBooking(String bookingId) {
            return bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking", bookingId));
        }

        /**
         * Generates a unique booking ID.
         * In production, this could use UUID or a distributed ID generator.
         * 
         * @return unique booking ID
         */
        private String generateBookingId() {
            return "BKG-" + UUID.randomUUID().toString();
        }

        /**
         * Retrieves all bookings in the system.
         * 
         * @return list of all bookings
         */
        public List<Booking> getAllBookings() {
            return bookingRepository.findAll();
        }
    }
}