package work.dir.operations.commands;

import org.springframework.stereotype.Component;
import work.dir.Account.AccountService;
import work.dir.console.ConsoleInput;
import work.dir.operations.ConsoleOperationType;
import work.dir.operations.OperationCommand;

@Component
public class AccountTransferCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountTransferCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute(){
        int fromAccountId = consoleInput.readPositiveInt("Enter from account id", "account id");
        int toAccountId = consoleInput.readPositiveInt("Enter to account id", "account id");
        if(fromAccountId == toAccountId) throw new IllegalArgumentException("Error!" +
                "source and target should be different!");


        int amount = consoleInput.readPositiveInt("Enter amount: ", "amount");
        accountService.transferMoney(fromAccountId, toAccountId, amount);
        System.out.println("Transfer complete! From - " + fromAccountId + " to - " + toAccountId +
                ", amount = " + amount);
    }

    @Override
    public ConsoleOperationType getOperationType(){
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
