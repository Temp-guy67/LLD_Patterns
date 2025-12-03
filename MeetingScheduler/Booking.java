package MeetingScheduler;

class Booking {
    private final String bookingId;
    private final String employeeId;
    private final String roomId;
    private final TimeSlot timeSlot;
    private final LocalDateTime createdAt;
    private volatile BookingStatus status;

    /**
     * Constructs a Booking with ACTIVE status.
     * 
     * @param bookingId unique identifier (auto-generated)
     * @param employeeId ID of the employee making the booking
     * @param roomId ID of the room being booked
     * @param timeSlot time interval for the booking
     */

    public Booking(String bookingId, String employeeId, String roomId, TimeSlot timeSlot) {
        validateBooking(bookingId, employeeId, roomId, timeSlot);
        this.bookingId = bookingId;
        this.employeeId = employeeId;
        this.roomId = roomId;
        this.timeSlot = timeSlot;
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.ACTIVE;
    }

    private void validateBooking(String bookingId, String empId, String rmId, TimeSlot slot) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
        if (empId == null || empId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (rmId == null || rmId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (slot == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }
    }

    /**
     * Checks if this booking overlaps with a given time slot.
     * Only ACTIVE bookings are considered for overlap.
     * 
     * @param other the time slot to check against
     * @return true if booking is active and overlaps, false otherwise
     */
    public boolean overlaps(TimeSlot other) {
        return isActive() && timeSlot.overlaps(other);
    }

    /**
     * Cancels this booking if it's currently active.
     * 
     * @throws IllegalStateException if booking is not active
     */
    public synchronized void cancel() {
        if (!isActive()) {
            throw new IllegalStateException(
                "Cannot cancel booking - current status: " + status);
        }
        this.status = BookingStatus.CANCELLED;
    }

    /**
     * Marks this booking as completed.
     * Typically called by a scheduled job after meeting time has passed.
     */
    public synchronized void complete() {
        if (isActive()) {
            this.status = BookingStatus.COMPLETED;
        }
    }

    public boolean isActive() {
        return status == BookingStatus.ACTIVE;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == BookingStatus.COMPLETED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    @Override
    public String toString() {
        return String.format(
            "Booking{id='%s', employee='%s', room='%s', timeSlot=%s, status=%s}", 
            bookingId, employeeId, roomId, timeSlot, status);
    }
}