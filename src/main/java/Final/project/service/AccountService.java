package Final.project.service;

import Final.project.entities.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(int id);
    List<Account> getAllAccounts();
    Account addAccount(Account account);
    void updateAccount(Account account);
    void deleteAccountById(int id);

    List<Account> getAccountsByUserId(int userId);
}
