package Final.project.dao;

import Final.project.entities.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AssetDaoDB implements AssetDao{
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Asset getAssetById(int id) {
        try {
            final String SELECT_ASSET_BY_ID = "SELECT * FROM Asset WHERE AssetID = ?";
            return jdbc.queryForObject(SELECT_ASSET_BY_ID, new AssetMapper(), id);
        } catch (DataAccessException ex) {
            // If no asset is found with the given ID, return null
            return null;
        }
    }

    @Override
    public List<Asset> getAllAssets() {
        final String SELECT_ALL_ASSETS = "SELECT * FROM Asset";
        return jdbc.query(SELECT_ALL_ASSETS, new AssetMapper());
    }

    @Override
    public Asset addAsset(Asset asset) {
        final String INSERT_ASSET = "INSERT INTO Asset (AssetName, AssetType) VALUES (?, ?)";

        try {
            jdbc.update(INSERT_ASSET, asset.getAssetName(), asset.getAssetType());

            // Retrieve the generated AssetID
            int assetId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            asset.setAssetID(assetId);

            return asset;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateAsset(Asset asset) {
        final String UPDATE_ASSET = "UPDATE Asset SET AssetName = ?, AssetType = ? WHERE AssetID = ?";
        jdbc.update(UPDATE_ASSET, asset.getAssetName(), asset.getAssetType(), asset.getAssetID());
    }

    @Override
    public void deleteAssetById(int id) {
        // Delete the asset from the Portfolio_Asset bridge table first
        final String DELETE_PORTFOLIO_ASSETS = "DELETE FROM Portfolio_Asset WHERE AssetID = ?";
        jdbc.update(DELETE_PORTFOLIO_ASSETS, id);

        // Delete the asset from the Asset table
        final String DELETE_ASSET = "DELETE FROM Asset WHERE AssetID = ?";
        jdbc.update(DELETE_ASSET, id);
    }

    @Override
    public List<Asset> getAssetsByPortfolioId(int portfolioId) {
        final String SELECT_ASSETS_BY_PORTFOLIO_ID = "SELECT a.* FROM Asset a " +
                "JOIN Portfolio_Asset pa ON a.AssetID = pa.AssetID " +
                "WHERE pa.PortfolioID = ?";
        return jdbc.query(SELECT_ASSETS_BY_PORTFOLIO_ID, new AssetMapper(), portfolioId);
    }

    @Override
    public List<Asset> getAssetsByUserId(int userId) {
        final String SELECT_ASSETS_BY_USER_ID = "SELECT a.* FROM Asset a " +
                "JOIN Portfolio_Asset pa ON a.AssetID = pa.AssetID " +
                "JOIN Portfolio p ON pa.PortfolioID = p.PortfolioID " +
                "JOIN Account ac ON p.AccountID = ac.AccountID " +
                "WHERE ac.UserID = ?";
        return jdbc.query(SELECT_ASSETS_BY_USER_ID, new AssetMapper(), userId);
    }

    public static final class AssetMapper implements RowMapper<Asset> {
        @Override
        public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
            Asset asset = new Asset();
            asset.setAssetID(rs.getInt("AssetID"));
            asset.setAssetName(rs.getString("AssetName"));
            asset.setAssetType(rs.getString("AssetType"));
            return asset;
        }
    }
}
