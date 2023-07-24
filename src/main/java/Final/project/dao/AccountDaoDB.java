package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDaoDB implements AccountDao{
    @Override
    public Account getAccountById(int id) {
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public Account addAccount(Account account) {
        return null;
    }

    @Override
    public void updateAccount(Account account) {

    }

    @Override
    public void deleteAccountById(int id) {

    }

    @Override
    public Account getAccountByUserId(int userId) {
        return null;
    }


    public static final class AccountMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet rs, int index) throws SQLException {
            Account account = new Account();
            account.setAccountID(rs.getInt("AccountID"));
            account.setAccountName(rs.getString("AccountName"));
            account.setAccountType(rs.getString("AccountType"));
            return account;
        }
    }
}
