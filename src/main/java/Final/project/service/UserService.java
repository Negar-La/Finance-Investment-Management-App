package Final.project.service;

import Final.project.entities.Account;
import Final.project.entities.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    List<User> getAllUsers();
    User addUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);

    List<Account> getAccountsForUser(int userId);
}
