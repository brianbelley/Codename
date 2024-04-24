package org.example.model;

import org.example.dataaccess.DB;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class Record {
    private int id;
    private int roundNumber;
    private int scoreNumber;
    private String result;

    public Record(int roundNumber, int scoreNumber, String result) {
        this.roundNumber = roundNumber;
        this.scoreNumber = scoreNumber;
        this.result = result;
    }

    public Record(int id, int scoreNumber, int roundNumber, String result) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.scoreNumber = scoreNumber;
        this.result = result;
    }

    public Record() {
        this.roundNumber = 0;
        this.scoreNumber = 0;
        this.result = "";
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for roundNumber, scoreNumber, and result
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getScoreNumber() {
        return scoreNumber;
    }

    public void setScoreNumber(int scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // toString method for printing record details
    @Override
    public String toString() {
        return "Record{" +
                "roundNumber=" + roundNumber +
                ", scoreNumber=" + scoreNumber +
                ", result='" + result + '\'' +
                '}';
    }

    // List records for a given username
    public static List<Record> listRecords(String username) {
        try {
            return DB.listRecords(username);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to load records: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }


    // Method to delete a record
    public static void deleteRecord(int id) {
        try {
            DB.deleteRecord(id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to delete record: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}

