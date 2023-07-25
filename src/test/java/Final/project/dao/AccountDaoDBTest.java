package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountDaoDBTest {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;
    private User testUser;
    private List<Account> testAccounts;

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
    public void testAddAndGetAccountById() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create test accounts
        testAccounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        testAccounts.add(account1);
        Account addedAccount1 = accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(testUser.getUserID());
        testAccounts.add(account2);
        Account addedAccount2 = accountDao.addAccount(account2);

        // Retrieve the accounts by their IDs
        Account retrievedAccount1 = accountDao.getAccountById(addedAccount1.getAccountID());
        Account retrievedAccount2 = accountDao.getAccountById(addedAccount2.getAccountID());

        // Check if the retrieved accounts are not null and match the test accounts
        assertNotNull(retrievedAccount1);
        assertNotNull(retrievedAccount2);
        assertEquals(addedAccount1, retrievedAccount1);
        assertEquals(addedAccount2, retrievedAccount2);

        // Check if the accounts are associated with the correct user
        assertEquals(testUser.getUserID(), retrievedAccount1.getUserID());
        assertEquals(testUser.getUserID(), retrievedAccount2.getUserID());
    }

    @Test
    public void testGetAllAccounts() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create test accounts
        testAccounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        testAccounts.add(account1);
        accountDao.addAccount(account1);

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(testUser.getUserID());
        testAccounts.add(account2);
        accountDao.addAccount(account2);

        // Retrieve all accounts from the database
        List<Account> retrievedAccounts = accountDao.getAllAccounts();

        // Check if the retrieved accounts are not null and match the test accounts
        assertNotNull(retrievedAccounts);
        assertEquals(testAccounts.size(), retrievedAccounts.size());
        assertTrue(retrievedAccounts.containsAll(testAccounts));
    }

    @Test
    public void testUpdateAccount() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create a test account
        Account account = new Account();
        account.setAccountName("Savings Account");
        account.setAccountType("Savings");
        account.setUserID(testUser.getUserID());
        account = accountDao.addAccount(account);

        // Update the account's details
        account.setAccountName("Updated Savings Account");
        account.setAccountType("Updated Savings");

        // Update the account in the database
        accountDao.updateAccount(account);

        // Retrieve the updated account from the database
        Account retrievedAccount = accountDao.getAccountById(account.getAccountID());

        // Check if the retrieved account is not null and matches the updated account
        assertNotNull(retrievedAccount);
        assertEquals(account, retrievedAccount);
    }

    @Test
    public void testDeleteAccountById() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create a test account
        Account account = new Account();
        account.setAccountName("Savings Account");
        account.setAccountType("Savings");
        account.setUserID(testUser.getUserID());
        account = accountDao.addAccount(account);

        // Delete the account from the database
        accountDao.deleteAccountById(account.getAccountID());

        // Try to retrieve the deleted account from the database
        Account retrievedAccount = accountDao.getAccountById(account.getAccountID());

        // Check if the retrieved account is null after deletion
        assertNull(retrievedAccount);
    }

    @Test
    public void testGetAccountsByUserId() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("Sara");
        testUser.setLastName("Faith");
        testUser.setEmail("john@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create test accounts associated with the test user
        testAccounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        testAccounts.add(accountDao.addAccount(account1));

        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(testUser.getUserID());
        testAccounts.add(accountDao.addAccount(account2));

        // Create another test account not associated with the test user
        Account account3 = new Account();
        account3.setAccountName("Personal Account");
        account3.setAccountType("Personal");
        account3.setUserID(-1); // Not associated with any user
        accountDao.addAccount(account3);

        // Retrieve accounts associated with the test user using the method under test
        List<Account> retrievedAccounts = accountDao.getAccountsByUserId(testUser.getUserID());

        // Check if the retrieved accounts are not null and match the test accounts
        assertNotNull(retrievedAccounts);
        assertEquals(testAccounts.size(), retrievedAccounts.size());
        assertTrue(retrievedAccounts.containsAll(testAccounts));
    }
}