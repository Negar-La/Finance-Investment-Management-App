package Final.project.dao;

import Final.project.entities.Portfolio;

import java.util.List;

public interface PortfolioDao {
    Portfolio getPortfolioById(int id);
    List<Portfolio> getAllPortfolios();
    Portfolio addPortfolio(Portfolio portfolio);
    void updatePortfolio(Portfolio portfolio);
    void deletePortfolioById(int id);

    List<Portfolio> getPortfoliosByUserId(int userId);
}
