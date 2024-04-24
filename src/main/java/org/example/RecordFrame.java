package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Record;

public class RecordFrame extends JFrame {
    private String username;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    public RecordFrame(String username) {
        this.username = username;
        setTitle("Records");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a table model with columns
        String[] columnNames = {"ID", "Round Number", "Score", "Result"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create a JTable with the table model
        recordTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(recordTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load records from the database
        loadRecords();

        // Add a ListSelectionListener to the table
        recordTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = recordTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(RecordFrame.this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteRecord(id);
                        }
                    }
                }
            }
        });

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
        // Call listRecords method to get records for the user
        List<Record> records = Record.listRecords(username);

        // Check if records are retrieved successfully
        if (records != null) {
            // Populate the table model with records
            for (Record record : records) {
                Object[] rowData = {record.getId(), record.getRoundNumber(), record.getScoreNumber(), record.getResult()};
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No records found for user: " + username);
        }
    }

    private void deleteRecord(int id) {
        // Implement the delete record logic
        Record.deleteRecord(id);
        // After deleting the record, you might want to refresh the table
        tableModel.removeRow(recordTable.getSelectedRow());
    }
}

