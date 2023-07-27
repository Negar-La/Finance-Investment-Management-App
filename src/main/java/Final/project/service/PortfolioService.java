package Final.project.service;

import Final.project.entities.Asset;
import Final.project.entities.Portfolio;

import java.util.List;

public interface PortfolioService {
    Portfolio getPortfolioById(int id);
    List<Portfolio> getAllPortfolios();
    Portfolio addPortfolio(Portfolio portfolio);
    void updatePortfolio(Portfolio portfolio);
    void deletePortfolioById(int id);

    List<Portfolio> getPortfoliosByUserId(int userId);
    List<Asset> getAssetsForPortfolio(int portfolioId);
}
