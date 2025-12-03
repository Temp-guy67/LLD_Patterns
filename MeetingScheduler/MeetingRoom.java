package MeetingScheduler;

class MeetingRoom {
    private final String roomId;
    private final String name;
    private final int capacity;
    private final String location;

    public MeetingRoom(String roomId, String name, int capacity, String location) {
        validateMeetingRoom(roomId, name, capacity, location);
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    private void validateMeetingRoom(String id, String name, int capacity, String location) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Room location cannot be null or empty");
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingRoom that = (MeetingRoom) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return String.format("MeetingRoom{id='%s', name='%s', capacity=%d, location='%s'}", 
                           roomId, name, capacity, location);
    }
}