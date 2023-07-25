package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.Portfolio;
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

    @Autowired
    PortfolioDao portfolioDao;

    private User testUser;
    private List<Account> testAccounts;

    @BeforeEach
    public void setUp() {
        // Clear all existing users from the database
        List<User> users = userDao.getAllUsers();
        users.forEach(user -> {
            userDao.deleteUserById(user.getUserID());
        });
        // Clear all existing accounts from the database
        List<Account> accounts = accountDao.getAllAccounts();
        accounts.forEach(account -> {
            accountDao.deleteAccountById(account.getAccountID());
        });

        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        portfolios.forEach(portfolio -> {
            portfolioDao.deletePortfolioById(portfolio.getPortfolioID());
        });
    }

    @Test
    public void testAddAndGetAccountByIdWithoutPortfolios() {
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
    public void testAddAndGetAccountById() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("Sara");
        testUser.setLastName("Faith");
        testUser.setEmail("sara@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create test accounts associated with the test user
        testAccounts = new ArrayList<>();

        // Account 1
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        account1 = accountDao.addAccount(account1);
        testAccounts.add(account1);

        // Create test portfolios and add them to the list for Account 1
        List<Portfolio> portfoliosForAccount1 = new ArrayList<>();
        Portfolio portfolio1ForAccount1 = new Portfolio();
        portfolio1ForAccount1.setPortfolioName("Portfolio 1 for Account 1");
        portfolio1ForAccount1.setAccountID(account1.getAccountID());
        portfolio1ForAccount1 = portfolioDao.addPortfolio(portfolio1ForAccount1);
        portfoliosForAccount1.add(portfolio1ForAccount1);

        Portfolio portfolio2ForAccount1 = new Portfolio();
        portfolio2ForAccount1.setPortfolioName("Portfolio 2 for Account 1");
        portfolio2ForAccount1.setAccountID(account1.getAccountID());
        portfolio2ForAccount1 = portfolioDao.addPortfolio(portfolio2ForAccount1);
        portfoliosForAccount1.add(portfolio2ForAccount1);

        account1.setPortfolios(portfoliosForAccount1);

        // Account 2
        Account account2 = new Account();
        account2.setAccountName("Investment Account");
        account2.setAccountType("Investment");
        account2.setUserID(testUser.getUserID());
        account2 = accountDao.addAccount(account2);
        testAccounts.add(account2);

        // Create test portfolios and add them to the list for Account 2
        List<Portfolio> portfoliosForAccount2 = new ArrayList<>();
        Portfolio portfolio1ForAccount2 = new Portfolio();
        portfolio1ForAccount2.setPortfolioName("Portfolio 1 for Account 2");
        portfolio1ForAccount2.setAccountID(account2.getAccountID());
        portfolio1ForAccount2 = portfolioDao.addPortfolio(portfolio1ForAccount2);
        portfoliosForAccount2.add(portfolio1ForAccount2);

        Portfolio portfolio2ForAccount2 = new Portfolio();
        portfolio2ForAccount2.setPortfolioName("Portfolio 2 for Account 2");
        portfolio2ForAccount2.setAccountID(account2.getAccountID());
        portfolio2ForAccount2 = portfolioDao.addPortfolio(portfolio2ForAccount2);
        portfoliosForAccount2.add(portfolio2ForAccount2);

        account2.setPortfolios(portfoliosForAccount2);

        // Retrieve accounts by ID and check if they match the test accounts
        for (Account testAccount : testAccounts) {
            Account retrievedAccount = accountDao.getAccountById(testAccount.getAccountID());
            assertNotNull(retrievedAccount);
            assertEquals(testAccount, retrievedAccount);
            assertEquals(testAccount.getPortfolios(), retrievedAccount.getPortfolios());
        }
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
    public void testDeleteAccountByIdWithPortfolio() {
        // Create a test user
        testUser = new User();
        testUser.setFirstName("Sara");
        testUser.setLastName("Faith");
        testUser.setEmail("sara@example.com");
        testUser.setPhone("111-111-2233");
        testUser = userDao.addUser(testUser);

        // Create a test account associated with the test user
        Account testAccount = new Account();
        testAccount.setAccountName("Savings Account");
        testAccount.setAccountType("Savings");
        testAccount.setUserID(testUser.getUserID());
        testAccount = accountDao.addAccount(testAccount);

        // Create a portfolio for the test account
        List<Portfolio> portfoliosForTestAccount = new ArrayList<>();
        Portfolio portfolioForTestAccount = new Portfolio();
        portfolioForTestAccount.setPortfolioName("Portfolio for Test Account");
        portfolioForTestAccount.setAccountID(testAccount.getAccountID());
        portfolioForTestAccount = portfolioDao.addPortfolio(portfolioForTestAccount);
        portfoliosForTestAccount.add(portfolioForTestAccount);

        testAccount.setPortfolios(portfoliosForTestAccount);

        // Delete the test account by ID
        accountDao.deleteAccountById(testAccount.getAccountID());

        // Try to retrieve the deleted account by ID and check if it's null
        Account retrievedAccount = accountDao.getAccountById(testAccount.getAccountID());
        assertNull(retrievedAccount);
    }


}