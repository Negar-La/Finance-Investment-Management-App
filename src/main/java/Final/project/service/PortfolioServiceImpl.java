package Final.project.service;

import Final.project.dao.PortfolioDao;
import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDao portfolioDao;
    @Override
    public Portfolio getPortfolioById(int id) {
        return portfolioDao.getPortfolioById(id);
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return portfolioDao.getAllPortfolios();
    }

    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        return portfolioDao.addPortfolio(portfolio);
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        portfolioDao.updatePortfolio(portfolio);
    }

    @Override
    public void deletePortfolioById(int id) {
        portfolioDao.deletePortfolioById(id);
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(int userId) {
        return portfolioDao.getPortfoliosByUserId(userId);
    }

    @Override
    public List<Asset> getAssetsForPortfolio(int portfolioId) {
        return portfolioDao.getAssetsForPortfolio(portfolioId);
    }
}
