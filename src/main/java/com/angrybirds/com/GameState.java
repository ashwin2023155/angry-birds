// New class: GameState.java
package com.angrybirds.com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    public float birdVelocityX;
    public float birdVelocityY;
    public String nextBirdType;
    public int currentBirdIndex;
    public int currentLevel;
    public float[] currentBirdPosition;
    public boolean isLaunched;
    public float[] pigPositions;
    public float[] blockPositions;
    public boolean[] pigAliveStates;
    public float[] birdPositions;
    public String currentBirdType;
    public float[] blockAngles;
    public int score;

    public List<PigData> pigStates = new ArrayList<>();

    public static class PigData implements Serializable {
        public float x;
        public float y;
        public int hitcount;
        public boolean isAlive;
        public String pigType; // "SMALL" or "MEDIUM"
        public int pigNumber; // To track specific pig instances
    } // Add arrays to store remaining birds
    public boolean hasRedBird;
    public boolean hasBlueBird;
    public boolean hasYellowBird;
    public boolean hasBlueBird2;
    public boolean hasYellowBird2;
    public boolean hasRedBird2;
    public boolean hasYellowBird3;
}
