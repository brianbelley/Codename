package org.example.model;
public class Record {
    private int roundNumber;
    private int scoreNumber;
    private String result;

    public Record(int roundNumber, int scoreNumber, String result) {
        this.roundNumber = roundNumber;
        this.scoreNumber = scoreNumber;
        this.result = result;
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
}

