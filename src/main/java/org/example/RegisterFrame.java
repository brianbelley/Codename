package org.example;

import org.example.dataaccess.LoginService;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JTextField passwordField;

    private LoginFrame loginFrame;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Labels
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        // TextFields
        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        // Button
        JButton registerButton = new JButton("Register");

        // Add labels and text fields to the frame
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);
        gbc.gridy = 1;
        add(emailLabel, gbc);
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow text fields to grow horizontally
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        add(usernameField, gbc);
        gbc.gridy = 1;
        add(emailField, gbc);
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Register button action listener
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Set size
        setSize(400, 200);

        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim(); // Remove leading and trailing spaces
        String email = emailField.getText().trim(); // Remove leading and trailing spaces
        String password = passwordField.getText();

        // Check if any of the fields are empty or contain only spaces
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return; // Exit the method without registering the user
        }

        // Create a new User object
        User newUser = new User(username, email, password);

        // Register the user using the LoginService
        LoginService.registerUser(newUser);
        dispose(); // Close the RegisterFrame
        loginFrame.setVisible(true); // Show the LoginFrame
    }
}


