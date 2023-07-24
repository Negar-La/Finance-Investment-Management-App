package Final.project.entities;

import java.util.List;

public class Portfolio {
    private int portfolioID;
    private String portfolioName;
    //private int accountID; // Foreign key to associate the portfolio with the account
    private List<Asset> assets;

    // Constructors, getters, setters, and other methods


    public int getPortfolioID() {
        return portfolioID;
    }

    public void setPortfolioID(int portfolioID) {
        this.portfolioID = portfolioID;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portfolio portfolio = (Portfolio) o;

        if (portfolioID != portfolio.portfolioID) return false;
        if (!portfolioName.equals(portfolio.portfolioName)) return false;
        return assets.equals(portfolio.assets);
    }

    @Override
    public int hashCode() {
        int result = portfolioID;
        result = 31 * result + portfolioName.hashCode();
        result = 31 * result + assets.hashCode();
        return result;
    }
}

