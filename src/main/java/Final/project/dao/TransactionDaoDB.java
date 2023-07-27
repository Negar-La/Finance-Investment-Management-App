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
        final String SELECT_PORTFOLIO_BY_TRANSACTION_ID = "SELECT * FROM Portfolio WHERE PortfolioID = (SELECT PortfolioID FROM Account WHERE AccountID = (SELECT AccountID FROM Transaction WHERE TransactionID = ?))";
        Portfolio portfolio = jdbc.queryForObject(SELECT_PORTFOLIO_BY_TRANSACTION_ID, new PortfolioDaoDB.PortfolioMapper(), transaction.getTransactionID());
        transaction.setPortfolio(portfolio);
    }

    private void insertAsset(Transaction transaction) {
        String sql = "SELECT * FROM Portfolio_Asset PA " +
                "JOIN Asset A ON PA.AssetID = A.AssetID " +
                "WHERE PA.PortfolioID = ?";
        List<Asset> assets = jdbc.query(sql, new AssetDaoDB.AssetMapper(), transaction.getPortfolio().getPortfolioID());
        transaction.getPortfolio().setAssets(assets);
    }





    @Override
    public List<Transaction> getAllTransactions() {
        final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM Transaction";
        return jdbc.query(SELECT_ALL_TRANSACTIONS, new TransactionMapper());
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

            // Retrieve the generated TransactionID
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
        final String SELECT_TRANSACTIONS_BY_PORTFOLIO_ID = "SELECT * FROM Transaction WHERE PortfolioID = ?";
        return jdbc.query(SELECT_TRANSACTIONS_BY_PORTFOLIO_ID, new TransactionMapper(), portfolioId);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        final String SELECT_TRANSACTIONS_BY_USER_ID = "SELECT t.* FROM Transaction t " +
                "JOIN Portfolio p ON t.PortfolioID = p.PortfolioID " +
                "JOIN Account ac ON p.AccountID = ac.AccountID " +
                "WHERE ac.UserID = ?";
        return jdbc.query(SELECT_TRANSACTIONS_BY_USER_ID, new TransactionMapper(), userId);
    }

    // Mapper class to convert the database result into a Transaction object
    public static final class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTransactionID(rs.getInt("TransactionID"));
            transaction.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
            transaction.setAmount(rs.getBigDecimal("Amount"));
            transaction.setTransactionType(rs.getString("TransactionType"));
            transaction.setDescription(rs.getString("Description"));
            return transaction;
        }
    }

}
