package MeetingScheduler.exception;

public class ConcurrentBookingException extends MeetingRoomException {

    public ConcurrentBookingException(String roomId) {
        super(String.format(
                "Room '%s' was just booked by another user. Please try again.",
                roomId));
    }
}