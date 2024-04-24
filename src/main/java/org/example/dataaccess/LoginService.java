package org.example.dataaccess;

import org.example.model.User;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private static Connection connection;

    static {
        try {
            // Connect to SQLite database
            connection = DriverManager.getConnection("jdbc:sqlite:CodenameDB.sqlite");

            // Create users table if not exists
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "email TEXT," +
                    "password TEXT)";
            connection.createStatement().executeUpdate(createUserTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database.");
            System.exit(1);
        }
    }

    public static void registerUser(User user) {
        String insertUserQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertUserQuery)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "User registered successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to register user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static boolean loginUser(String username, String password) {
        String loginQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(loginQuery)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if a row is returned
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to login: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}

