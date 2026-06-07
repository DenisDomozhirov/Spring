package work.dir.User;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import work.dir.Account.Account;
import work.dir.Account.AccountProperties;
import work.dir.Account.AccountService;
import work.dir.TransactionHelperClass;

import java.util.*;

@Service
public class UserService {

    private int idCounter;
    private final SessionFactory sessionFactory;
    private final TransactionHelperClass transactionHelper;
    private final AccountService accountService;
    private final AccountProperties accountProperties;


    public UserService(
            AccountService accountService,
            SessionFactory sessionFactory,
            TransactionHelperClass transactionHelper,
            AccountProperties accountProperties
    ){
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
        this.accountProperties = accountProperties;
        this.accountService = accountService;
    }

    public User createUser(String login){

        String normalLogin = validateLogin(login);

        return transactionHelper.executeInTransaction(session -> {
            List<User> findUsersList = session.createQuery("SELECT u FROM User u where u.login = :login",
                    User.class)
                    .setParameter("login", normalLogin)
                    .getResultList();
            if(!findUsersList.isEmpty()) throw new IllegalArgumentException("Error! Write some login for user");
            User user = new User(normalLogin, new ArrayList<>());
            var defaultAccount = new Account(accountProperties.getDefaultAmount(), user);
            user.getAccountList().add(defaultAccount);

            session.persist(user);
            session.persist(defaultAccount);

            return user;
        });
    }

    public User userFindById(Integer id){
        if(id == null || id <= 0) throw new IllegalArgumentException("UserId should be > 0");
        return transactionHelper.executeInTransaction(session -> {
            User user = session.find(User.class, id);
            if(user == null) throw new IllegalArgumentException("User with id: " + id + "not found!");
            return user;
        });
    }

    public List<User> findAll(){
        return transactionHelper.executeInTransaction(session -> {
            return session.createQuery(" FROM User u", User.class).list();
        });
    }

    public String validateLogin(String login){
        if(login == null || login.isBlank()){
            throw new IllegalArgumentException("Error! Login must not be blank!");
        }
        return login;
    }
}
