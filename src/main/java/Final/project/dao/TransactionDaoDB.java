package Final.project.dao;

import Final.project.entities.Asset;
import Final.project.entities.Portfolio;
import Final.project.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class TransactionDaoDB implements TransactionDao{

    @Autowired
    private JdbcTemplate jdbc;


    @Override
    public Transaction getTransactionById(int id) {
        try {
            final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM Transaction WHERE TransactionID = ?";
            Transaction transaction = jdbc.queryForObject(SELECT_TRANSACTION_BY_ID, new TransactionMapper(), id);

            insertPortfolio(transaction);
            insertAsset(transaction);
            return transaction;
        } catch (DataAccessException ex) {
            // If no transaction is found with the given ID, return null
            return null;
        }
    }

    private void insertPortfolio(Transaction transaction) {
        final String SELECT_PORTFOLIO_BY_TRANSACTION_ID = "SELECT * FROM Portfolio WHERE PortfolioID = (SELECT PortfolioID FROM Transaction WHERE TransactionID = ?)";
        Portfolio portfolio = jdbc.queryForObject(SELECT_PORTFOLIO_BY_TRANSACTION_ID, new PortfolioDaoDB.PortfolioMapper(), transaction.getTransactionID());
        transaction.setPortfolio(portfolio);
    }


    private void insertAsset(Transaction transaction) {
      //  System.out.println("Portfolio before setting assets:");
      //  System.out.println(transaction.getPortfolio());

        String sql = "SELECT A.* FROM Asset A JOIN Portfolio_Asset PA ON A.AssetID = PA.AssetID WHERE PA.PortfolioID = ?";
        List<Asset> assets = jdbc.query(sql, new AssetDaoDB.AssetMapper(), transaction.getPortfolio().getPortfolioID());
        transaction.getPortfolio().setAssets(assets);
        String sql_for_asset = "select a.* from asset a join transaction t on a.assetID = t.assetID where t.transactionID = ?";
        Asset asset = jdbc.queryForObject(sql_for_asset, new AssetDaoDB.AssetMapper(), transaction.getTransactionID());
        transaction.setAsset(asset);

     //   System.out.println("Portfolio after setting assets:");
     //   System.out.println(transaction.getPortfolio());
    }


    @Override
    public List<Transaction> getAllTransactions() {
        final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM Transaction";
        List<Transaction> transactions = jdbc.query(SELECT_ALL_TRANSACTIONS, new TransactionMapper());

        // Fetch and set the associated Portfolio and Asset objects for each Transaction
        for (Transaction transaction : transactions) {
            insertPortfolio(transaction);
            insertAsset(transaction);
        }
        return transactions;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        final String INSERT_TRANSACTION = "INSERT INTO Transaction (TransactionDate, Amount, TransactionType, Description, PortfolioID, AssetID) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            jdbc.update(
                    INSERT_TRANSACTION,
                    transaction.getTransactionDate(),
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getDescription(),
                    transaction.getPortfolio().getPortfolioID(),
                    transaction.getAsset().getAssetID()
            );

            int transactionId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            transaction.setTransactionID(transactionId);

            return transaction;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        final String UPDATE_TRANSACTION = "UPDATE Transaction SET TransactionDate = ?, Amount = ?, TransactionType = ?, Description = ?, PortfolioID = ?, AssetID = ? WHERE TransactionID = ?";
        jdbc.update(
                UPDATE_TRANSACTION,
                transaction.getTransactionDate(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getDescription(),
                transaction.getPortfolio().getPortfolioID(),
                transaction.getAsset().getAssetID(),
                transaction.getTransactionID()
        );
    }

    @Override
    public void deleteTransactionById(int id) {
        final String DELETE_TRANSACTION = "DELETE FROM Transaction WHERE TransactionID = ?";
        jdbc.update(DELETE_TRANSACTION, id);
    }

    @Override
    public List<Transaction> getTransactionsByPortfolioId(int portfolioId) {
        String sql = "SELECT * FROM Transaction WHERE PortfolioID = ?";

        List<Transaction> transactions = new ArrayList<>();

        try {
            // Fetch transactions with the given portfolioId
            transactions = jdbc.query(sql, new TransactionMapper(), portfolioId);

            // Fetch and set the associated Portfolio and Asset objects for each Transaction
            for (Transaction transaction : transactions) {
                insertPortfolio(transaction);
                insertAsset(transaction);
            }
        } catch (DataAccessException ex) {
            // Handle any exceptions that might occur during data retrieval
            ex.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
       // String sql = "SELECT t.*, p.PortfolioName, a.AssetID, a.AssetName, a.AssetType FROM Transaction t INNER JOIN Portfolio p ON t.PortfolioID = p.PortfolioID INNER JOIN Asset a ON t.AssetID = a.AssetID WHERE p.AccountID = ?";
        String sql =  "SELECT t.* FROM Transaction t JOIN Portfolio p ON t.PortfolioID = p.PortfolioID JOIN Account ac ON p.AccountID = ac.AccountID WHERE ac.UserID = ?";
        List<Transaction> transactions = new ArrayList<>();

        try {
            transactions = jdbc.query(sql, new TransactionMapper(), userId);

            for (Transaction transaction : transactions) {
                insertPortfolio(transaction);
                insertAsset(transaction);
            }
        } catch (DataAccessException ex) {
            // Handle any exceptions that might occur during data retrieval
            ex.printStackTrace();
        }
        return transactions;
    }


    // Mapper class to convert the database result into a Transaction object
    public static final class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTransactionID(rs.getInt("TransactionID"));
            transaction.setTransactionDate(rs.getDate("TransactionDate").toLocalDate());
            transaction.setAmount(rs.getBigDecimal("Amount"));
            transaction.setTransactionType(rs.getString("TransactionType"));
            transaction.setDescription(rs.getString("Description"));

            return transaction;
        }
    }
}
