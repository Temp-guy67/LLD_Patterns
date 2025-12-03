package MeetingScheduler;

import java.time.LocalDateTime;
import java.util.Objects;


public class TimeSlot {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        validateTimeSlot(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateTimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start time and end time cannot be the same");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create time slot in the past");
        }
    }

    public boolean overlaps(TimeSlot other) {
        if (other == null) {
            return false;
        }
        // Overlap occurs if: start1 < end2 AND start2 < end1
        return this.startTime.isBefore(other.endTime) && 
               other.startTime.isBefore(this.endTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(startTime, timeSlot.startTime) &&
               Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return String.format("TimeSlot{%s to %s}", startTime, endTime);
    }
}