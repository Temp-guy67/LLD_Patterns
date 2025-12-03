package MeetingScheduler.exception;

public class RoomNotAvailableException extends MeetingRoomException {

    public RoomNotAvailableException(String roomId, String timeSlot) {
        super(String.format(
                "Room '%s' is not available for the requested time slot: %s",
                roomId, timeSlot));
    }
}