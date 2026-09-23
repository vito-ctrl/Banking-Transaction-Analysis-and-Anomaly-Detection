package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverConnection {
    private static final String url = "jdbc:mysql://localhost:3306/banking_analysis";
    private static final String USER = "vito";
    private static final String PASSWORD = "Vito123456789@";

    public static Connection.getConnection() throw SQLException{
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}