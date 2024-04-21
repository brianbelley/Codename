package org.example;

import org.example.dataaccess.FileManagement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TemporaryFrame extends JFrame {

    private JComboBox<String> difficultyComboBox;
    private JTextArea wordTextArea;

    public TemporaryFrame() {
        setTitle("Temporary Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for difficulty selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        difficultyComboBox = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
        topPanel.add(difficultyLabel);
        topPanel.add(difficultyComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Panel for displaying words
        JPanel centerPanel = new JPanel(new BorderLayout());
        wordTextArea = new JTextArea();
        wordTextArea.setEditable(false);
        centerPanel.add(new JScrollPane(wordTextArea), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Button to fetch words
        JButton fetchWordsButton = new JButton("Fetch Words");
        fetchWordsButton.addActionListener(e -> fetchWords());
        add(fetchWordsButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchWords() {
        // Get the selected difficulty
        String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();

        // Retrieve words based on the selected difficulty
        List<String> words = FileManagement.getWordsByDifficulty("path/to/words.json", selectedDifficulty);

        // Display the words
        StringBuilder wordList = new StringBuilder();
        for (String word : words) {
            wordList.append(word).append("\n");
        }
        wordTextArea.setText(wordList.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TemporaryFrame::new);
    }
}
