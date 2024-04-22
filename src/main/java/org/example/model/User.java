package org.example.model;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.example.dataaccess.DatabaseConnection.getConnection;

public class User {
    private String username;
    private String email;
    private String password;
    private Record record;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.record = new Record();
    }

    public User(String username, Record record) {
        this.username = username;
        this.record = record;
    }

    // Getters and setters for username, email, password, and record
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    // toString method for printing user details
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", record=" + record +
                '}';
    }

    // Create Records table if it doesn't exist
    private static void createRecordsTableIfNotExists(Connection connection) throws SQLException {
        String createRecordsTableQuery = "CREATE TABLE IF NOT EXISTS Records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "roundNumber INTEGER," +
                "scoreNumber INTEGER," +
                "result TEXT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createRecordsTableQuery);
        }
    }

    // Save the record to the database
    public void saveRecord(User user) {
        // Get connection to the database
        try (Connection connection = getConnection()) {
            //Check if the Records table is created
            createRecordsTableIfNotExists(connection);

            // SQL query to insert a record into the Records table
            String insertRecordQuery = "INSERT INTO Records (username, roundNumber, scoreNumber, result) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertRecordQuery)) {
                // Set values for the prepared statement
                statement.setString(1, user.getUsername());
                statement.setInt(2, record.getRoundNumber());
                statement.setInt(3, record.getScoreNumber());
                statement.setString(4, record.getResult());
                // Execute the query
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record saved successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to save record: " + e.getMessage());
            e.printStackTrace();
        }
    }



}

