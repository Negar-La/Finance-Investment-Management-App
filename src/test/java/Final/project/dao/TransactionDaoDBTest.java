package Final.project.dao;

import Final.project.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create a transaction associated with the portfolio and asset
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setTransactionType("buy");
        transaction.setDescription("Buying Asset 1");

        // Set the portfolio and asset objects
        transaction.setPortfolio(testPortfolio);

        // Now, let's fetch the corresponding asset from the database and set it to the transaction's asset property
        Asset retrievedAsset = assetDao.getAssetById(asset1.getAssetID());
        transaction.setAsset(retrievedAsset);

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

    @Test
    public void testGetAllTransactions() {
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

        // Create assets associated with the portfolios
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        // Create portfolios associated with the assets
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setPortfolioName("Negar's Portfolio 1");
        portfolio1.setAccountID(account1.getAccountID());
        portfolio1.setAssets(List.of(asset1));
        portfolio1 = portfolioDao.addPortfolio(portfolio1);

        Portfolio portfolio2 = new Portfolio();
        portfolio2.setPortfolioName("Negar's Portfolio 2");
        portfolio2.setAccountID(account1.getAccountID());
        portfolio2.setAssets(List.of(asset2));
        portfolio2 = portfolioDao.addPortfolio(portfolio2);

        // Create transactions associated with the portfolios and assets
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionDate(LocalDate.now());
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setTransactionType("buy");
        transaction1.setDescription("Buying Asset 1");
        transaction1.setPortfolio(portfolio1);
        transaction1.setAsset(asset1);
        transaction1 = transactionDao.addTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionDate(LocalDate.now());
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setTransactionType("sell");
        transaction2.setDescription("Selling Asset 2");
        transaction2.setPortfolio(portfolio2);
        transaction2.setAsset(asset2);
        transaction2 = transactionDao.addTransaction(transaction2);

        // Get all transactions from the database
        List<Transaction> allTransactions = transactionDao.getAllTransactions();

        // Print details of the expected transactions
        System.out.println("Expected Transactions:");
        System.out.println(List.of(transaction1, transaction2));

        // Print details of the actual transactions
        System.out.println("Actual Transactions:");
        System.out.println(allTransactions);

        // Assert that the retrieved transactions are not null and match the expected list
        assertNotNull(allTransactions);
        assertEquals(List.of(transaction1, transaction2), allTransactions);
    }

    @Test
    public void testUpdateTransaction() {
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

        // Create a portfolio associated with the account and asset
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create a transaction associated with the portfolio and asset
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setTransactionType("buy");
        transaction.setDescription("Buying Asset 1");
        transaction.setPortfolio(testPortfolio);
        transaction.setAsset(asset1);

        // Add the transaction to the database
        transaction = transactionDao.addTransaction(transaction);

        // Modify the transaction's data
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setTransactionType("sell");
        transaction.setDescription("Selling Asset 1");

        // Update the transaction in the database
        transactionDao.updateTransaction(transaction);

        // Retrieve the updated transaction from the database
        Transaction retrievedTransaction = transactionDao.getTransactionById(transaction.getTransactionID());

        // Assert that the retrieved transaction is not null and matches the updated transaction
        assertNotNull(retrievedTransaction);
        assertEquals(transaction, retrievedTransaction);
    }

    @Test
    public void testDeleteTransactionById() {
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

        // Create a portfolio associated with the account and asset
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create a transaction associated with the portfolio and asset
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setTransactionType("buy");
        transaction.setDescription("Buying Asset 1");
        transaction.setPortfolio(testPortfolio);
        transaction.setAsset(asset1);

        // Add the transaction to the database
        transaction = transactionDao.addTransaction(transaction);

        // Delete the transaction from the database
        transactionDao.deleteTransactionById(transaction.getTransactionID());

        // Try to retrieve the deleted transaction from the database
        Transaction retrievedTransaction = transactionDao.getTransactionById(transaction.getTransactionID());

        // Assert that the retrieved transaction is null (indicating that it was successfully deleted)
        assertNull(retrievedTransaction);
    }

    @Test
    public void testGetTransactionsByPortfolioId() {
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

        // Create a portfolio associated with the account and asset
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create transactions associated with the portfolio and user
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionDate(LocalDate.now());
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setTransactionType("buy");
        transaction1.setDescription("Buying Asset 1");
        transaction1.setPortfolio(testPortfolio);
        transaction1.setAsset(asset1);
        transaction1 = transactionDao.addTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionDate(LocalDate.now());
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setTransactionType("sell");
        transaction2.setDescription("Selling Asset 1");
        transaction2.setPortfolio(testPortfolio);
        transaction2.setAsset(asset1);
        transaction2 = transactionDao.addTransaction(transaction2);

        // Get transactions by portfolio ID
        List<Transaction> portfolioTransactions = transactionDao.getTransactionsByPortfolioId(testPortfolio.getPortfolioID());

        // Assert that the retrieved transactions are not null and match the expected list
        assertNotNull(portfolioTransactions);
        assertEquals(2, portfolioTransactions.size());

        // Print details of the expected transactions
        System.out.println("Expected Transactions:");
        System.out.println(List.of(transaction1, transaction2));

        // Print details of the actual transactions
        System.out.println("Actual Transactions:");
        System.out.println(portfolioTransactions);

        // Verify if each transaction in the portfolioTransactions list belongs to the correct portfolio
        for (Transaction transaction : portfolioTransactions) {
            assertEquals(testPortfolio.getPortfolioID(), transaction.getPortfolio().getPortfolioID());
        }
    }

    @Test
    public void testGetTransactionsByUserId() {
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

        // Create a portfolio associated with the account and asset
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(List.of(asset1));
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create transactions associated with the portfolio and user
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionDate(LocalDate.now());
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setTransactionType("buy");
        transaction1.setDescription("Buying Asset 1");
        transaction1.setPortfolio(testPortfolio);
        transaction1.setAsset(asset1);
        transaction1 = transactionDao.addTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionDate(LocalDate.now());
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setTransactionType("sell");
        transaction2.setDescription("Selling Asset 1");
        transaction2.setPortfolio(testPortfolio);
        transaction2.setAsset(asset1);
        transaction2 = transactionDao.addTransaction(transaction2);

        // Get transactions by user ID
        List<Transaction> userTransactions = transactionDao.getTransactionsByUserId(testUser.getUserID());

        // Assert that the retrieved transactions are not null and match the expected list
        assertNotNull(userTransactions);
        assertEquals(2, userTransactions.size());

        // Print details of the expected transactions
        System.out.println("Expected Transactions:");
        System.out.println(List.of(transaction1, transaction2));

        // Print details of the actual transactions
        System.out.println("Actual Transactions:");
        System.out.println(userTransactions);

        // Verify if each transaction in the userTransactions list belongs to the correct user
        for (Transaction transaction : userTransactions) {
            Portfolio portfolio = transaction.getPortfolio();
            assertNotNull(portfolio);
            assertEquals(testUser.getUserID(), accountDao.getAccountById(portfolio.getAccountID()).getUserID());
        }
    }



}