package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.User.User;
import work.dir.User.UserService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class AccountDepositCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountDepositCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        int accountId = consoleInput.readPositiveInt("Enter account id: ", "account id");
        int amount = consoleInput.readPositiveInt("Enter amount: ", "amoumt");

        accountService.deposit(accountId, amount);
        System.out.println("Deposited: " + amount + " to account - " + accountId);
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }

}

