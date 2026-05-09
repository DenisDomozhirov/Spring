package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.Account;
import work.dir.Account.AccountService;
import work.dir.User.UserService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class AccountCreateCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final ConsoleInput consoleInput;

    public AccountCreateCommand(AccountService accountService, UserService userService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        int userId = consoleInput.readPositiveInt("Enter user id: ", "user id");
        var user = userService.userFindById(userId);
        Account account = accountService.createAccount(user);
        user.getAccountList().add(account);
        System.out.println("Account created: " + account);
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.ACCOUNT_CREATE;
    }

}
