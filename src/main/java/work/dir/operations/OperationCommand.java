package work.dir.operations;

public interface OperationCommand {
    void execute();
    ConsoleOperationType getOperationType();
}
