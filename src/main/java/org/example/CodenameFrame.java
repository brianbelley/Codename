package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.example.model.*;

public class CodenameFrame extends JFrame {

    private int roomIdToDelete; // Variable to store the room ID to delete
    private List<KeyCard> keyCards; // List to store keycards

    public CodenameFrame(int roomIdToDelete, List<KeyCard> keyCards) {
        this.roomIdToDelete = roomIdToDelete; // Store the room ID to delete
        this.keyCards = keyCards; // Store the keycards


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Add components
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Codename Game");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridLayout(5, 5)); // Example layout
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 2));
        JButton endTurnButton = new JButton("End Turn");
        JButton revealCardButton = new JButton("Reveal Card");
        JLabel clueLabel = new JLabel("Clue:");
        JTextField clueText = new JTextField();
        JLabel numberLabel = new JLabel("Number:");
        JTextField numberTextField = new JTextField();

        bottomPanel.add(clueLabel);
        bottomPanel.add(clueText);
        bottomPanel.add(numberLabel);
        bottomPanel.add(numberTextField);
        bottomPanel.add(revealCardButton);
        bottomPanel.add(endTurnButton);

        add(bottomPanel, BorderLayout.SOUTH);


        // End turn button action listener
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle end turn logic
                JOptionPane.showMessageDialog(CodenameFrame.this, "End Turn Button Clicked");
            }
        });

        // Add window listener to delete the room when closing the frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deleteRoom();
            }
        });

        // Set size and make visible
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Populate the centerPanel with keycards
        populateCenterPanelWithKeyCards();
    }

    private void populateCenterPanelWithKeyCards() {
        if (keyCards != null) {
            JPanel centerPanel = (JPanel) getContentPane().getComponent(1); // Get the centerPanel
            centerPanel.removeAll(); // Clear existing components
            for (KeyCard card : keyCards) {
                JButton cardButton = new JButton(card.getCardWord());
                centerPanel.add(cardButton);
                // Add action listener to handle card clicks
                cardButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle card click logic
                        JOptionPane.showMessageDialog(CodenameFrame.this, "Card Clicked: " + card.getCardWord());
                    }
                });
            }
            centerPanel.revalidate(); // Refresh the panel to reflect changes
        }
    }

    private void deleteRoom() {
        // Perform deletion operation in the database or any other cleanup
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:CodenameDB.sqlite")) {
            String deleteRoomQuery = "DELETE FROM Rooms WHERE RoomID = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteRoomQuery)) {
                statement.setInt(1, roomIdToDelete);
                statement.executeUpdate();
                System.out.println("Room deleted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete room: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        int roomIdToDelete = 1; // Replace 1 with the actual room ID to delete
        List<KeyCard> keyCards = null; // Replace null with the actual list of keycards
        SwingUtilities.invokeLater(() -> new CodenameFrame(roomIdToDelete, keyCards));
    }
}
