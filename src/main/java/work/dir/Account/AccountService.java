package work.dir.Account;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import work.dir.TransactionHelperClass;
import work.dir.User.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    private final SessionFactory sessionFactory;
    private final TransactionHelperClass transactionHelper;

    private final AccountProperties accountProperties;

    public AccountService(
            AccountProperties accountProperties,
            SessionFactory sessionFactory,
            TransactionHelperClass transactionHelper
            ) {
        this.accountProperties = accountProperties;
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }

    public Account createAccount(User user){
        return transactionHelper.executeInTransaction(session -> {
            if(user == null) throw new IllegalArgumentException("User cant be null!");

            Account account = new Account(accountProperties.getDefaultAmount(), user);
            session.persist(account);
            return account;
        });

    }

    public Optional<Account> findAccountById(Integer id){
        return transactionHelper.executeInTransaction(session -> {
            Account account = session.find(Account.class, id);
            return Optional.ofNullable(account);
        });
    }

    public List<User> getUsersAccount(Integer userId){
        return transactionHelper.executeInTransaction(session -> {
            List<User> usersListById = session.createQuery("SELECT u FROM Users u where u.id = :id",
                            User.class)
                    .setParameter("id", userId)
                    .getResultList();
            if(!usersListById.isEmpty()) throw new IllegalArgumentException("Error try another one!");

            return usersListById;
        });
    }

    public void withdraw(Integer fromAccountID, Integer amount){
        validatePositiveAccountId(fromAccountID, "account id");
        validatePositiveAmount(amount);

        transactionHelper.executeIntTransaction(session -> {
            Account account = findAccountById(fromAccountID)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: %s"
                            .formatted(fromAccountID)));

            if(amount > account.getMoneyAmount()) throw new IllegalArgumentException("Error! " +
                    "insufficient funds in the account id=%s, amount of money =%s, withdrawal attempt =%s"
                            .formatted(fromAccountID, account.getMoneyAmount(), amount));
            account.setMoneyAmount(account.getMoneyAmount() - amount);
        });

    }

    public void deposit(Integer toAccountId, Integer amount){
        validatePositiveAccountId(toAccountId, "account id");
        validatePositiveAmount(amount);
        transactionHelper.executeIntTransaction(session -> {
            Account account = session.find(Account.class, toAccountId);
            if(account == null) throw new IllegalArgumentException("Error! No such element " +
                    "with id: %s".formatted(toAccountId));

            account.setMoneyAmount(account.getMoneyAmount() + amount);
        });

    }


    public Account closeAccount(Integer accountId){
        validatePositiveAccountId(accountId, "account id");
        return transactionHelper.executeInTransaction(session -> {
            Account accountToClose = session.find(Account.class, accountId);
            if(accountToClose == null) throw new IllegalArgumentException("Error! No such account!");

            User user = accountToClose.getUser();
            List<Account> usersAccount = user.getAccountList();
            if(usersAccount.size() == 1) throw new IllegalArgumentException("Error! Cant close " +
                    "only one account");

            Account targetAccount = null;
            for(Account acc : usersAccount){
                if(acc.getId() != accountId){
                    targetAccount = acc;
                    break;
                }
            }

            var newAmount = targetAccount.getMoneyAmount() + accountToClose.getMoneyAmount();
            targetAccount.setMoneyAmount(newAmount);

            session.remove(accountToClose);

            return accountToClose;
        });

    }

    public void transferMoney(int fromAccountId, int toAccountId, int amount){
        validatePositiveAccountId(fromAccountId, "from - account id");
        validatePositiveAccountId(toAccountId, "to - account id");
        validatePositiveAmount(amount);

        if(fromAccountId == toAccountId) throw new IllegalArgumentException("Error!" +
                "source and target account id should be different!");

        transactionHelper.executeIntTransaction(session -> {
            Account accountFrom = session.find(Account.class, fromAccountId);
            if(accountFrom == null) throw new IllegalArgumentException("No such account!");

            Account accountTo = session.find(Account.class, toAccountId);
            if(accountTo == null) throw new IllegalArgumentException("No such account!");

            if(accountFrom.getMoneyAmount() < amount) throw new IllegalArgumentException(
                    "Error! No money on account! Balance: "
                    + accountFrom.getMoneyAmount());


            accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);

            int amountToTransfer = accountTo.getUser().getId() == accountFrom.getUser().getId() ? amount :
                    (int) Math.round(amount * (1 - accountProperties.getMoneyCommission()));

            accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToTransfer);
        });
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
