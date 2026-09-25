package dao;

import model.Client;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public Client create(Client client) throws SQLException {

        String sql = """
            INSERT INTO clients (name, email)
            VALUES (?, ?)
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                sql,
                PreparedStatement.RETURN_GENERATED_KEYS
            )
        ) {

            statement.setString(1, client.name());
            statement.setString(2, client.email());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                Long id = keys.getLong(1);

                return new Client(
                    id,
                    client.name(),
                    client.email()
                );
            }
        }

        return null;
    }

    public Optional<Client> findById(Long id) throws SQLException {

        String sql = """
            SELECT id, name, email
            FROM clients
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Client client = new Client(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                );

                return Optional.of(client);
            }
        }

        return Optional.empty();
    }

    public List<Client> findAll() throws SQLException {

        String sql = """
            SELECT id, name, email
            FROM clients
        """;

        List<Client> clients = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Client client = new Client(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                );

                clients.add(client);
            }
        }

        return clients;
    }

    public boolean update(Client client) throws SQLException {

        String sql = """
            UPDATE clients
            SET name = ?, email = ?
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, client.name());
            statement.setString(2, client.email());
            statement.setLong(3, client.id());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public boolean deleteById(Long id) throws SQLException {

        String sql = """
            DELETE FROM clients
            WHERE id = ?
        """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }


}