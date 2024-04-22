package org.example.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // JDBC URL for SQLite database (replace "CodenameDB.sqlite" with your database file path)
    private static final String url = "jdbc:sqlite:CodenameDB.sqlite";

    // Get connection to the SQLite database
    public static Connection getConnection() throws SQLException {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
    }
}
