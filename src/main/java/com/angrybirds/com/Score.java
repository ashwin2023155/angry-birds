package com.angrybirds.com;

public class Score {
    private int currentScore;

    public Score() {
        this.currentScore = 0;
    }

    public void addPoints(int points) {
        currentScore += points;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}
