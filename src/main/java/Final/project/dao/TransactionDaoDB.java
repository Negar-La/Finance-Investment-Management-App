package Final.project.dao;

import Final.project.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionDaoDB implements TransactionDao{

    private final JdbcTemplate jdbcTemplate;
    private final PortfolioDao portfolioDao;
    private final AssetDao assetDao;

    @Autowired
    public TransactionDaoDB(JdbcTemplate jdbcTemplate, PortfolioDao portfolioDao, AssetDao assetDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.portfolioDao = portfolioDao;
        this.assetDao = assetDao;
    }



    @Override
    public Transaction getTransactionById(int id) {
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return null;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public void updateTransaction(Transaction transaction) {

    }

    @Override
    public void deleteTransactionById(int id) {

    }

    @Override
    public List<Transaction> getTransactionsByPortfolioId(int portfolioId) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        return null;
    }

    // Mapper class to convert the database result into a Transaction object
    private static final class TransactionMapper implements RowMapper<Transaction> {

        private final PortfolioDao portfolioDao;
        private final AssetDao assetDao;

        public TransactionMapper(PortfolioDao portfolioDao, AssetDao assetDao) {
            this.portfolioDao = portfolioDao;
            this.assetDao = assetDao;
        }

        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTransactionID(rs.getInt("TransactionID"));
            // Assuming transactionDate is stored as a TIMESTAMP or DATETIME in the database
            transaction.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
            transaction.setAmount(rs.getBigDecimal("Amount"));
            transaction.setTransactionType(rs.getString("TransactionType"));
            transaction.setDescription(rs.getString("Description"));
            int portfolioId = rs.getInt("PortfolioID");
            int assetId = rs.getInt("AssetID");
            // Set the portfolio and asset by querying the PortfolioDao and AssetDao
            transaction.setPortfolio(portfolioDao.getPortfolioById(portfolioId));
            transaction.setAsset(assetDao.getAssetById(assetId));
            return transaction;
        }
    }


}
