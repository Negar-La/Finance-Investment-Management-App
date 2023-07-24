package Final.project.dao;

import Final.project.entities.Asset;

import java.util.List;

public interface AssetDao {
    Asset getAssetById(int id);
    List<Asset> getAllAssets();
    Asset addAsset(Asset asset);
    void updateAsset(Asset asset);
    void deleteAssetById(int id);

    List<Asset> getAssetsByPortfolioId(int portfolioId);

    List<Asset> getAssetsByUserId(int userId);
}