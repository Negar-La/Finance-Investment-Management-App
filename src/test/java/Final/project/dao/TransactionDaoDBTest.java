package Final.project.dao;

import Final.project.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TransactionDaoDBTest {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    AssetDao assetDao;

    @Autowired
    TransactionDao transactionDao;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Clear all existing transactions from the database
        List<Transaction> transactions = transactionDao.getAllTransactions();
        transactions.forEach(transaction -> {
            transactionDao.deleteTransactionById(transaction.getTransactionID());
        });

        // Clear all existing portfolios from the database
        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        portfolios.forEach(portfolio -> {
            portfolioDao.deletePortfolioById(portfolio.getPortfolioID());
        });

        // Clear all existing accounts from the database
        List<Account> accounts = accountDao.getAllAccounts();
        accounts.forEach(account -> {
            accountDao.deleteAccountById(account.getAccountID());
        });

        // Clear all existing assets from the database
        List<Asset> assets = assetDao.getAllAssets();
        assets.forEach(asset -> {
            assetDao.deleteAssetById(asset.getAssetID());
        });

        // Clear all existing users from the database
        List<User> users = userDao.getAllUsers();
        users.forEach(user -> {
            userDao.deleteUserById(user.getUserID());
        });
    }

    @Test
    public void testAddAndGetTransactionById() {
        // Create a user
        User testUser = new User();
        testUser.setFirstName("Neg");
        testUser.setLastName("La");
        testUser.setEmail("negar@example.com");
        testUser.setPhone("111-000-2200");
        testUser = userDao.addUser(testUser);

        // Create an account associated with the user
        Account account1 = new Account();
        account1.setAccountName("Savings Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        account1 = accountDao.addAccount(account1);

        // Create an asset associated with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);


        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("John's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create a transaction associated with the portfolio and asset
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.of(2023, 7, 21, 0, 0));
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setTransactionType("buy");
        transaction.setDescription("Buying Asset 1");

        // Set the portfolio and asset objects
        transaction.setPortfolio(testPortfolio);
       transaction.setAsset(asset1);

        // Add the transaction to the database
        transaction = transactionDao.addTransaction(transaction);

        // Get the transaction by its ID
        Transaction retrievedTransaction = transactionDao.getTransactionById(transaction.getTransactionID());

        // Assert that the retrieved transaction is not null and matches the original transaction
        assertNotNull(retrievedTransaction);

        // Print details of the expected transaction
        System.out.println("Expected Transaction:");
        System.out.println(transaction);

        // Print details of the actual transaction
        System.out.println("Actual Transaction:");
        System.out.println(retrievedTransaction);

        assertEquals(transaction, retrievedTransaction);
    }

}