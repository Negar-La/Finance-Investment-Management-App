package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.User;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDaoDBTest {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @BeforeEach
    public void setUp() {
        // Clear all existing users from the database
        List<User> users = userDao.getAllUsers();
        users.forEach(user -> {
            userDao.deleteUserById(user.getUserID());
        });

        List<Account> accounts = accountDao.getAllAccounts();
        accounts.forEach(account -> {
            accountDao.deleteAccountById(account.getAccountID());
        });
    }

    @Test
    public void testAddUserWithoutAccounts() {
        // Create a test user
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");


        // Initialize the accounts property to an empty list
       // testUser.setAccounts(new ArrayList<>());

        // Add the user without any accounts
        User addedUser = userDao.addUser(testUser);

        // Check if the user was successfully added to the database
        assertNotNull(addedUser);
        assertNotNull(addedUser.getUserID());



        // Check if there are no associated accounts for the user (should be an empty list)
        List<Account> accountsForUser = userDao.getAccountsForUser(addedUser.getUserID());
        assertNull(accountsForUser);
    }


    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john@example.com");
        user1.setPhone("111-111-2233");
        users.add(userDao.addUser(user1));

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(user1.getUserID());
        accounts.add(account1);
        account1 = accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(user1.getUserID());
        accounts.add(account2);
        account2 = accountDao.addAccount(account2);

        user1.setAccounts(accounts);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane@example.com");
        user2.setPhone("222-222-3344");
        users.add(userDao.addUser(user2));

        List<Account> accounts2 = new ArrayList<>();

        Account account3 = new Account();
        account3.setAccountName("Investment Account");
        account3.setAccountType("Investment");
        account3.setUserID(user2.getUserID());
        accounts2.add(account3);
        account3 = accountDao.addAccount(account3);

        user2.setAccounts(accounts2);

        List<User> retrievedUsers = userDao.getAllUsers();
        assertNotNull(retrievedUsers);
        assertEquals(users.size(), retrievedUsers.size());
        assertEquals(retrievedUsers, users);
        assertTrue(retrievedUsers.contains(user1));
    }

    @Test
    public void testAddAndGetAndUpdateUserById() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPhone("111-111-2233");
        user = userDao.addUser(user);

        // Creating accounts for the user
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(user.getUserID());
        accounts.add(account1);
        account1 = accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(user.getUserID());
        accounts.add(account2);
        account2 = accountDao.addAccount(account2);

        user.setAccounts(accounts);

        // Update the user along with the associated accounts
      userDao.updateUser(user);

        // Retrieve the user by ID, it should have the accounts associated
        User fromDao = userDao.getUserById(user.getUserID());
        assertNotNull(fromDao);
        assertEquals(user, fromDao);
        assertEquals(accounts.size(), fromDao.getAccounts().size());

        // Verify that the accounts are also correctly associated with the user
        for (Account account : fromDao.getAccounts()) {
            assertEquals(user.getUserID(), account.getUserID());
        }
    }

    @Test
    public void testUpdateUserName() {
        // Create a test user
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");

        // Add the user to the database
        User addedUser = userDao.addUser(testUser);

        // Modify the user's name
        addedUser.setFirstName("Jane");
        addedUser.setLastName("Smith");

        // Update the user in the database
        userDao.updateUser(addedUser);

        // Retrieve the updated user from the database
        User updatedUser = userDao.getUserById(addedUser.getUserID());

        // Assert that the user's name has been updated in the database
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
    }

    @Test
    public void testDeleteUser() {
        // Create a test user
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");

        // Add the user to the database
        User addedUser = userDao.addUser(testUser);

        // Retrieve the user from the database to ensure it was added successfully
        User retrievedUser = userDao.getUserById(addedUser.getUserID());
        assertNotNull(retrievedUser);

        // Delete the user
        userDao.deleteUserById(addedUser.getUserID());

        // Attempt to retrieve the user again
        User deletedUser = userDao.getUserById(addedUser.getUserID());

        // Assert that the user is no longer present in the database (should be null)
        assertNull(deletedUser);
    }

    @Test
    public void testGetAccountsForUser() {
        // Create a test user
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");

        // Add the user to the database
        User addedUser = userDao.addUser(testUser);

        // Create test accounts associated with the user
        List<Account> testAccounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(addedUser.getUserID()); // Set the userID for the account
        testAccounts.add(account1);
        account1 = accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(addedUser.getUserID()); // Set the userID for the account
        testAccounts.add(account2);
        account2 = accountDao.addAccount(account2);

        // Call the method to get accounts for the user
        List<Account> retrievedAccounts = userDao.getAccountsForUser(addedUser.getUserID());

        // Assert that the retrieved accounts are not null and match the test accounts
        assertNotNull(retrievedAccounts);
        assertEquals(testAccounts.size(), retrievedAccounts.size());

        // Assert that the accounts have been correctly associated with the user
        for (Account account : retrievedAccounts) {
            assertEquals(addedUser.getUserID(), account.getUserID());
        }
    }


}
