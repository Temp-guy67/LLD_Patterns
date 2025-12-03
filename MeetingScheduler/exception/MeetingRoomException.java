package MeetingScheduler.exception;

public class MeetingRoomException extends RuntimeException {
    public MeetingRoomException(String message) {
        super(message);
    }

    public MeetingRoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
