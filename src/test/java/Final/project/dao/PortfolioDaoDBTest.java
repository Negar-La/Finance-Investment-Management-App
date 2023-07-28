package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
import Final.project.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PortfolioDaoDBTest {
    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    AssetDao assetDao;

    private User testUser;

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

        List<Asset> assets = assetDao.getAllAssets();
        assets.forEach(asset -> {
            assetDao.deleteAssetById(asset.getAssetID());
        });
    }

    @Test
    public void testAddAndGetPortfolioById() {
        // Create a user
        testUser = new User();
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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("John's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create assets and associate them with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset1);
        assets.add(asset2);
        testPortfolio.setAssets(assets);

        // Save the assets in the Portfolio_Asset table
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve the portfolio by ID
        Portfolio retrievedPortfolio = portfolioDao.getPortfolioById(testPortfolio.getPortfolioID());

        // Perform the assertion
        assertEquals(testPortfolio.getAssets().size(), retrievedPortfolio.getAssets().size());
    }

    @Test
    public void testGetAllPortfolios() {
        // Create a user
        testUser = new User();
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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create assets and associate them with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset1);
        assets.add(asset2);
        testPortfolio.setAssets(assets);

        // Save the assets in the Portfolio_Asset table
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve all portfolios
        List<Portfolio> allPortfolios = portfolioDao.getAllPortfolios();

        // Perform the assertions
        assertEquals(1, allPortfolios.size()); // There should be one portfolio in the list
        Portfolio retrievedPortfolio = allPortfolios.get(0);
        assertEquals(testPortfolio.getPortfolioID(), retrievedPortfolio.getPortfolioID());
        assertEquals(testPortfolio.getPortfolioName(), retrievedPortfolio.getPortfolioName());
        assertEquals(testPortfolio.getAccountID(), retrievedPortfolio.getAccountID());

        // Check if the assets in the portfolio match
        assertEquals(testPortfolio.getAssets().size(), retrievedPortfolio.getAssets().size());
        assertTrue(retrievedPortfolio.getAssets().containsAll(testPortfolio.getAssets()));
    }

    @Test
    public void testUpdatePortfolio() {
        // Create a user
        testUser = new User();
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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create assets and associate them with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset1);
        assets.add(asset2);
        testPortfolio.setAssets(assets);

        // Save the assets in the Portfolio_Asset table
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve the portfolio by ID
        Portfolio retrievedPortfolio = portfolioDao.getPortfolioById(testPortfolio.getPortfolioID());

        // Perform the initial assertions before updating
        assertEquals(testPortfolio.getPortfolioID(), retrievedPortfolio.getPortfolioID());
        assertEquals(testPortfolio.getPortfolioName(), retrievedPortfolio.getPortfolioName());
        assertEquals(testPortfolio.getAccountID(), retrievedPortfolio.getAccountID());

        // Check if the assets in the portfolio match
        assertEquals(testPortfolio.getAssets().size(), retrievedPortfolio.getAssets().size());
        assertTrue(retrievedPortfolio.getAssets().containsAll(testPortfolio.getAssets()));

        // Now, update the portfolio's name
        String updatedPortfolioName = "Updated Portfolio Name";
        retrievedPortfolio.setPortfolioName(updatedPortfolioName);

        // Update the portfolio in the database
        portfolioDao.updatePortfolio(retrievedPortfolio);

        // Retrieve the updated portfolio from the database
        Portfolio updatedPortfolio = portfolioDao.getPortfolioById(retrievedPortfolio.getPortfolioID());

        // Perform the assertions after updating
        assertEquals(retrievedPortfolio.getPortfolioID(), updatedPortfolio.getPortfolioID());
        assertEquals(updatedPortfolioName, updatedPortfolio.getPortfolioName());
        assertEquals(retrievedPortfolio.getAccountID(), updatedPortfolio.getAccountID());

        // Check if the assets in the updated portfolio still match
        assertEquals(retrievedPortfolio.getAssets().size(), updatedPortfolio.getAssets().size());
        assertTrue(updatedPortfolio.getAssets().containsAll(retrievedPortfolio.getAssets()));
    }

    @Test
    public void testDeletePortfolioById() {
        // Create a user
        testUser = new User();
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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create assets and associate them with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset1);
        assets.add(asset2);
        testPortfolio.setAssets(assets);

        // Save the assets in the Portfolio_Asset table
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve the portfolio by ID
        Portfolio retrievedPortfolio = portfolioDao.getPortfolioById(testPortfolio.getPortfolioID());

        // Perform the initial assertions before deletion
        assertEquals(testPortfolio.getPortfolioID(), retrievedPortfolio.getPortfolioID());
        assertEquals(testPortfolio.getPortfolioName(), retrievedPortfolio.getPortfolioName());
        assertEquals(testPortfolio.getAccountID(), retrievedPortfolio.getAccountID());

        // Check if the assets in the portfolio match
        assertEquals(testPortfolio.getAssets().size(), retrievedPortfolio.getAssets().size());
        assertTrue(retrievedPortfolio.getAssets().containsAll(testPortfolio.getAssets()));

        // Now, delete the portfolio from the database
        portfolioDao.deletePortfolioById(retrievedPortfolio.getPortfolioID());

        // Attempt to retrieve the deleted portfolio from the database
        Portfolio deletedPortfolio = portfolioDao.getPortfolioById(retrievedPortfolio.getPortfolioID());

        // Perform the assertions after deletion (deletedPortfolio should be null)
        assertNull(deletedPortfolio);
    }

    @Test
    public void testGetPortfoliosByUserId() {
        // Create a user
        testUser = new User();
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

        // Create two portfolios associated with the account
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setPortfolioName("Negar's Portfolio 1");
        portfolio1.setAccountID(account1.getAccountID());
        portfolio1.setAssets(new ArrayList<>());
        portfolio1 = portfolioDao.addPortfolio(portfolio1);

        Portfolio portfolio2 = new Portfolio();
        portfolio2.setPortfolioName("Negar's Portfolio 2");
        portfolio2.setAccountID(account1.getAccountID());
        portfolio2.setAssets(new ArrayList<>());
        portfolio2 = portfolioDao.addPortfolio(portfolio2);

        // Retrieve portfolios associated with the user
        List<Portfolio> portfoliosByUserId = portfolioDao.getPortfoliosByUserId(testUser.getUserID());

        // Perform the assertions
        assertEquals(2, portfoliosByUserId.size()); // There should be two portfolios associated with the user
        assertTrue(portfoliosByUserId.contains(portfolio1));
        assertTrue(portfoliosByUserId.contains(portfolio2));
    }

    @Test
    public void testGetAssetsForPortfolio() {
        // Create a user
        testUser = new User();
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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("Negar's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);

        // Create assets and associate them with the portfolio
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset1);
        assets.add(asset2);
        testPortfolio.setAssets(assets);

        // Save the assets in the Portfolio_Asset table
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve assets associated with the portfolio
        List<Asset> assetsForPortfolio = portfolioDao.getAssetsForPortfolio(testPortfolio.getPortfolioID());

        // Perform the assertions
        assertEquals(2, assetsForPortfolio.size()); // There should be two assets associated with the portfolio
        assertTrue(assetsForPortfolio.contains(asset1));
        assertTrue(assetsForPortfolio.contains(asset2));
    }


}