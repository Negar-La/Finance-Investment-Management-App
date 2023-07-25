package Final.project.dao;

import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
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
public class PortfolioDaoDB implements PortfolioDao{

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Portfolio getPortfolioById(int id) {
        try {
            final String SELECT_PORTFOLIO_BY_ID = "SELECT * FROM Portfolio WHERE PortfolioID = ?";
            Portfolio portfolio = jdbc.queryForObject(SELECT_PORTFOLIO_BY_ID, new PortfolioMapper(), id);
            portfolio.setAssets(getAssetsForPortfolio(id));
            return portfolio;
        } catch (DataAccessException ex) {
            // If no portfolio is found with the given ID, return null
            return null;
        }
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        final String SELECT_ALL_PORTFOLIOS = "SELECT * FROM Portfolio";
        List<Portfolio> portfolios = jdbc.query(SELECT_ALL_PORTFOLIOS, new PortfolioMapper());

        for (Portfolio portfolio : portfolios) {
            portfolio.setAssets(getAssetsForPortfolio(portfolio.getPortfolioID()));
        }

        return portfolios;
    }
    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        final String INSERT_PORTFOLIO = "INSERT INTO Portfolio (PortfolioName, AccountID) VALUES (?, ?)";

        try {
            jdbc.update(INSERT_PORTFOLIO, portfolio.getPortfolioName(), portfolio.getAccountID());

            // Retrieve the generated PortfolioID
            int portfolioId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            portfolio.setPortfolioID(portfolioId);

            // Insert assets for the portfolio into the Portfolio_Asset table
            insertPortfolioAssets(portfolio);

            return portfolio;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    // Helper method to insert assets for a portfolio
    private void insertPortfolioAssets(Portfolio portfolio) {
        final String INSERT_PORTFOLIO_ASSETS = "INSERT INTO Portfolio_Asset (PortfolioID, AssetID, Quantity) VALUES (?, ?, ?)";

        if (portfolio != null && portfolio.getAssets() != null) {
            for (Asset asset : portfolio.getAssets()) {
                jdbc.update(INSERT_PORTFOLIO_ASSETS, portfolio.getPortfolioID(), asset.getAssetID(), asset.getQuantity());
            }
        }
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        final String UPDATE_PORTFOLIO = "UPDATE Portfolio SET PortfolioName = ?, AccountID = ? WHERE PortfolioID = ?";
        jdbc.update(UPDATE_PORTFOLIO, portfolio.getPortfolioName(), portfolio.getAccountID(), portfolio.getPortfolioID());

        // Update assets for the portfolio
        updateAssetsForPortfolio(portfolio);
    }

    // Helper method to update assets for a portfolio
    private void updateAssetsForPortfolio(Portfolio portfolio) {
        // First, delete all existing portfolio assets
        final String DELETE_PORTFOLIO_ASSETS = "DELETE FROM Portfolio_Asset WHERE PortfolioID = ?";
        jdbc.update(DELETE_PORTFOLIO_ASSETS, portfolio.getPortfolioID());

        // Then, insert the updated assets
        insertPortfolioAssets(portfolio);
    }

    @Override
    public void deletePortfolioById(int id) {
        // Delete the portfolio's assets from the Portfolio_Asset table first
        final String DELETE_PORTFOLIO_ASSETS = "DELETE FROM Portfolio_Asset WHERE PortfolioID = ?";
        jdbc.update(DELETE_PORTFOLIO_ASSETS, id);

        // Delete the portfolio from the Portfolio table
        final String DELETE_PORTFOLIO = "DELETE FROM Portfolio WHERE PortfolioID = ?";
        jdbc.update(DELETE_PORTFOLIO, id);
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(int userId) {
        try {
            final String SELECT_PORTFOLIOS_BY_USERID = "SELECT * FROM Portfolio JOIN Account ON Portfolio.AccountID = Account.AccountID WHERE Account.UserID = ?";
            List<Portfolio> portfolios = jdbc.query(SELECT_PORTFOLIOS_BY_USERID, new PortfolioMapper(), userId);

            for (Portfolio portfolio : portfolios) {
                portfolio.setAssets(getAssetsForPortfolio(portfolio.getPortfolioID()));
            }

            return portfolios;
        } catch (DataAccessException ex) {
            // If no portfolios are found with the given user ID, return an empty list
            return new ArrayList<>();
        }
    }

    @Override
    public List<Asset> getAssetsForPortfolio(int portfolioId) {
        final String SELECT_ASSETS_FOR_PORTFOLIO = "SELECT Asset.*, Portfolio_Asset.Quantity " +
                "FROM Asset " +
                "JOIN Portfolio_Asset ON Asset.AssetID = Portfolio_Asset.AssetID " +
                "WHERE Portfolio_Asset.PortfolioID = ?";
        return jdbc.query(SELECT_ASSETS_FOR_PORTFOLIO, new AssetDaoDB.AssetMapper(), portfolioId);
    }


    public static final class PortfolioMapper implements RowMapper<Portfolio> {

        @Override
        public Portfolio mapRow(ResultSet rs, int index) throws SQLException {
            Portfolio portfolio = new Portfolio();
            portfolio.setPortfolioID(rs.getInt("PortfolioID"));
            portfolio.setPortfolioName(rs.getString("PortfolioName"));
            portfolio.setAccountID(rs.getInt("AccountID"));
            return portfolio;
        }
    }
}
