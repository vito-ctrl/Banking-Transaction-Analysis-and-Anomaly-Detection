package dao;

import model.Transaction;
import model.TransactionType;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO {

    // CREATE
    // Inserts the transaction AND updates the account balance
    // in a single atomic DB transaction: either both happen, or neither does.
    public Transaction create(Transaction transaction) throws SQLException {

        String insertSql = """
            INSERT INTO transactions
            (transaction_date, amount, transaction_type, location, account_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        String balanceSql = """
            UPDATE accounts
            SET balance = balance + ?
            WHERE id = ?
        """;

        // NOTE: TRANSFER only has a single account_id on this model, so it is
        // treated as a debit from that account (same as WITHDRAWAL). If you need
        // TRANSFER to also credit a destination account, the model/schema will
        // need a second account column and this method updated accordingly.
        double balanceDelta = switch (transaction.type()) {
            case DEPOSIT -> transaction.amount();
            case WITHDRAWAL, TRANSFER -> -transaction.amount();
        };

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            Transaction created;

            try (PreparedStatement statement =
                         connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setTimestamp(1, Timestamp.valueOf(transaction.date()));
                statement.setDouble(2, transaction.amount());
                statement.setString(3, transaction.type().name());
                statement.setString(4, transaction.location());
                statement.setLong(5, transaction.accountId());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (!resultSet.next()) {
                        throw new SQLException("Failed to create transaction.");
                    }

                    Long id = resultSet.getLong(1);

                    created = new Transaction(
                            id,
                            transaction.date(),
                            transaction.amount(),
                            transaction.type(),
                            transaction.location(),
                            transaction.accountId()
                    );
                }
            }

            try (PreparedStatement balanceStatement = connection.prepareStatement(balanceSql)) {

                balanceStatement.setDouble(1, balanceDelta);
                balanceStatement.setLong(2, transaction.accountId());

                int rowsAffected = balanceStatement.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SQLException(
                            "Account " + transaction.accountId() + " does not exist; balance not updated."
                    );
                }
            }

            connection.commit();
            return created;

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }

            throw e;

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    // Nothing more we can do; avoid masking the original exception.
                }
            }
        }
    }


    // FIND BY ID
    public Optional<Transaction> findById(Long id) throws SQLException {

        String sql = """
            SELECT id, transaction_date, amount,
                   transaction_type, location, account_id
            FROM transactions
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapTransaction(resultSet));
                }
            }
        }

        return Optional.empty();
    }


    // FIND ALL
    public List<Transaction> findAll() throws SQLException {

        String sql = """
            SELECT id, transaction_date, amount,
                   transaction_type, location, account_id
            FROM transactions
            ORDER BY transaction_date DESC
        """;

        List<Transaction> transactions = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                transactions.add(mapTransaction(resultSet));
            }
        }

        return transactions;
    }


    // FIND BY ACCOUNT
    public List<Transaction> findByAccountId(Long accountId)
            throws SQLException {

        String sql = """
            SELECT id, transaction_date, amount,
                   transaction_type, location, account_id
            FROM transactions
            WHERE account_id = ?
            ORDER BY transaction_date DESC
        """;

        List<Transaction> transactions = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    transactions.add(mapTransaction(resultSet));
                }
            }
        }

        return transactions;
    }


    // UPDATE
    public boolean update(Transaction transaction)
            throws SQLException {

        String sql = """
            UPDATE transactions
            SET transaction_date = ?,
                amount = ?,
                transaction_type = ?,
                location = ?,
                account_id = ?
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setTimestamp(
                    1,
                    Timestamp.valueOf(transaction.date())
            );

            statement.setDouble(2, transaction.amount());
            statement.setString(3, transaction.type().name());
            statement.setString(4, transaction.location());
            statement.setLong(5, transaction.accountId());
            statement.setLong(6, transaction.id());

            return statement.executeUpdate() > 0;
        }
    }


    // DELETE
    public boolean deleteById(Long id) throws SQLException {

        String sql = """
            DELETE FROM transactions
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        }
    }


    // MAP RESULTSET → TRANSACTION
    private Transaction mapTransaction(ResultSet resultSet)
            throws SQLException {

        Long id = resultSet.getLong("id");

        LocalDateTime date =
                resultSet
                        .getTimestamp("transaction_date")
                        .toLocalDateTime();

        double amount =
                resultSet.getDouble("amount");

        TransactionType type =
                TransactionType.valueOf(
                        resultSet.getString("transaction_type")
                );

        String location =
                resultSet.getString("location");

        Long accountId =
                resultSet.getLong("account_id");

        return new Transaction(
                id,
                date,
                amount,
                type,
                location,
                accountId
        );
    }
}