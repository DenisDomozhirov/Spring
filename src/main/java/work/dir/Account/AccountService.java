package work.dir.Account;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import work.dir.User.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    private int idCounter;
    private final Map<Integer, Account> accountMap;
    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.idCounter = 0;
        this.accountMap = new HashMap<>();
        this.accountProperties = accountProperties;
    }

    public Account createAccount(User user){
        if(user == null) throw new IllegalArgumentException("User cant be null!");

        idCounter++;
        Account newAccount = new Account(idCounter, user.getId(), accountProperties.getDefaultAmount());
        accountMap.put(idCounter, newAccount);
        return newAccount;
    }

    public Optional<Account> findAccountById(Integer id){
        var accountId = accountMap.get(id);
        return Optional.ofNullable(accountId);
    }

    public List<Account> getUsersAccount(Integer userId){
        return accountMap.values().stream()
                .filter(n -> userId.equals(n.getUserID()))
                .toList();
    }

    public void withdraw(Integer fromAccountID, Integer amount){
        validatePositiveAccountId(fromAccountID, "account id");
        validatePositiveAmount(amount);

        Account account = findAccountById(fromAccountID)
                .orElseThrow(() -> new IllegalArgumentException("No such account: %s"
                        .formatted(fromAccountID)));

        if(amount > account.getMoneyAmount()) throw new IllegalArgumentException("Error! " +
                "insufficient funds in the account id=%s, amount of money =%s, withdrawal attempt =%s"
                        .formatted(fromAccountID, account.getMoneyAmount(), amount));
        account.setMoneyAmount(account.getMoneyAmount() - amount);
    }

    public void deposit(Integer toAccountId, Integer amount){
        validatePositiveAccountId(toAccountId, "account id");
        validatePositiveAmount(amount);

        Account account = findAccountById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Error! No such element " +
                        "with id: %s".formatted(toAccountId)));

        account.setMoneyAmount(account.getMoneyAmount() + amount);
    }


    public Account closeAccount(Integer accountId){
        validatePositiveAccountId(accountId, "account id");
        Account accountToClose = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: %s"
                        .formatted(accountId)));

        var userId = accountToClose.getUserID();
        var userAccounts = getUsersAccount(userId);
        if(userAccounts.size() == 1) throw new IllegalArgumentException("Error! Cant close " +
                "only one account");
        accountMap.remove(accountId);

        var accountToTransferMoney = userAccounts.stream()
                .filter(n -> n.getId() != accountId)
                .findFirst()
                .orElseThrow();

        var newAmount = accountToTransferMoney.getMoneyAmount() + accountToClose.getMoneyAmount();
        accountToTransferMoney.setMoneyAmount(newAmount);
        return accountToClose;
    }

    public void transferMoney(int fromAccountId, int toAccountId, int amount){
        validatePositiveAccountId(fromAccountId, "from - account id");
        validatePositiveAccountId(toAccountId, "to - account id");
        validatePositiveAmount(amount);

        if(fromAccountId == toAccountId) throw new IllegalArgumentException("Error!" +
                "source and target account id should be different!");

        Account accountFrom = exceptionFindById(fromAccountId, "Account from!");

        Account accountTo = exceptionFindById(fromAccountId, "Account to!");

        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);

        int amountToTransfer = accountTo.getUserID() == accountFrom.getUserID() ? amount :
                (int) Math.round(amount * (1 - accountProperties.getMoneyCommission()));
        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToTransfer);
    }

    private Account exceptionFindById(int accountId, String whichAccount){
        return findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Error!" +
                        " No such account with id: %s (%d)".formatted(accountId, whichAccount)));
    }

    public void validatePositiveAccountId(Integer id, String fieldName){
        if(id == null || id <= 0) throw new IllegalArgumentException("Error! " + fieldName +
                " should be > 0!");
    }

    public void validatePositiveAmount(Integer amount){
        if(amount == null || amount <= 0) throw new IllegalArgumentException("Error! amount " +
                "should be > 0!");
    }

}
