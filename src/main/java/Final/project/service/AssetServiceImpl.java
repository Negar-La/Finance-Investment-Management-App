package Final.project.service;

import Final.project.dao.AssetDao;
import Final.project.entities.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    AssetDao assetDao;
    @Override
    public Asset getAssetById(int id) {
        return assetDao.getAssetById(id);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetDao.getAllAssets();
    }

    @Override
    public Asset addAsset(Asset asset) {
        return assetDao.addAsset(asset);
    }

    @Override
    public void updateAsset(Asset asset) {
        assetDao.updateAsset(asset);
    }

    @Override
    public void deleteAssetById(int id) {
        assetDao.deleteAssetById(id);
    }

    @Override
    public List<Asset> getAssetsByPortfolioId(int portfolioId) {
        return assetDao.getAssetsByPortfolioId(portfolioId);
    }
}
