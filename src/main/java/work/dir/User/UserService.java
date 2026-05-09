package work.dir.User;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import work.dir.Account.AccountService;

import java.util.*;

@Service
public class UserService {

    private int idCounter;
    private final Map<Integer, User> userMap;
    private final Set<String> logins;
    private final AccountService accountService;

    public UserService(AccountService accountService){
        this.idCounter = 0;
        this.userMap = new HashMap<>();
        this.logins = new HashSet<>();
        this.accountService = accountService;
    }

    public User createUser(String login){
        String normalLogin = validateLogin(login);
        if(logins.contains(normalLogin)){
            throw new IllegalArgumentException("This login already exists with login %s"
                    .formatted(normalLogin));
        }
        idCounter++;
        var user = new User(idCounter, normalLogin, new ArrayList<>());
        var defaultAccount = accountService.createAccount(user);
        user.getAccountList().add(defaultAccount);

        userMap.put(idCounter, user);
        logins.add(normalLogin);
        return user;
    }

    public User userFindById(Integer id){
        if(id == null || id <= 0) throw new IllegalArgumentException("UserId should be > 0");
        var user = userMap.get(id);
        if(user == null) throw new IllegalArgumentException("Error! No such element with this id %s"
                .formatted(id));
        return user;
    }

    public List<User> findAll(){
        return userMap.values().stream().toList();
    }

    public String validateLogin(String login){
        if(login == null || login.isBlank()){
            throw new IllegalArgumentException("Error! Login must not be blank!");
        }
        return login;
    }
}
