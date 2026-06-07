package work.dir.console;

import org.springframework.stereotype.Component;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OperationConsoleListener {

    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final ConsoleInput consoleInput;
    private boolean isRunning;

    public OperationConsoleListener(
            List<OperationCommand> operationCommandList,
            ConsoleInput consoleInput
    ) {
        this.commandMap = operationCommandList.stream()
                .collect(Collectors.toMap(OperationCommand::getOperationType, n -> n));
        this.consoleInput = consoleInput;
        this.isRunning = true;
    }

    public void runProgramm(){
        init();
        procces();
    }

    private void procces(){
        while (isRunning){
            var nextOperation = consoleInput.readOperationType();
            proccesNextCoomand(nextOperation);
            if(nextOperation == ConsoleOperationType.EXIT) isRunning = false;
        }
    }

    public void proccesNextCoomand(ConsoleOperationType consoleOperationType){
        try{
            OperationCommand command = commandMap.get(consoleOperationType);
            if(command == null){
                throw new IllegalStateException("No command handler for " + consoleOperationType);
            }
            command.execute();
        } catch(IllegalArgumentException | IllegalStateException e){
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e){
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            System.out.println("Error: " + message);
        }
    }

    public void init(){
        System.out.println("Programm started! Chose EXIT for close!");
        consoleInput.printAvailbleCommands();
    }
}
