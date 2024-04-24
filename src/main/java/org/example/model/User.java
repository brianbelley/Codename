package org.example.model;
import org.example.dataaccess.DB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.example.dataaccess.DatabaseConnection.getConnection;

public class User {
    private String username;
    private String email;
    private String password;
    private Record record;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.record = new Record();
    }

    public User(String username, Record record) {
        this.username = username;
        this.record = record;
    }

    // Getters and setters for username, email, password, and record
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    // toString method for printing user details
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", record=" + record +
                '}';
    }

    public void saveRecord(User user) {
        DB.saveRecord(user.getUsername(), record.getRoundNumber(), record.getScoreNumber(), record.getResult());
    }




}

