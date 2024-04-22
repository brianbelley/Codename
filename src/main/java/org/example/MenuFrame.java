package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    private String username;

    public MenuFrame(String username) {
        this.username = username;
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, with gaps of 10 pixels
        setPreferredSize(new Dimension(300, 200));

        // Create a JLabel to display the greeting message
        JLabel greetingLabel = new JLabel("Hello, " + username);
        greetingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(greetingLabel);

        // Create a button to open RoomFrame
        JButton openRoomButton = new JButton("Open Room");
        openRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRoomFrame();
            }
        });
        add(openRoomButton);

        // Create a button to open RecordFrame
        JButton openRecordButton = new JButton("Open Record");
        openRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRecordFrame();
            }
        });
        add(openRecordButton);

        pack(); // Adjusts the size of the frame based on its contents
        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);
    }

    private void openRoomFrame() {
        // Open the RoomFrame with the username
        new RoomFrame(username);
        dispose(); // Close the MenuFrame
    }

    private void openRecordFrame() {
        // Open the RecordFrame
        new RecordFrame(username);
        dispose(); // Close the MenuFrame
    }


}

