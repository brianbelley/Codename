package org.example.dataaccess;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Record;

public class DB {

    // Create Rooms table if it doesn't exist
    public static void createRoomsTableIfNotExists(Connection connection) {
        String createRoomsTableQuery = "CREATE TABLE IF NOT EXISTS Rooms (" +
                "roomId INTEGER PRIMARY KEY," +
                "roomCode INTEGER," +
                "difficulty TEXT," +
                "roomSize INTEGER)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createRoomsTableQuery);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to create Rooms table: " + e.getMessage());
            e.printStackTrace();
        }
    }


        // Save the room to the database
        public static void saveRoom ( int roomId, int roomCode, String difficulty,int roomSize){
            try (Connection connection = DatabaseConnection.getConnection()) {
                // Check if the Rooms table is created
                createRoomsTableIfNotExists(connection);

                // SQL query to insert a room into the Rooms table
                String insertRoomQuery = "INSERT INTO Rooms (roomId, roomCode, difficulty, roomSize) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertRoomQuery)) {
                    // Set values for the prepared statement
                    statement.setInt(1, roomId);
                    statement.setInt(2, roomCode);
                    statement.setString(3, difficulty);
                    statement.setInt(4, roomSize);
                    // Execute the query
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Room saved successfully!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to save room: " + e.getMessage());
                e.printStackTrace();
            }
        }

    // Create Records table if it doesn't exist
    public static void createRecordsTableIfNotExists(Connection connection) throws SQLException {
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
    public static void saveRecord(String username, int roundNumber, int scoreNumber, String result) {
        // Get connection to the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the Records table is created
            createRecordsTableIfNotExists(connection);

            // SQL query to insert a record into the Records table
            String insertRecordQuery = "INSERT INTO Records (username, roundNumber, scoreNumber, result) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertRecordQuery)) {
                // Set values for the prepared statement
                statement.setString(1, username);
                statement.setInt(2, roundNumber);
                statement.setInt(3, scoreNumber);
                statement.setString(4, result);
                // Execute the query
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record saved successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to save record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // List records for a given username
    public static List<Record> listRecords(String username) {
        List<Record> records = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            // SQL query to select records for the user
            String selectQuery = "SELECT * FROM Records WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                // Retrieve records from the result set and add them to the list
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int roundNumber = resultSet.getInt("roundNumber");
                    int scoreNumber = resultSet.getInt("scoreNumber");
                    String result = resultSet.getString("result");
                    Record record = new Record(id,roundNumber, scoreNumber, result);
                    records.add(record);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to load records: " + ex.getMessage());
            ex.printStackTrace();
        }

        return records;
    }


    // Delete a record from the database
    public static void deleteRecord(int recordId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // SQL query to delete a record from the Records table by its ID
            String deleteRecordQuery = "DELETE FROM Records WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteRecordQuery)) {
                // Set the value for the record ID parameter
                statement.setInt(1, recordId);
                // Execute the deletion query
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "No record found with ID: " + recordId);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to delete record: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
