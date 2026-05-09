package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.User.UserService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class UserCreateCommand implements OperationCommand {

    private final UserService userService;
    private final ConsoleInput consoleInput;

    public UserCreateCommand(UserService userService, ConsoleInput consoleInput) {
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        String login = consoleInput.readRequiredString("Enter user login: ", "user login");
        var user = userService.createUser(login);
        System.out.println("New user created! " + user);
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.USER_CREATE;
    }
}
