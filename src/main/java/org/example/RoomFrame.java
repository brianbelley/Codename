package org.example;
import org.example.model.CardType;
import org.example.model.Difficulty;
import org.example.model.KeyCard;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoomFrame extends JFrame {

    private JTextField roomIdField;
    private JTextField roomCodeField;
    private JComboBox<Difficulty> difficultyComboBox;
    private JTextField roomSizeField;

    // SQLite connection
    private Connection connection;

    public RoomFrame() {
        setTitle("Create Room");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5,5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Initialize SQLite connection
        try {
            // Connect to SQLite database (create one if not exists)
            connection = DriverManager.getConnection("jdbc:sqlite:CodenameDB.sqlite");

            // Create rooms table if not exists
            String createRoomsTableQuery = "CREATE TABLE IF NOT EXISTS Rooms (" +
                    "RoomID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "RoomCode INTEGER UNIQUE," +
                    "Difficulty TEXT," +
                    "RoomSize INTEGER)";
            connection.createStatement().executeUpdate(createRoomsTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database.");
            dispose();
        }

        // Labels
        JLabel roomIdLabel = new JLabel("Room ID:");
        JLabel roomCodeLabel = new JLabel("Room Code:");
        JLabel difficultyLabel = new JLabel("Difficulty:");
        JLabel roomSizeLabel = new JLabel("Room Size:");

        // TextFields and ComboBox
        roomIdField = new JTextField();
        roomCodeField = new JTextField();
        difficultyComboBox = new JComboBox<>(Difficulty.values());
        roomSizeField = new JTextField();

        // Button
        JButton createRoomButton = new JButton("Create Room");

        // Add labels and components to the frame
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(roomIdLabel, gbc);
        gbc.gridy = 1;
        add(roomCodeLabel, gbc);
        gbc.gridy = 2;
        add(difficultyLabel, gbc);
        gbc.gridy = 3;
        add(roomSizeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow text fields to grow horizontally
        roomIdField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        add(roomIdField, gbc);
        gbc.gridy = 1;
        add(roomCodeField, gbc);
        gbc.gridy = 2;
        add(difficultyComboBox, gbc);
        gbc.gridy = 3;
        add(roomSizeField, gbc);

        // Create room button action listener
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(createRoomButton, gbc);
        createRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createRoom();
            }
        });

        // Set size
        setSize(400, 250);

        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);
    }

    private void createRoom() {
        int roomId = Integer.parseInt(roomIdField.getText().trim()); // Assuming roomId is provided
        int roomCode = Integer.parseInt(roomCodeField.getText().trim()); // Assuming roomCode is provided
        Difficulty difficulty = (Difficulty) difficultyComboBox.getSelectedItem();
        int roomSize = Integer.parseInt(roomSizeField.getText().trim()); // Assuming roomSize is provided

        // Ensure that the KeyCard table exists
        ensureKeyCardTableExists();

        // Insert room data into Rooms table
        String insertRoomQuery = "INSERT INTO Rooms (RoomID, RoomCode, Difficulty, RoomSize) VALUES (?, ?, ?, ?)";
        try (PreparedStatement roomStatement = connection.prepareStatement(insertRoomQuery)) {
            // Insert room data
            roomStatement.setInt(1, roomId);
            roomStatement.setInt(2, roomCode);
            roomStatement.setString(3, difficulty.toString());
            roomStatement.setInt(4, roomSize);
            roomStatement.executeUpdate();

            // Generate keycards based on the specified difficulty
            List<KeyCard> keyCards = KeyCard.generateKeyCards(String.valueOf(difficulty));


            // Open the CodenameFrame with the room ID and list of keycards
            SwingUtilities.invokeLater(() -> new CodenameFrame(roomId, keyCards));

            JOptionPane.showMessageDialog(this, "Room created successfully!");
            dispose(); // Close the RoomFrame

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to create room: " + ex.getMessage());
            ex.printStackTrace();
        }
    }




    private void ensureKeyCardTableExists() {
        // Check if the KeyCard table exists, if not, create it
        String createKeyCardTableQuery = "CREATE TABLE IF NOT EXISTS KeyCard (" +
                "CardNumber INTEGER PRIMARY KEY," +
                "CardWord TEXT," +
                "CardType TEXT)";
        try {
            connection.createStatement().executeUpdate(createKeyCardTableQuery);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to ensure KeyCard table exists: " + ex.getMessage());
            ex.printStackTrace();
        }
    }



    private List<KeyCard> generateKeyCards(Difficulty difficulty) {
        // Generate keycards based on difficulty level
        // You can adjust the logic to generate keycards based on the difficulty
        List<KeyCard> keyCards = new ArrayList<>();
        int numKeyCards = 25; // Total number of keycards
        int numRedCards = numKeyCards / 3; // 1/3 of keycards are RED
        int numBlueCards = numKeyCards / 3; // 1/3 of keycards are BLUE
        int numNeutralCards = numKeyCards / 3; // 1/3 of keycards are NEUTRAL

        // Generate RED keycards
        for (int i = 1; i <= numRedCards; i++) {
            keyCards.add(new KeyCard(i, "RedWord" + i, CardType.RED));
        }

        // Generate BLUE keycards
        for (int i = 1; i <= numBlueCards; i++) {
            keyCards.add(new KeyCard(i + numRedCards, "BlueWord" + i, CardType.BLUE));
        }

        // Generate NEUTRAL keycards
        for (int i = 1; i <= numNeutralCards; i++) {
            keyCards.add(new KeyCard(i + numRedCards + numBlueCards, "NeutralWord" + i, CardType.NEUTRAL));
        }

        // Shuffle the keycards to randomize their positions
        Collections.shuffle(keyCards);

        // Return the generated keycards
        return keyCards;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomFrame::new);
    }
}

