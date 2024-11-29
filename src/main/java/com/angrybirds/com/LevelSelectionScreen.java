package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LevelSelectionScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture levelScreenTexture;
    private Texture backButtonTexture;
    private Texture level1ButtonTexture;
    private Texture level2ButtonTexture;
    private Texture level3ButtonTexture;
    private Stage stage;
    private OrthographicCamera camera;
    private boolean hasSavedGame;
    private GameState savedState;

    // Original screen dimensions
    private static final float ORIGINAL_WIDTH = 1280f;
    private static final float ORIGINAL_HEIGHT = 720f;

    public LevelSelectionScreen(Main game,boolean checkSavedGame) {
        this.game = game;
        this.hasSavedGame = checkSavedGame;
        this.batch = game.getBatch();


        // Initialize camera with original dimensions
        camera = new OrthographicCamera();
        camera.setToOrtho(false, ORIGINAL_WIDTH, ORIGINAL_HEIGHT);



        // Use FitViewport to maintain aspect ratio and fixed coordinates
        stage = new Stage(new FitViewport(ORIGINAL_WIDTH, ORIGINAL_HEIGHT), batch);
        Gdx.input.setInputProcessor(stage);

        this.hasSavedGame = false;
        if (checkSavedGame) {
            try {
                FileInputStream fileIn = new FileInputStream("gamestate.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                savedState = (GameState) in.readObject();
                in.close();
                fileIn.close();
                this.hasSavedGame = true;
                // Create continue button at top
                ImageButton continueButton = new ImageButton(new TextureRegionDrawable(new Texture("play_button.png")));
                continueButton.setSize(2000f, 1000f);
                continueButton.setPosition(50f, ORIGINAL_HEIGHT-82);
                continueButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameScreen screen = new GameScreen(game, savedState.currentLevel);
                        screen.loadGameState();
                        game.setScreen(screen);
                    }
                });
                stage.addActor(continueButton);
            } catch (Exception e) {
                Gdx.app.error("LevelSelection", "Error loading saved game: " + e.getMessage());
                hasSavedGame = false;
            }

        }

        else {
            System.out.println("hello");
        }

        // Load textures
        levelScreenTexture = new Texture("l.jpg"); // Background image for level selection screen
        backButtonTexture = new Texture("back_button.png");
        level1ButtonTexture = new Texture("level1_button.png");
        level2ButtonTexture = new Texture("level2_button.png");
        level3ButtonTexture = new Texture("level3_button.png");

        // Back Button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(72f, 72f);
        backButton.setPosition(10f, ORIGINAL_HEIGHT - 82f); // Adjusted position
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game)); // Navigate back to main menu
            }
        });

        // Level 1 Button
        ImageButton level1Button = new ImageButton(new TextureRegionDrawable(level1ButtonTexture));
        level1Button.setSize(400f, 400f); // Width and height for the button
        level1Button.setPosition(60f, 80f); // Position for Level 1
        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen screen = new GameScreen(game, 1);
                if (hasSavedGame && savedState.currentLevel == 1) {
                    screen.loadGameState();
                }


                    game.setScreen(screen);


            }
        });

        // Level 2 Button
        ImageButton level2Button = new ImageButton(new TextureRegionDrawable(level2ButtonTexture));
        level2Button.setSize(400f, 400f);
        level2Button.setPosition(440f, 300f); // Position for Level 2
        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen screen = new GameScreen(game, 2);
                if (hasSavedGame && savedState.currentLevel == 2) {
                    screen.loadGameState();
                }
                game.setScreen(screen);
            }
        });

        // Level 3 Button
        ImageButton level3Button = new ImageButton(new TextureRegionDrawable(level3ButtonTexture));
        level3Button.setSize(400f, 400f);
        level3Button.setPosition(800f, 80f); // Position for Level 3
        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen screen = new GameScreen(game, 3);
                if (hasSavedGame && savedState.currentLevel == 3) {
                    screen.loadGameState();
                }
                game.setScreen(screen);
            }
        });

        // Add buttons to the stage
        stage.addActor(backButton);
        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
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
        batch.draw(levelScreenTexture, 0, 0, ORIGINAL_WIDTH, ORIGINAL_HEIGHT); // Ensure the background fills the screen
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
        // Dispose textures and stage to free resources
        levelScreenTexture.dispose();
        backButtonTexture.dispose();
        level1ButtonTexture.dispose();
        level2ButtonTexture.dispose();
        level3ButtonTexture.dispose();
        stage.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
