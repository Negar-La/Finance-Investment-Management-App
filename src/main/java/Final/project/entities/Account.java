package Final.project.entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class Account {
    private int accountID;
    @NotBlank(message = "Account Name must not be blank")
    @Size(max = 45, message="Account Name must be fewer than 45 characters")
    private String accountName;
    @NotBlank(message = "Account Type must not be blank")
    @Size(max = 45, message="Account Type must be fewer than 45 characters")
    private String accountType;

    //private User user;
    @Positive(message = "User ID must be a positive number")
    private int userID; // Foreign key to associate the account with the user
    private List<Portfolio> portfolios; // Composition: Account has multiple Portfolios

    // Constructor, getters, setters, and other methods

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountID != account.accountID) return false;
        if (userID != account.userID) return false;
        if (!accountName.equals(account.accountName)) return false;
        if (!accountType.equals(account.accountType)) return false;
        return Objects.equals(portfolios, account.portfolios);
    }

    @Override
    public int hashCode() {
        int result = accountID;
        result = 31 * result + accountName.hashCode();
        result = 31 * result + accountType.hashCode();
        result = 31 * result + userID;
        result = 31 * result + (portfolios != null ? portfolios.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountID=" + accountID +
                ", accountName='" + accountName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", userID=" + userID +
                ", portfolios=" + portfolios +
                '}';
    }
}
