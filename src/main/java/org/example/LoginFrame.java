package org.example;

import org.example.dataaccess.LoginService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Codename");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Label for "Codename"
        JLabel codenameLabel = new JLabel("Codename");
        codenameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        codenameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font and size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(codenameLabel, gbc); // Add to the top of the frame

        // Labels
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // TextFields
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        // Add labels and text fields to the frame
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow text fields to grow horizontally
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        add(usernameField, gbc);
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Add buttons to the frame
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);
        gbc.gridy = 4;
        add(loginButton, gbc);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        // Set size
        setSize(400, 300);

        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);
    }

    private void registerUser() {
        // Create a RegisterFrame instance and hide the LoginFrame
        RegisterFrame registerFrame = new RegisterFrame(this);
        this.setVisible(false);
    }

    // In the LoginFrame class
    private void loginUser() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            boolean loginSuccess = LoginService.loginUser(username, password);
            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // Open RoomFrame after successful login and pass the username
                new MenuFrame(username);
                dispose(); // Close the LoginFrame
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to login: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
