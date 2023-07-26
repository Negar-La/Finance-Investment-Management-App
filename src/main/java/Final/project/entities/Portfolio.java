package Final.project.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class Portfolio {
    private int portfolioID;
    @NotBlank(message = "Account Type must not be blank")
    @Size(max = 45, message="Account Type must be fewer than 45 characters")
    private String portfolioName;
    //private int accountID; // Foreign key to associate the portfolio with the account

    private int accountID; // Foreign key to associate the portfolio with the account
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

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
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
        if (accountID != portfolio.accountID) return false;
        if (!Objects.equals(portfolioName, portfolio.portfolioName)) return false;
        return Objects.equals(assets, portfolio.assets);
    }


    @Override
    public int hashCode() {
        int result = portfolioID;
        result = 31 * result + portfolioName.hashCode();
        result = 31 * result + accountID;
        result = 31 * result + (assets != null ? assets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "portfolioID=" + portfolioID +
                ", portfolioName='" + portfolioName + '\'' +
                ", assets=" + assets +
                '}';
    }
}

