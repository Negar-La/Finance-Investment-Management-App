package Final.project.service;

import Final.project.dao.TransactionDao;
import Final.project.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
    @Override
    public Transaction getTransactionById(int id) {
        return transactionDao.getTransactionById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionDao.addTransaction(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        transactionDao.updateTransaction(transaction);
    }

    @Override
    public void deleteTransactionById(int id) {
        transactionDao.deleteTransactionById(id);
    }

    @Override
    public List<Transaction> getTransactionsByPortfolioId(int portfolioId) {
        return transactionDao.getTransactionsByPortfolioId(portfolioId);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionDao.getTransactionsByUserId(userId);
    }

    @Override
    public BigDecimal getUserBalance(int userId) throws InsufficientFundsException {
        List<Transaction> transactions = transactionDao.getTransactionsByUserId(userId);
        BigDecimal balance = BigDecimal.ZERO;

        //the user is adding assets when they perform a "buy" or "deposit"
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if ("buy".equalsIgnoreCase(transaction.getTransactionType())
                    || "deposit".equalsIgnoreCase(transaction.getTransactionType())) {
                balance = balance.add(amount);
            } else if ("sell".equalsIgnoreCase(transaction.getTransactionType())
                    || "withdrawal".equalsIgnoreCase(transaction.getTransactionType())) {
                if (balance.compareTo(amount) >= 0) {
                    balance = balance.subtract(amount);
                } else {
                    throw new InsufficientFundsException("Insufficient funds for withdrawal.");
                }
            }
        }

        return balance;
    }
}
