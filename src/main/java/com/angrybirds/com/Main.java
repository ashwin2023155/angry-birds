package com.angrybirds.com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;

public class Main extends Game {
    private SpriteBatch batch;
    private GameScreen currentGameScreen;
    private MusicManager musicManager;

    @Override
    public void create() {
        Box2D.init();
        batch = new SpriteBatch();

        // Initialize and play background music
        musicManager = new MusicManager();
        musicManager.playBackgroundMusic();

        if (Gdx.files.local("gamestate.ser").exists()) {
            setScreen(new LevelSelectionScreen(this, true)); // Pass true to indicate saved game exists
        } else {
            setScreen(new MainMenuScreen(this));
        }
    }

    public void setCurrentGameScreen(GameScreen screen) {
        this.currentGameScreen = screen;
    }

    public GameScreen getCurrentGameScreen() {
        return currentGameScreen;
    }

    @Override
    public void render() {
        super.render(); // Renders the current screen
    }

    @Override
    public void dispose() {
        if (screen instanceof GameScreen) {
            ((GameScreen)screen).saveGameState();
        }
        batch.dispose();
        musicManager.dispose();
    }

    @Override
    public void pause() {
        if (screen instanceof GameScreen) {
            ((GameScreen)screen).saveGameState();
        }
        musicManager.pauseBackgroundMusic();
    }

    @Override
    public void resume() {
        super.resume();
        musicManager.playBackgroundMusic();
    }

    // Getter for the batch
    public SpriteBatch getBatch() {
        return batch;
    }
}
