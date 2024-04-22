package org.example;

import org.example.model.Player;
import org.example.model.Record;
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

    private String username;
    private int roomIdToDelete; // Variable to store the room ID to delete
    private List<KeyCard> keyCards; // List to store keycards
    private Player redOperator;
    private Player blueOperator;
    private boolean isRedSpymasterTurn = true;
    private boolean isRedOperatorTurn = false;
    private boolean isBlueSpymasterTurn = false;
    private boolean isBlueOperatorTurn = false;
    private JTextField clueText;
    private JTextField numberTextField;
    private JLabel clueLabel;
    private JLabel numberLabel;
    private JButton revealCardButton;
    private int currentTurnIndex = 0;
    private final String[] turnOrder = {"RED_SPYMASTER", "RED_OPERATOR", "BLUE_SPYMASTER", "BLUE_OPERATOR"};
    private JLabel scoreLabel;
    private GameState gameState;
    private JLabel turnLabel;

    private int turnCounter = 0;
    private int redScore = 8;
    private int blueScore = 8;


    public CodenameFrame(String username,int roomIdToDelete, List<KeyCard> keyCards) {
        this.username = username;
        this.roomIdToDelete = roomIdToDelete; // Store the room ID to delete
        this.keyCards = keyCards; // Store the keycards

        gameState = new GameState(0, 16, "Ongoing");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add components
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Codename Game");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Create and initialize the score label
        scoreLabel = new JLabel("Red Score: " + redScore + "   Blue Score: " + blueScore);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        // Add label indicating whose turn it is
        turnLabel = new JLabel("It's Red SpyMaster's turn.");
        turnLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(turnLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(5, 5)); // Example layout
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 2));
        JButton endTurnButton = new JButton("End Turn");
        revealCardButton = new JButton("Reveal Card");
        JButton revealClueButton = new JButton("Reveal Clue");
        clueLabel = new JLabel("Clue:");
        clueText = new JTextField();
        numberLabel = new JLabel("Number:");
        numberTextField = new JTextField();

        bottomPanel.add(clueLabel);
        bottomPanel.add(clueText);
        bottomPanel.add(numberLabel);
        bottomPanel.add(numberTextField);
        bottomPanel.add(revealCardButton);
        bottomPanel.add(endTurnButton);
        bottomPanel.add(revealClueButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Hide blue label, text field, and number label initially
        clueLabel.setVisible(isRedSpymasterTurn || isBlueSpymasterTurn);
        clueText.setVisible(isRedSpymasterTurn || isBlueSpymasterTurn);
        numberLabel.setVisible(isRedSpymasterTurn || isBlueSpymasterTurn);
        numberTextField.setVisible(isRedSpymasterTurn || isBlueSpymasterTurn);



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


        // Reveal Clue button action listener
        revealClueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRevealClue();
            }
        });

        // Reveal Card button action listener
        revealCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRevealCard();
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
                makeButtonRound(cardButton); // Make the button round

                // Set color based on card type for spymaster's turn
                if (isRedOperatorTurn || isBlueOperatorTurn) {
                    switch (card.getCardType()) {
                        case RED:
                            cardButton.setBackground(Color.RED);
                            break;
                        case BLUE:
                            cardButton.setBackground(Color.BLUE);
                            break;
                        case NEUTRAL:
                            cardButton.setBackground(Color.WHITE);
                            break;
                        case ASSASSIN:
                            cardButton.setBackground(Color.BLACK);
                            break;
                    }
                } else {
                    // Set default color for operator's turn
                    cardButton.setBackground(Color.PINK); // Set default color to light gray for operator's turn
                    cardButton.setForeground(Color.BLACK);
                }

                centerPanel.add(cardButton);
                // Add action listener to handle card clicks
                cardButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle card click logic
                        handleCardClick(card);
                    }
                });
            }
            centerPanel.revalidate(); // Refresh the panel to reflect changes
        }
    }

    private void makeButtonRound(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Adjust padding as needed
        button.setPreferredSize(new Dimension(100, 100)); // Adjust size as needed
        button.setBackground(Color.WHITE); // Set background color
        button.setForeground(Color.GRAY); // Set text color
        button.setFont(new Font("Arial", Font.BOLD, 10)); // Set font
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor
        button.setMargin(new Insets(10, 10, 10, 10)); // Set margin
    }


    private void handleCardClick(KeyCard clickedCard) {
        // Check if it's the Spymaster's turn
        if (!isSpymasterTurn()) {
            // Update the card's state to revealed
            clickedCard.setRevealed(true);

            // Update the card colors
            populateCenterPanelWithKeyCards();

            // Increment turn counter
            turnCounter++;

            // Decrement the score if the operator clicks on the right keycard
            if (isOperatorTurn(clickedCard)) {
                if (clickedCard.getCardType() == CardType.RED) {
                    redScore--; // Decrease red team's score
                } else if (clickedCard.getCardType() == CardType.BLUE) {
                    blueScore--; // Decrease blue team's score
                }
                // Update the score label text
                scoreLabel.setText("Red Score: " + redScore + "   Blue Score: " + blueScore);
                // Change text color to yellow for Spymasters
                clueLabel.setForeground(Color.YELLOW);
                clueText.setForeground(Color.YELLOW);
            }

            // Check for win condition
            boolean redTeamWin = true;
            boolean blueTeamWin = true;
            boolean assassinRevealed = false;
            for (KeyCard card : keyCards) {
                if (!card.isRevealed()) {
                    if (card.getCardType() == CardType.RED) {
                        blueTeamWin = false;
                    } else if (card.getCardType() == CardType.BLUE) {
                        redTeamWin = false;
                    }
                } else if (card.getCardType() == CardType.ASSASSIN) {
                    assassinRevealed = true;
                }
            }

            // Save game state
            String status;
            if (redTeamWin) {
                // Display message indicating the red team wins
                JOptionPane.showMessageDialog(this, "Red team wins! All red agents have been identified.");
                status = "Red team wins";

                gameState.saveGameState(turnCounter,redScore+blueScore,status);

                // Create a record instance and save it
                Record record = new Record(gameState.getRoundNumber(), gameState.getScoreNumber(), gameState.getStatus());
                User user = new User(username,record);
                user.saveRecord(user);

                new MenuFrame(username);

                deleteRoom();
                dispose();

            } else if (blueTeamWin) {
                // Display message indicating the blue team wins
                JOptionPane.showMessageDialog(this, "Blue team wins! All blue agents have been identified.");
                status = "Blue team wins";

                gameState.saveGameState(turnCounter,redScore+blueScore,status);

                // Create a record instance and save it
                Record record = new Record(gameState.getRoundNumber(), gameState.getScoreNumber(), gameState.getStatus());
                User user = new User(username,record);
                user.saveRecord(user);

                new MenuFrame(username);

                deleteRoom();
                dispose();

            } else if (assassinRevealed) {
                // Display message indicating the assassin was revealed, end the game
                JOptionPane.showMessageDialog(this, "Assassin revealed! The opposing team wins.");
                status = "Assassin revealed";

                gameState.saveGameState(turnCounter,redScore+blueScore,status);

                // Create a record instance and save it
                Record record = new Record(gameState.getRoundNumber(), gameState.getScoreNumber(), gameState.getStatus());
                User user = new User(username,record);
                user.saveRecord(user);

                new MenuFrame(username);

                deleteRoom();
                dispose();

            } else {
                status = "Ongoing";
            }
            gameState.saveGameState(turnCounter, redScore + blueScore, status);


            // Switch turns
            endTurn();
        } else {
            // Display message indicating only the Operator can reveal cards
            JOptionPane.showMessageDialog(this, "Only the Operator can reveal cards.");
        }
    }






    private boolean isSpymasterTurn() {
        return !turnOrder[currentTurnIndex].equals("RED_OPERATOR") && !turnOrder[currentTurnIndex].equals("BLUE_OPERATOR");
    }

    private boolean isOperatorTurn(KeyCard clickedCard) {
        return turnOrder[currentTurnIndex].equals("RED_OPERATOR") && clickedCard.getCardType() != CardType.BLUE ||
                turnOrder[currentTurnIndex].equals("BLUE_OPERATOR") && clickedCard.getCardType() != CardType.RED;
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

    // Inside the handleRevealClue method
    private void handleRevealClue() {
        // Check if it's the Spymaster's turn
        if (!isRedSpymasterTurn || !isBlueSpymasterTurn) {
            // Logic for revealing clue
            String clue = clueText.getText();
            String number = numberTextField.getText();

            // Display clue
            JOptionPane.showMessageDialog(this, "Clue: " + clue + "\nNumber: " + number);

        } else {
            // Display message indicating only the Operator can reveal clues
            JOptionPane.showMessageDialog(this, "Only the Operator can reveal clues.");
        }
    }

    // Inside the handleRevealCard method
    private void handleRevealCard() {
        // Check if it's the Spymaster's turn
        if (isRedOperatorTurn || isBlueOperatorTurn) {
            // Logic for revealing card
            // Implement the logic here
        } else {
            // Display message indicating only the Spymaster can reveal cards
            JOptionPane.showMessageDialog(this, "Only the Spymaster can reveal cards.");
        }
    }




    private void endTurn() {
        // Update the card colors
        populateCenterPanelWithKeyCards();

        // Hide or show clue-related components based on the current turn
        clueLabel.setVisible(!isRedSpymasterTurn && !isBlueSpymasterTurn);
        clueText.setVisible(!isRedSpymasterTurn && !isBlueSpymasterTurn);
        numberLabel.setVisible(!isRedSpymasterTurn && !isBlueSpymasterTurn);
        numberTextField.setVisible(!isRedSpymasterTurn && !isBlueSpymasterTurn);
        revealCardButton.setVisible(!isRedSpymasterTurn && !isBlueSpymasterTurn);





        // Disable keycards during Spymaster's turn
        JPanel centerPanel = (JPanel) getContentPane().getComponent(1);
        Component[] components = centerPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setEnabled(!isRedOperatorTurn || !isBlueOperatorTurn);
            }
        }

        // Update the current turn index and turn variables
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.length;
        updateTurnVariables();


        // Clear clue text field if it's the operator's turn
        if (isRedSpymasterTurn || isBlueSpymasterTurn) {
            clear();
        }

        // Display message indicating the current turn
        JOptionPane.showMessageDialog(this, "It's now " + turnOrder[currentTurnIndex] + "'s turn.");

        turnLabel.setText("It's now " + turnOrder[currentTurnIndex] + "'s turn.");
    }

    private void updateTurnVariables() {
        isRedSpymasterTurn = turnOrder[currentTurnIndex].equals("RED_SPYMASTER");
        isRedOperatorTurn = turnOrder[currentTurnIndex].equals("RED_OPERATOR");
        isBlueSpymasterTurn = turnOrder[currentTurnIndex].equals("BLUE_SPYMASTER");
        isBlueOperatorTurn = turnOrder[currentTurnIndex].equals("BLUE_OPERATOR");
    }

    public void clear(){
        clueText.setText("");
        numberTextField.setText("");
    }

}