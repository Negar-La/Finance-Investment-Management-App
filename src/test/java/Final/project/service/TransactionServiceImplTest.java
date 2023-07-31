package Final.project.service;

import Final.project.entities.Transaction;
import Final.project.service.InsufficientFundsException;
import Final.project.service.TransactionDaoStubImpl;
import Final.project.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;
    private TransactionDaoStubImpl transactionDao;

    @BeforeEach
    public void setUp() {
        transactionDao = new TransactionDaoStubImpl();
        transactionService = new TransactionServiceImpl(transactionDao);
    }

    @Test
    public void testGetUserBalanceWithNoTransactions() throws InsufficientFundsException {
        // ARRANGE
        int userId = 1;
        transactionDao.setTransactionsForUser(userId, new ArrayList<>());

        // ACT
        BigDecimal balance = transactionService.getUserBalance(userId);

        // ASSERT
        assertEquals(BigDecimal.ZERO, balance);
    }

    @Test
    public void testGetUserBalanceWithDepositAndBuyTransactions() throws InsufficientFundsException {
        // ARRANGE
        int userId = 1;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("deposit", new BigDecimal("100.00")));
        transactions.add(createTransaction("buy", new BigDecimal("50.00")));
        transactionDao.setTransactionsForUser(userId, transactions);

        // ACT
        BigDecimal balance = transactionService.getUserBalance(userId);

        // ASSERT
        assertEquals(new BigDecimal("150.00"), balance);
    }

    @Test
    public void testGetUserBalanceWithWithdrawalAndSellTransactions() {
        // ARRANGE
        int userId = 1;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("withdrawal", new BigDecimal("70.00")));
        transactions.add(createTransaction("sell", new BigDecimal("30.00")));
        transactionDao.setTransactionsForUser(userId, transactions);

        // ACT & ASSERT
        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.getUserBalance(userId);
        });
    }

    @Test
    public void testGetUserBalanceWithInsufficientFunds() {
        // ARRANGE
        int userId = 1;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("withdrawal", new BigDecimal("200.00")));
        transactionDao.setTransactionsForUser(userId, transactions);

        // ACT & ASSERT
        assertThrows(InsufficientFundsException.class, () -> transactionService.getUserBalance(userId));
    }

    // Helper method to create a transaction with the given transaction type and amount
    private Transaction createTransaction(String transactionType, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        return transaction;
    }
}
