package Final.project.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Transaction {
    private int transactionID;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private String transactionType;
    private String description;

    private Portfolio portfolio;
    private Asset asset;

    // Constructors, getters, setters, and other methods

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (transactionID != that.transactionID) return false;
        if (!transactionDate.equals(that.transactionDate)) return false;
        if (!amount.equals(that.amount)) return false;
        if (!transactionType.equals(that.transactionType)) return false;
        if (!description.equals(that.description)) return false;
        if (!portfolio.equals(that.portfolio)) return false;
        return asset.equals(that.asset);
    }

    @Override
    public int hashCode() {
        int result = transactionID;
        result = 31 * result + transactionDate.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + transactionType.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + portfolio.hashCode();
        result = 31 * result + asset.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", transactionDate=" + transactionDate +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", description='" + description + '\'' +
                ", portfolio=" + portfolio +
                ", asset=" + asset +
                '}';
    }
}

