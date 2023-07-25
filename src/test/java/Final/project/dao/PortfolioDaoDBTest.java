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
    public void testGetPortfolioById() {
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
        asset1.setQuantity(new BigDecimal(11));
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2.setQuantity(new BigDecimal(10));
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
        asset1.setQuantity(new BigDecimal(11));
        asset1 = assetDao.addAsset(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetName("Asset 2");
        asset2.setAssetType("Type 2");
        asset2.setQuantity(new BigDecimal(10));
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

}