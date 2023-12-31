package Final.project.service;

import Final.project.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(int id);
    List<Transaction> getAllTransactions();
    Transaction addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransactionById(int id);


    List<Transaction> getTransactionsByPortfolioId(int portfolioId);
    List<Transaction> getTransactionsByUserId(int userId);

    List<Transaction> getTransactionsByDate(LocalDate date);

    BigDecimal getUserBalance(int userId) throws InsufficientFundsException;
}
