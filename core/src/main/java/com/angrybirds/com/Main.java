package com.angrybirds.com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));  // Starts with the Main Menu Screen
    }

    @Override
    public void render() {
        super.render(); // Renders the current screen
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    // Getter for the batch
    public SpriteBatch getBatch() {
        return batch;
    }
}
