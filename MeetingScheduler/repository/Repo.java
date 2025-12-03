package MeetingScheduler.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import MeetingScheduler.domain.TimeSlot;
import MeetingScheduler.domain.Booking;
import MeetingScheduler.domain.Employee;
import MeetingScheduler.domain.MeetingRoom;

public class Repo {

    public static class EmployeeRepository {
        private final Map<String, Employee> employees;

        public EmployeeRepository() {
            this.employees = new ConcurrentHashMap<>();
        }

        /**
         * Saves an employee to the repository.
         * 
         * @param employee the employee to save
         * @return the saved employee
         */
        public Employee save(Employee employee) {
            employees.put(employee.getEmployeeId(), employee);
            return employee;
        }

        /**
         * Finds an employee by ID.
         * 
         * @param employeeId the employee ID
         * @return Optional containing the employee if found
         */
        public Optional<Employee> findById(String employeeId) {
            return Optional.ofNullable(employees.get(employeeId));
        }

        /**
         * Retrieves all employees.
         * 
         * @return unmodifiable list of all employees
         */
        public List<Employee> findAll() {
            return Collections.unmodifiableList(
                    new ArrayList<>(employees.values()));
        }

        /**
         * Checks if an employee exists.
         * 
         * @param employeeId the employee ID
         * @return true if employee exists, false otherwise
         */
        public boolean exists(String employeeId) {
            return employees.containsKey(employeeId);
        }

        /**
         * Deletes an employee by ID.
         * 
         * @param employeeId the employee ID
         * @return true if deleted, false if not found
         */
        public boolean deleteById(String employeeId) {
            return employees.remove(employeeId) != null;
        }
    }

    /**
     * Repository for managing MeetingRoom entities.
     * Thread-safe implementation using ConcurrentHashMap.
     */
    public static class MeetingRoomRepository {
        private final Map<String, MeetingRoom> rooms;

        public MeetingRoomRepository() {
            this.rooms = new ConcurrentHashMap<>();
        }

        /**
         * Saves a meeting room to the repository.
         * 
         * @param room the meeting room to save
         * @return the saved meeting room
         */
        public MeetingRoom save(MeetingRoom room) {
            rooms.put(room.getRoomId(), room);
            return room;
        }

        /**
         * Finds a meeting room by ID.
         * 
         * @param roomId the room ID
         * @return Optional containing the room if found
         */
        public Optional<MeetingRoom> findById(String roomId) {
            return Optional.ofNullable(rooms.get(roomId));
        }

        /**
         * Retrieves all meeting rooms.
         * 
         * @return unmodifiable list of all rooms
         */
        public List<MeetingRoom> findAll() {
            return Collections.unmodifiableList(
                    new ArrayList<>(rooms.values()));
        }

        /**
         * Finds rooms by minimum capacity.
         * 
         * @param minCapacity minimum capacity required
         * @return list of rooms meeting the capacity requirement
         */
        public List<MeetingRoom> findByMinCapacity(int minCapacity) {
            return rooms.values().stream()
                    .filter(room -> room.getCapacity() >= minCapacity)
                    .collect(Collectors.toList());
        }

        /**
         * Finds rooms by location.
         * 
         * @param location the location to search for
         * @return list of rooms at the specified location
         */
        public List<MeetingRoom> findByLocation(String location) {
            return rooms.values().stream()
                    .filter(room -> room.getLocation().equalsIgnoreCase(location))
                    .collect(Collectors.toList());
        }

        /**
         * Checks if a room exists.
         * 
         * @param roomId the room ID
         * @return true if room exists, false otherwise
         */
        public boolean exists(String roomId) {
            return rooms.containsKey(roomId);
        }

        /**
         * Deletes a room by ID.
         * 
         * @param roomId the room ID
         * @return true if deleted, false if not found
         */
        public boolean deleteById(String roomId) {
            return rooms.remove(roomId) != null;
        }
    }

    /**
     * Repository for managing Booking entities.
     * Thread-safe implementation with support for complex queries.
     */
    public static class BookingRepository {
        private final Map<String, Booking> bookings;
        private final Map<String, List<String>> roomBookings; // roomId -> List of bookingIds
        private final Map<String, List<String>> employeeBookings; // employeeId -> List of bookingIds

        public BookingRepository() {
            this.bookings = new ConcurrentHashMap<>();
            this.roomBookings = new ConcurrentHashMap<>();
            this.employeeBookings = new ConcurrentHashMap<>();
        }

        /**
         * Saves a booking to the repository.
         * Maintains indexes for efficient querying by room and employee.
         * 
         * @param booking the booking to save
         * @return the saved booking
         */
        public Booking save(Booking booking) {
            bookings.put(booking.getBookingId(), booking);

            // Update room index
            roomBookings.computeIfAbsent(
                    booking.getRoomId(),
                    k -> new ArrayList<>()).add(booking.getBookingId());

            // Update employee index
            employeeBookings.computeIfAbsent(
                    booking.getEmployeeId(),
                    k -> new ArrayList<>()).add(booking.getBookingId());

            return booking;
        }

        /**
         * Finds a booking by ID.
         * 
         * @param bookingId the booking ID
         * @return Optional containing the booking if found
         */
        public Optional<Booking> findById(String bookingId) {
            return Optional.ofNullable(bookings.get(bookingId));
        }

        /**
         * Retrieves all bookings.
         * 
         * @return unmodifiable list of all bookings
         */
        public List<Booking> findAll() {
            return Collections.unmodifiableList(
                    new ArrayList<>(bookings.values()));
        }

        /**
         * Finds all bookings for a specific room.
         * 
         * @param roomId the room ID
         * @return list of bookings for the room
         */
        public List<Booking> findByRoomId(String roomId) {
            return roomBookings.getOrDefault(roomId, Collections.emptyList())
                    .stream()
                    .map(bookings::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        /**
         * Finds all active bookings for a specific room.
         * 
         * @param roomId the room ID
         * @return list of active bookings for the room
         */
        public List<Booking> findActiveBookingsByRoomId(String roomId) {
            return findByRoomId(roomId).stream()
                    .filter(Booking::isActive)
                    .collect(Collectors.toList());
        }

        /**
         * Finds all bookings made by a specific employee.
         * 
         * @param employeeId the employee ID
         * @return list of bookings made by the employee
         */
        public List<Booking> findByEmployeeId(String employeeId) {
            return employeeBookings.getOrDefault(employeeId, Collections.emptyList())
                    .stream()
                    .map(bookings::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        /**
         * Finds all active bookings for a specific employee.
         * 
         * @param employeeId the employee ID
         * @return list of active bookings for the employee
         */
        public List<Booking> findActiveBookingsByEmployeeId(String employeeId) {
            return findByEmployeeId(employeeId).stream()
                    .filter(Booking::isActive)
                    .collect(Collectors.toList());
        }

        /**
         * Finds bookings that overlap with a given time slot for a specific room.
         * Only considers ACTIVE bookings.
         * 
         * @param roomId   the room ID
         * @param timeSlot the time slot to check
         * @return list of overlapping active bookings
         */
        public List<Booking> findOverlappingBookings(String roomId, TimeSlot timeSlot) {
            return findActiveBookingsByRoomId(roomId).stream()
                    .filter(booking -> booking.overlaps(timeSlot))
                    .collect(Collectors.toList());
        }

        /**
         * Checks if a room is available for a given time slot.
         * 
         * @param roomId   the room ID
         * @param timeSlot the time slot to check
         * @return true if room is available, false otherwise
         */
        public boolean isRoomAvailable(String roomId, TimeSlot timeSlot) {
            return findOverlappingBookings(roomId, timeSlot).isEmpty();
        }

        /**
         * Deletes a booking by ID.
         * 
         * @param bookingId the booking ID
         * @return true if deleted, false if not found
         */
        public boolean deleteById(String bookingId) {
            Booking booking = bookings.remove(bookingId);
            if (booking != null) {
                // Clean up indexes
                roomBookings.getOrDefault(booking.getRoomId(), Collections.emptyList())
                        .remove(bookingId);
                employeeBookings.getOrDefault(booking.getEmployeeId(), Collections.emptyList())
                        .remove(bookingId);
                return true;
            }
            return false;
        }
    }
}
