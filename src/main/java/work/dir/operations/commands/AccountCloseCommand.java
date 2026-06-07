package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.User.UserService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class AccountCloseCommand implements OperationCommand {
    private final AccountService accountService;
    private final UserService userService;
    private final ConsoleInput consoleInput;

    public AccountCloseCommand(AccountService accountService, UserService userService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        int accountId = consoleInput.readPositiveInt("Enter account id to close: ", "account id");
        var closeAccount = accountService.closeAccount(accountId);
        var user = userService.userFindById(closeAccount.getUser().getId());
        user.getAccountList().remove(closeAccount);
        System.out.println("Account " + accountId + " closed!");
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }

}
