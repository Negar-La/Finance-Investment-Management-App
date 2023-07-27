package Final.project.service;

import Final.project.dao.AccountDao;
import Final.project.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Override
    public Account getAccountById(int id) {
        return accountDao.getAccountById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public Account addAccount(Account account) {
        return accountDao.addAccount(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    @Override
    public void deleteAccountById(int id) {
        accountDao.deleteAccountById(id);
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) {
        return accountDao.getAccountsByUserId(userId);
    }
}
