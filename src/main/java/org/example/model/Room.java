package org.example.model;

public class Room {
    private int roomId;
    private int roomCode;
    private Difficulty difficulty;
    private int roomSize;
    private Player[] players;

    public Room(int roomId, int roomCode, Difficulty difficulty, int roomSize, Player[] players) {
        this.roomId = roomId;
        this.roomCode = roomCode;
        this.difficulty = difficulty;
        this.roomSize = roomSize;
        this.players = players;
    }

    // Getters and setters for roomId, roomCode, difficulty, roomSize, and players
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}

