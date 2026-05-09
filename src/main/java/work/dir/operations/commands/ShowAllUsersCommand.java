package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.User.User;
import work.dir.User.UserService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class ShowAllUsersCommand implements OperationCommand {

    private final UserService userService;


    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(){
        System.out.println("All users: ");
        userService.findAll().forEach(System.out::println);
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.SHOW_ALLUSERS;
    }
}
