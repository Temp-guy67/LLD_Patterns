package MeetingScheduler.exception;

public class EntityNotFoundException extends MeetingRoomException {

    public EntityNotFoundException(String entityType, String id) {
        super(String.format("%s with ID '%s' not found", entityType, id));
    }
}
