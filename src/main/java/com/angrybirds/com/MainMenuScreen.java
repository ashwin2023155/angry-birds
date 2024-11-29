package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture crossButtonTexture;
    private OrthographicCamera camera;

    // Constants for button positioning and sizing
    private static final float PLAY_BUTTON_BOTTOM_PADDING = -30f;
    private static final float PLAY_BUTTON_WIDTH = 200f;
    private static final float PLAY_BUTTON_HEIGHT = 200f;

    private static final float CROSS_BUTTON_WIDTH = 50f;
    private static final float CROSS_BUTTON_HEIGHT = 50f;
    private static final float CROSS_BUTTON_PADDING = 20f; // Padding from top and right edges

    public MainMenuScreen(Main game) {
        this.game = game;

        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // Initialize stage with ScreenViewport for better scaling
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture("homepage.png");
        playButtonTexture = new Texture("play_button.png");
        crossButtonTexture = new Texture("close_button.png");  // Make sure to add this to your assets

        // Create and position the play button
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playButtonTexture));
        playButton.setSize(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        playButton.setPosition(
            (Gdx.graphics.getWidth() - PLAY_BUTTON_WIDTH) / 2,
            PLAY_BUTTON_BOTTOM_PADDING
        );

        // Create and position the cross button
        ImageButton crossButton = new ImageButton(new TextureRegionDrawable(crossButtonTexture));
        crossButton.setSize(CROSS_BUTTON_WIDTH, CROSS_BUTTON_HEIGHT);
        crossButton.setPosition(
            Gdx.graphics.getWidth() - CROSS_BUTTON_WIDTH - CROSS_BUTTON_PADDING,
            Gdx.graphics.getHeight() - CROSS_BUTTON_HEIGHT - CROSS_BUTTON_PADDING
        );

        // Add click listener to the play button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectionScreen(game,true));
            }
        });

        // Add click listener to the cross button
        crossButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit the application
            }
        });

        // Add both buttons to the stage
        stage.addActor(playButton);
        stage.addActor(crossButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        // Draw background
        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);

        // Reposition both buttons
        ImageButton playButton = (ImageButton) stage.getActors().get(0);
        ImageButton crossButton = (ImageButton) stage.getActors().get(1);

        // Update play button position
        playButton.setPosition(
            (width - PLAY_BUTTON_WIDTH) / 2,
            PLAY_BUTTON_BOTTOM_PADDING
        );

        // Update cross button position
        crossButton.setPosition(
            width - CROSS_BUTTON_WIDTH - CROSS_BUTTON_PADDING,
            height - CROSS_BUTTON_HEIGHT - CROSS_BUTTON_PADDING
        );
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        crossButtonTexture.dispose();
    }

    // Required methods
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
