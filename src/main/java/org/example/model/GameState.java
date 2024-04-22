package org.example.model;

public class GameState {
    private int roundNumber;
    private int scoreNumber;
    private String status;

    public GameState(int roundNumber, int scoreNumber, String status) {
        this.roundNumber = roundNumber;
        this.scoreNumber = scoreNumber;
        this.status = status;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void saveGameState(int roundNumber, int scoreNumber, String status) {
        this.roundNumber = roundNumber;
        this.scoreNumber = scoreNumber;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "roundNumber=" + roundNumber +
                ", scoreNumber=" + scoreNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
