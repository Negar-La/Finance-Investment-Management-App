package Final.project.dao;

import Final.project.entities.Transaction;

import java.util.List;

public interface TransactionDao {
    Transaction getTransactionById(int id);
    List<Transaction> getAllTransactions();
    Transaction addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransactionById(int id);

    List<Transaction> getTransactionsByPortfolioId(int portfolioId);
    List<Transaction> getTransactionsByUserId(int userId);
}
