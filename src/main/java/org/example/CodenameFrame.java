package org.example;

import org.example.model.Player;
import org.example.model.Roles;
import org.example.model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import org.example.model.*;

public class CodenameFrame extends JFrame {

    private int roomIdToDelete; // Variable to store the room ID to delete
    private List<KeyCard> keyCards; // List to store keycards
    private Player redOperator;
    private Player blueOperator;
    private boolean isRedTurn = true; // Boolean to track whose turn it is
    private boolean isRedSpymasterTurn = true;
    private boolean isRedOperatorTurn = false;
    private boolean isBlueSpymasterTurn = false;
    private boolean isBlueOperatorTurn = false;

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
                endTurn();
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

        // Assign roles
        assignRoles();
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

    private void assignRoles() {
        // Randomly assign roles to players
        Roles redRole = Math.random() < 0.5 ? Roles.SPYMASTER : Roles.OPERATOR;
        Team redTeam = redRole == Roles.SPYMASTER ? Team.RED : Team.BLUE;

        Roles blueRole = redRole == Roles.SPYMASTER ? Roles.OPERATOR : Roles.SPYMASTER;
        Team blueTeam = blueRole == Roles.SPYMASTER ? Team.RED : Team.BLUE;

        redOperator = new Player(redRole, redTeam, null);
        blueOperator = new Player(blueRole, blueTeam, null);


        // Start with red's turn
        endTurn();
    }


    private void endTurn() {
        String message;

        if (isRedSpymasterTurn) {
            message = "It's now the Red Spymaster's turn.";
            isRedSpymasterTurn = false;
            isRedOperatorTurn = true;
        } else if (isRedOperatorTurn) {
            message = "It's now the Red Operator's turn.";
            isRedOperatorTurn = false;
            isBlueSpymasterTurn = true;
        } else if (isBlueSpymasterTurn) {
            message = "It's now the Blue Spymaster's turn.";
            isBlueSpymasterTurn = false;
            isBlueOperatorTurn = true;
        } else {
            message = "It's now the Blue Operator's turn.";
            isBlueOperatorTurn = false;
            isRedSpymasterTurn = true; // Start over with Red Spymaster
        }

        JOptionPane.showMessageDialog(this, message);
    }


    public static void main(String[] args) {
        int roomIdToDelete = 1; // Replace 1 with the actual room ID to delete
        List<KeyCard> keyCards = null; // Replace null with the actual list of keycards
        SwingUtilities.invokeLater(() -> new CodenameFrame(roomIdToDelete, keyCards));
    }
}