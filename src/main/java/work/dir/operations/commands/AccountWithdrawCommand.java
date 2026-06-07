package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class AccountWithdrawCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountWithdrawCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        int fromAccountId = consoleInput.readPositiveInt("Enter from account id", "account id");

        int amount = consoleInput.readPositiveInt("Enter amount: ", "amount");
        accountService.withdraw(fromAccountId, amount);
        System.out.println("Withdraw complete! From - %s, amount = %d".formatted(fromAccountId, amount));
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}