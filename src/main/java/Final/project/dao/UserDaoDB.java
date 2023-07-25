package Final.project.dao;

import Final.project.entities.Account;
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
public class UserDaoDB implements UserDao{

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    AccountDao accountDao;
    @Override
    public User getUserById(int id) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE UserID = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
            if (user != null) {
                user.setAccounts(getAccountsForUser(id));
            }
            return user;

        } catch(DataAccessException ex) {
            return null;
        }
    }

    //     create a helper method to get accounts for a user:
    @Override
    public List<Account> getAccountsForUser(int id){
        final String SELECT_ACCOUNTS_FOR_USER = "SELECT * FROM Account WHERE UserID = ?";
        List<Account> accounts = new ArrayList<>();
        try {
            accounts = jdbc.query(SELECT_ACCOUNTS_FOR_USER, new AccountDaoDB.AccountMapper(), id);
        } catch (DataAccessException ex) {
            // Handle the exception appropriately or log it
            ex.printStackTrace();
        }

        return accounts.isEmpty() ? null : accounts;
        //make sure that the getAccountsForUser method returns null if there are no associated accounts for the user
    }

    @Override
    public List<User> getAllUsers() {
        try {
            final String SELECT_ALL_USERS = "SELECT * FROM User";
            List<User> users = jdbc.query(SELECT_ALL_USERS, new UserMapper());

            for (User user : users) {
                user.setAccounts(getAccountsForUser(user.getUserID()));
            }

            return users;
        } catch (DataAccessException ex) {
            return null;
        }
    }


    @Override
    public User addUser(User user) {
        try {
            final String INSERT_USER = "INSERT INTO User (FirstName, LastName, Email, Phone) VALUES (?, ?, ?, ?)";

            jdbc.update(INSERT_USER, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());

            // Retrieve the generated UserID
            int userId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            user.setUserID(userId);

            // Insert user's accounts into the Account table and associate them with the user
            insertUserAccounts(user);

            return user;

        } catch (DataAccessException ex) {
            return null;
        }
    }

    private void insertUserAccounts(User user) {
        final String INSERT_USER_ACCOUNTS = "INSERT INTO Account (AccountName, AccountType, UserID) VALUES (?, ?, ?)";

        if (user != null && user.getAccounts() != null) {
            for (Account account : user.getAccounts()) {
                System.out.println("Inserting account: " + account.getAccountName());
                int rowsAffected = jdbc.update(INSERT_USER_ACCOUNTS, account.getAccountName(), account.getAccountType(), user.getUserID());
                System.out.println(rowsAffected + " row(s) inserted.");
            }
        }
    }



    @Override
    public void updateUser(User user) {
        try {
            final String UPDATE_USER = "UPDATE User SET FirstName = ?, LastName = ?, Email = ?, Phone = ? WHERE UserID = ?";

            jdbc.update(UPDATE_USER, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getUserID());

            // Update user's accounts in the Account table
            updateAccountsForUser(user);

        } catch (DataAccessException ex) {
            // Handle exception if needed
        }
    }

    private void updateAccountsForUser(User user) {
        final String UPDATE_USER_ACCOUNTS = "UPDATE Account SET AccountName = ?, AccountType = ? WHERE AccountID = ? AND UserID = ?";
        if (user != null && user.getAccounts() != null) {
            for (Account account : user.getAccounts()) {
                jdbc.update(UPDATE_USER_ACCOUNTS, account.getAccountName(), account.getAccountType(), account.getAccountID(), user.getUserID());
            }
        }

    }

    @Override
    public void deleteUserById(int id) {
        try {
            // Delete user's accounts from the Account table first
            final String DELETE_USER_ACCOUNTS = "DELETE FROM Account WHERE UserID = ?";
            jdbc.update(DELETE_USER_ACCOUNTS, id);

            // Delete the user from the User table
            final String DELETE_USER = "DELETE FROM User WHERE UserID = ?";
            jdbc.update(DELETE_USER, id);

        } catch (DataAccessException ex) {
            // Handle exception if needed
        }
    }

    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int index) throws SQLException {
            User user = new User();
            user.setUserID(rs.getInt("UserID"));
            user.setFirstName(rs.getString("FirstName"));
            user.setLastName(rs.getString("LastName"));
            user.setEmail(rs.getString("Email"));
            user.setPhone(rs.getString("Phone"));

//            List<Account> accounts = accountDao.getAccountsByUserId(user.getUserID());
//            user.setAccounts(accounts);   ****Don't use it here.
            return user;
        }
    }
}
