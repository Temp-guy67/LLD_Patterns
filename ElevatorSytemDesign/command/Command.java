package ElevatorSytemDesign.command;

// ==================== COMMAND PATTERN ====================
public interface Command {
    void execute();

    void undo();

    long getTimestamp();

}
