package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDaoDB implements AccountDao{

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Account getAccountById(int id) {
        try {
            final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM Account WHERE AccountID = ?";
            Account account = jdbc.queryForObject(SELECT_ACCOUNT_BY_ID, new AccountMapper(), id);
           // account.setUser(getUserForAccount(id));
            account.setPortfolios(getPortfoliosForAccount(id));
            return account;
        } catch (DataAccessException ex) {
            // If no account is found with the given ID, return null
            return null;
        }
    }

    private User getUserForAccount (int id) {
        final String GET_USER_FOR_ACCOUNT = "select u.* from user u inner join account a on u.userID = a.userID where a.AccountID = ?";
        return jdbc.queryForObject(GET_USER_FOR_ACCOUNT, new UserDaoDB.UserMapper(), id);
    }

    // Helper method to get portfolios for an account
    private List<Portfolio> getPortfoliosForAccount(int accountId) {
        final String SELECT_PORTFOLIOS_FOR_ACCOUNT = "SELECT * FROM Portfolio WHERE AccountID = ?";
        return jdbc.query(SELECT_PORTFOLIOS_FOR_ACCOUNT, new PortfolioDaoDB.PortfolioMapper(), accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        final String SELECT_ALL_ACCOUNTS = "SELECT * FROM Account";
        List<Account> accounts = jdbc.query(SELECT_ALL_ACCOUNTS, new AccountMapper());

        for (Account account : accounts) {
            account.setPortfolios(getPortfoliosForAccount(account.getAccountID()));
        }

        return accounts;
    }


    @Override
    public Account addAccount(Account account) {
        final String INSERT_ACCOUNT = "INSERT INTO Account (AccountName, AccountType, UserID) VALUES (?, ?, ?)";

        try {
            jdbc.update(INSERT_ACCOUNT, account.getAccountName(), account.getAccountType(), account.getUserID());

            // Retrieve the generated AccountID
            int accountId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            account.setAccountID(accountId);
            insertAccountPortfolios(account);

            return account;

        } catch (DataAccessException ex) {
            return null;
        }
    }


    // Helper method to insert portfolios for an account
    private void insertAccountPortfolios(Account account) {
        final String INSERT_ACCOUNT_PORTFOLIOS = "INSERT INTO Portfolio (PortfolioName, AccountID) VALUES (?, ?)";

        if (account != null && account.getPortfolios() != null) {
            for (Portfolio portfolio : account.getPortfolios()) {
                jdbc.update(INSERT_ACCOUNT_PORTFOLIOS, portfolio.getPortfolioName(), account.getAccountID());
            }
        }
    }

    @Override
    public void updateAccount(Account account) {
        final String UPDATE_ACCOUNT = "UPDATE Account SET AccountName = ?, AccountType = ? WHERE AccountID = ?";
        jdbc.update(UPDATE_ACCOUNT, account.getAccountName(), account.getAccountType(), account.getAccountID());

        // Update portfolios for the account
        updatePortfoliosForAccount(account);
    }

    // Helper method to update portfolios for an account
    private void updatePortfoliosForAccount(Account account) {
        final String UPDATE_ACCOUNT_PORTFOLIOS = "UPDATE Portfolio SET PortfolioName = ? WHERE PortfolioID = ? AND AccountID = ?";

        if (account != null && account.getPortfolios() != null) {
            for (Portfolio portfolio : account.getPortfolios()) {
                jdbc.update(UPDATE_ACCOUNT_PORTFOLIOS, portfolio.getPortfolioName(), portfolio.getPortfolioID(), account.getAccountID());
            }
        }

    }

    @Override
    public void deleteAccountById(int id) {
        // Delete account's portfolios from the Portfolio table first
        final String DELETE_ACCOUNT_PORTFOLIOS = "DELETE FROM Portfolio WHERE AccountID = ?";
        jdbc.update(DELETE_ACCOUNT_PORTFOLIOS, id);

        // Delete the account from the Account table
        final String DELETE_ACCOUNT = "DELETE FROM Account WHERE AccountID = ?";
        jdbc.update(DELETE_ACCOUNT, id);
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) {
        try {
            final String SELECT_ACCOUNTS_BY_USERID = "SELECT * FROM Account WHERE UserID = ?";
            List<Account> accounts = jdbc.query(SELECT_ACCOUNTS_BY_USERID, new AccountMapper(), userId);

            for (Account account : accounts) {
                account.setPortfolios(getPortfoliosForAccount(account.getAccountID()));
            }

            return accounts;
        } catch (DataAccessException ex) {
            // If no accounts are found with the given user ID, return null
            return null;
        }
    }


    public static final class AccountMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet rs, int index) throws SQLException {
            Account account = new Account();
            account.setAccountID(rs.getInt("AccountID"));
            account.setAccountName(rs.getString("AccountName"));
            account.setAccountType(rs.getString("AccountType"));
            account.setUserID(rs.getInt("UserID"));
            return account;
        }
    }
}
