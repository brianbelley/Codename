package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Set FlatLaf dark theme
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Open the LoginFrame
        LoginFrame loginFrame = new LoginFrame();
    }
}
