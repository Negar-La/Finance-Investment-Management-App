package Final.project.service;

import Final.project.dao.TransactionDao;
import Final.project.entities.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDaoStubImpl implements TransactionDao {

    private Map<Integer, List<Transaction>> transactionsByUserId = new HashMap<>();

    @Override
    public Transaction getTransactionById(int id) {
        // Implement this method if needed for other tests
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        // Implement this method if needed for other tests
        return null;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        // Implement this method if needed for other tests
        return null;
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        // Implement this method if needed for other tests
    }

    @Override
    public void deleteTransactionById(int id) {
        // Implement this method if needed for other tests
    }

    // Method to set the transactions for a specific user in the stub
    public void setTransactionsForUser(int userId, List<Transaction> transactions) {
        transactionsByUserId.put(userId, transactions);
    }

    @Override
    public List<Transaction> getTransactionsByPortfolioId(int portfolioId) {
        // Implement this method if needed for other tests
        return null;
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionsByUserId.getOrDefault(userId, new ArrayList<>());
    }
}

