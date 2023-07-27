package Final.project.service;

import Final.project.dao.TransactionDao;
import Final.project.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transactionDao;
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
}
