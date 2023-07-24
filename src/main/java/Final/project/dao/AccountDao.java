package Final.project.dao;

import Final.project.entities.Account;

import java.util.List;

public interface AccountDao {
    Account getAccountById(int id);
    List<Account> getAllAccounts();
    Account addAccount(Account account);
    void updateAccount(Account account);
    void deleteAccountById(int id);

    Account getAccountByUserId(int userId);
}
