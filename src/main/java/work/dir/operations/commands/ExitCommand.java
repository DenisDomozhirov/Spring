package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class ExitCommand implements OperationCommand {


    @Override
    public void execute(){
        System.out.println("Session stopped!");
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.EXIT;
    }
}