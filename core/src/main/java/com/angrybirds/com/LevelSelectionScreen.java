package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LevelSelectionScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture levelScreenTexture;
    private Texture backButtonTexture;
    private Texture level1ButtonTexture;
    private Stage stage;
    private OrthographicCamera camera;

    // Original screen dimensions
    private static final float ORIGINAL_WIDTH = 1280f;
    private static final float ORIGINAL_HEIGHT = 720f;

    // Fixed button positions and sizes
    private static final float BACK_BUTTON_X = 0f;
    private static final float BACK_BUTTON_Y = ORIGINAL_HEIGHT - 720f;
    private static final float BACK_BUTTON_SIZE = 72f;

    private static final float LEVEL1_BUTTON_X = 40f;
    private static final float LEVEL1_BUTTON_Y = 550f;
    private static final float LEVEL1_BUTTON_SIZE = 450f;


    public LevelSelectionScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();

        // Initialize camera with original dimensions
        camera = new OrthographicCamera();
        camera.setToOrtho(false, ORIGINAL_WIDTH, ORIGINAL_HEIGHT);

        // Use FitViewport to maintain aspect ratio and fixed coordinates
        stage = new Stage(new FitViewport(ORIGINAL_WIDTH, ORIGINAL_HEIGHT), batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures
        levelScreenTexture = new Texture("l.jpg");
        backButtonTexture = new Texture("back_button.png");
        level1ButtonTexture = new Texture("level1_button.png");

        // Create back button with fixed position and size
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);
        backButton.setPosition(BACK_BUTTON_X, BACK_BUTTON_Y);

        // Create level 1 button with fixed position and size
        ImageButton level1Button = new ImageButton(new TextureRegionDrawable(level1ButtonTexture));
        level1Button.setSize(LEVEL1_BUTTON_SIZE, LEVEL1_BUTTON_SIZE);
        level1Button.setPosition(LEVEL1_BUTTON_X, LEVEL1_BUTTON_Y);

        // Add click listener to back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Add click listener to level 1 button
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        // Add buttons to stage
        stage.addActor(backButton);
        stage.addActor(level1Button);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.update();
        stage.getViewport().apply();
        batch.setProjectionMatrix(camera.combined);

        // Draw the background
        batch.begin();
        batch.draw(levelScreenTexture, 0, 0, ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        batch.end();

        // Draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update viewport to maintain aspect ratio while keeping coordinates fixed
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
    }

    @Override
    public void dispose() {
        levelScreenTexture.dispose();
        backButtonTexture.dispose();
        level1ButtonTexture.dispose();
        stage.dispose();
    }

    // Required methods
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
