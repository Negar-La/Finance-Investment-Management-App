package Final.project.dao;

import Final.project.entities.Account;
import Final.project.entities.Asset;
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
class AssetDaoDBTest {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    AssetDao assetDao;

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
    public void testAddAndGetAssetById() {
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

        // Add the assets to the portfolio
        testPortfolio.setAssets(List.of(asset1, asset2));
        portfolioDao.updatePortfolio(testPortfolio);

        // Retrieve the first asset from the database using its ID
        Asset retrievedAsset1 = assetDao.getAssetById(asset1.getAssetID());

        // Perform assertions for asset1
        assertNotNull(retrievedAsset1);
        assertEquals(asset1.getAssetID(), retrievedAsset1.getAssetID());
        assertEquals(asset1.getAssetName(), retrievedAsset1.getAssetName());
        assertEquals(asset1.getAssetType(), retrievedAsset1.getAssetType());
        assertEquals(asset1, retrievedAsset1);

        // Retrieve the second asset from the database using its ID
        Asset retrievedAsset2 = assetDao.getAssetById(asset2.getAssetID());

        // Perform assertions for asset2
        assertNotNull(retrievedAsset2);
        assertEquals(asset2.getAssetID(), retrievedAsset2.getAssetID());
        assertEquals(asset2.getAssetName(), retrievedAsset2.getAssetName());
        assertEquals(asset2.getAssetType(), retrievedAsset2.getAssetType());
        assertEquals(asset2, retrievedAsset2);
    }

    @Test
    public void testGetAllAssets() {
        // Create assets
        Asset asset1 = new Asset();
        asset1.setAssetName("Asset 1");
        asset1.setAssetType("Type 1");
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2 = assetDao.addAsset(asset2);

        // Retrieve all assets from the database
        List<Asset> allAssets = assetDao.getAllAssets();

        // Perform assertions
        assertEquals(2, allAssets.size()); // There should be two assets in the database
        assertTrue(allAssets.contains(asset1));
        assertTrue(allAssets.contains(asset2));
    }

    @Test
    public void testUpdateAsset() {
        // Create an asset
        Asset asset = new Asset();
        asset.setAssetName("Asset 1");
        asset.setAssetType("Type 1");
        asset = assetDao.addAsset(asset);

        // Modify the asset
        asset.setAssetName("Updated Asset");
        asset.setAssetType("Updated Type");
        assetDao.updateAsset(asset);

        // Retrieve the updated asset from the database
        Asset updatedAsset = assetDao.getAssetById(asset.getAssetID());

        // Perform assertions
        assertEquals("Updated Asset", updatedAsset.getAssetName());
        assertEquals("Updated Type", updatedAsset.getAssetType());
    }

    @Test
    public void testDeleteAssetById() {
        // Create a user
        User testUser = new User();
        testUser.setFirstName("David");
        testUser.setLastName("La");
        testUser.setEmail("david@example.com");
        testUser.setPhone("111-222-3333");
        testUser = userDao.addUser(testUser);

        // Create an account associated with the user
        Account account1 = new Account();
        account1.setAccountName("Checking Account");
        account1.setAccountType("Savings");
        account1.setUserID(testUser.getUserID());
        account1 = accountDao.addAccount(account1);

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("David's Portfolio");
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

        // Delete the first asset by ID
        assetDao.deleteAssetById(asset1.getAssetID());

        // Try to retrieve the deleted asset from the database
        Asset deletedAsset = assetDao.getAssetById(asset1.getAssetID());

        // Perform assertion
        assertNull(deletedAsset); // The first asset should not exist in the database after deletion

        // Ensure the second asset still exists in the database
        Asset retrievedAsset2 = assetDao.getAssetById(asset2.getAssetID());
        assertEquals(asset2, retrievedAsset2);
    }

    @Test
    public void testGetAssetsByPortfolioId() {
        // Create a user
        User testUser = new User();
        testUser.setFirstName("David");
        testUser.setLastName("David");
        testUser.setEmail("david@example.com");
        testUser.setPhone("111-222-3333");
        testUser = userDao.addUser(testUser);

        // Create an account associated with the user
        Account account1 = new Account();
        account1.setAccountName("Checking Account");
        account1.setAccountType("Checking");
        account1.setUserID(testUser.getUserID());
        account1 = accountDao.addAccount(account1);

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

        // Create a portfolio associated with the account
        Portfolio testPortfolio = new Portfolio();
        testPortfolio.setPortfolioName("David's Portfolio");
        testPortfolio.setAccountID(account1.getAccountID());
        testPortfolio.setAssets(assets);
        testPortfolio = portfolioDao.addPortfolio(testPortfolio);


        // Retrieve assets associated with the portfolio
        List<Asset> assetsByPortfolioId = assetDao.getAssetsByPortfolioId(testPortfolio.getPortfolioID());

        // Perform assertions
        assertEquals(2, assetsByPortfolioId.size()); // There should be two assets associated with the portfolio
        assertTrue(assetsByPortfolioId.contains(asset1));
        assertTrue(assetsByPortfolioId.contains(asset2));
    }

}