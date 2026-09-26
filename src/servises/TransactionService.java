package service;

import dao.TransactionDAO;
import model.Transaction;
import model.TransactionType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }


    // =========================
    // CREATE
    // =========================

    public Transaction create(Transaction transaction)
            throws SQLException {

        validateTransaction(transaction);

        return transactionDAO.create(transaction);
    }


    // =========================
    // FIND BY ID
    // =========================

    public Optional<Transaction> findById(Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be positive."
            );
        }

        return transactionDAO.findById(id);
    }


    // =========================
    // FIND ALL
    // =========================

    public List<Transaction> findAll()
            throws SQLException {

        return transactionDAO.findAll();
    }


    // =========================
    // FIND BY ACCOUNT
    // =========================

    public List<Transaction> findByAccountId(Long accountId)
            throws SQLException {

        if (accountId == null || accountId <= 0) {
            throw new IllegalArgumentException(
                    "Account ID must be positive."
            );
        }

        return transactionDAO.findByAccountId(accountId);
    }


    // =========================
    // UPDATE
    // =========================

    public boolean update(Transaction transaction)
            throws SQLException {

        validateTransaction(transaction);

        if (transaction.id() == null || transaction.id() <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be positive."
            );
        }

        return transactionDAO.update(transaction);
    }


    // =========================
    // DELETE
    // =========================

    public boolean deleteById(Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be positive."
            );
        }

        return transactionDAO.deleteById(id);
    }


    // =========================
    // FILTER BY TYPE
    // =========================

    public List<Transaction> findByType(
            TransactionType type
    ) throws SQLException {

        if (type == null) {
            throw new IllegalArgumentException(
                    "Transaction type cannot be null."
            );
        }

        return findAll()
                .stream()
                .filter(transaction ->
                        transaction.type() == type
                )
                .toList();
    }


    // =========================
    // TOTAL AMOUNT
    // =========================

    public double calculateTotalAmount()
            throws SQLException {

        return findAll()
                .stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }


    // =========================
    // TOTAL BY TYPE
    // =========================

    public double calculateTotalByType(
            TransactionType type
    ) throws SQLException {

        return findByType(type)
                .stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }


    // =========================
    // GROUP BY TYPE
    // =========================

    public Map<TransactionType, List<Transaction>> groupByType()
            throws SQLException {

        return findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::type
                        )
                );
    }


    // =========================
    // SUSPICIOUS TRANSACTIONS
    // =========================

    public List<Transaction> detectSuspiciousTransactions()
            throws SQLException {

        return findAll()
                .stream()
                .filter(transaction ->
                        transaction.amount() >= 10000
                )
                .toList();
    }


    // =========================
    // VALIDATION
    // =========================

    private void validateTransaction(
            Transaction transaction
    ) {

        if (transaction == null) {
            throw new IllegalArgumentException(
                    "Transaction cannot be null."
            );
        }

        if (transaction.amount() <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than 0."
            );
        }

        if (transaction.type() == null) {
            throw new IllegalArgumentException(
                    "Transaction type cannot be null."
            );
        }

        if (transaction.location() == null ||
                transaction.location().isBlank()) {

            throw new IllegalArgumentException(
                    "Transaction location cannot be empty."
            );
        }

        if (transaction.accountId() == null ||
                transaction.accountId() <= 0) {

            throw new IllegalArgumentException(
                    "Account ID must be positive."
            );
        }

        if (transaction.date() == null) {
            throw new IllegalArgumentException(
                    "Transaction date cannot be null."
            );
        }
    }
}