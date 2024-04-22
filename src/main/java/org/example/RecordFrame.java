package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecordFrame extends JFrame {
    private String username;
    private JTextArea recordTextArea;

    public RecordFrame(String username) {
        this.username = username;
        setTitle("Records");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a JTextArea to display records
        recordTextArea = new JTextArea();
        recordTextArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(recordTextArea); // Add scroll bars
        add(scrollPane, BorderLayout.CENTER);

        // Load records from the database
        loadRecords();

        // Create a back button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the RecordFrame and open the MenuFrame
                new MenuFrame(username);
                dispose();
            }
        });
        add(backButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);
    }

    private void loadRecords() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:CodenameDB.sqlite")) {
            // SQL query to select records for the user
            String selectQuery = "SELECT * FROM Records WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                // Display records in the JTextArea
                while (resultSet.next()) {
                    int roundNumber = resultSet.getInt("roundNumber");
                    int scoreNumber = resultSet.getInt("scoreNumber");
                    String result = resultSet.getString("result");
                    recordTextArea.append("Round Number: " + roundNumber + ", Score: " + scoreNumber + ", Result: " + result + "\n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load records: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}

