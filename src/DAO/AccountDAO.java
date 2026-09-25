package dao;

import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account create(Account account) throws SQLException {

        String sql = """
            INSERT INTO accounts (
                number,
                balance,
                client_id,
                account_type,
                authorized_overdraft,
                interest_rate
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                sql,
                PreparedStatement.RETURN_GENERATED_KEYS
            )
        ) {

            statement.setString(1, account.getNumber());
            statement.setDouble(2, account.getBalance());
            statement.setLong(3, account.getClientId());

            if (account instanceof CurrentAccount currentAccount) {

                statement.setString(4, "CURRENT");
                statement.setDouble(
                    5,
                    currentAccount.getAuthorizedOverdraft()
                );
                statement.setNull(6, java.sql.Types.DECIMAL);

            } else if (account instanceof SavingsAccount savingsAccount) {

                statement.setString(4, "SAVINGS");
                statement.setNull(5, java.sql.Types.DECIMAL);
                statement.setDouble(
                    6,
                    savingsAccount.getInterestRate()
                );

            } else {
                throw new IllegalArgumentException(
                    "Unknown account type"
                );
            }

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {

                Long id = keys.getLong(1);

                if (account instanceof CurrentAccount currentAccount) {

                    return new CurrentAccount(
                        id,
                        currentAccount.getNumber(),
                        currentAccount.getBalance(),
                        currentAccount.getClientId(),
                        currentAccount.getAuthorizedOverdraft()
                    );

                } else {

                    SavingsAccount savingsAccount =
                        (SavingsAccount) account;

                    return new SavingsAccount(
                        id,
                        savingsAccount.getNumber(),
                        savingsAccount.getBalance(),
                        savingsAccount.getClientId(),
                        savingsAccount.getInterestRate()
                    );
                }
            }
        }

        return null;
    }

    public Optional<Account> findById(Long id) throws SQLException {

        String sql = """
            SELECT *
            FROM accounts
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapAccount(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public List<Account> findAll() throws SQLException {

        String sql = """
            SELECT *
            FROM accounts
        """;

        List<Account> accounts = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                accounts.add(mapAccount(resultSet));
            }
        }

        return accounts;
    }

    public List<Account> findByClientId(Long clientId)
            throws SQLException {

        String sql = """
            SELECT *
            FROM accounts
            WHERE client_id = ?
        """;

        List<Account> accounts = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    accounts.add(mapAccount(resultSet));
                }
            }
        }

        return accounts;
    }

    public boolean update(Account account) throws SQLException {

        String sql = """
            UPDATE accounts
            SET number = ?,
                balance = ?,
                client_id = ?,
                account_type = ?,
                authorized_overdraft = ?,
                interest_rate = ?
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, account.getNumber());
            statement.setDouble(2, account.getBalance());
            statement.setLong(3, account.getClientId());

            if (account instanceof CurrentAccount currentAccount) {

                statement.setString(4, "CURRENT");
                statement.setDouble(
                    5,
                    currentAccount.getAuthorizedOverdraft()
                );
                statement.setNull(6, Types.DECIMAL);

            } else if (account instanceof SavingsAccount savingsAccount) {

                statement.setString(4, "SAVINGS");
                statement.setNull(5, Types.DECIMAL);
                statement.setDouble(
                    6,
                    savingsAccount.getInterestRate()
                );

            } else {
                throw new IllegalArgumentException("Unknown account type");
            }

            statement.setLong(7, account.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(Long id) throws SQLException {

        String sql = """
            DELETE FROM accounts
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

    private Account mapAccount(ResultSet resultSet)
            throws SQLException {

        Long id = resultSet.getLong("id");
        String number = resultSet.getString("number");
        double balance = resultSet.getDouble("balance");
        Long clientId = resultSet.getLong("client_id");

        String type = resultSet.getString("account_type");

        if (type.equals("CURRENT")) {

            double overdraft =
                resultSet.getDouble("authorized_overdraft");

            return new CurrentAccount(
                id,
                number,
                balance,
                clientId,
                overdraft
            );

        } else if (type.equals("SAVINGS")) {

            double interestRate =
                resultSet.getDouble("interest_rate");

            return new SavingsAccount(
                id,
                number,
                balance,
                clientId,
                interestRate
            );
        }

        throw new IllegalArgumentException(
            "Unknown account type: " + type
        );
    }
}