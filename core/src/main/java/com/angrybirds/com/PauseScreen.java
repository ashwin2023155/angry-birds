package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseScreen implements Screen {
    private Main game;
    private Texture pauseScreenTexture;
    private Texture closeButtonTexture;
    private Texture giveUpButtonTexture;
    private SpriteBatch batch;
    private Stage stage;
    private ImageButton closeButton;
    private ImageButton giveUpButton;

    // Constants for virtual screen size
    private float virtualWidth = 1920;
    private float virtualHeight = 1080;
    private Viewport viewport;

    // Constants for button positioning
    private static final float BUTTON_SIZE = 140f; // Default button size for close button
    private static final float GIVE_UP_BUTTON_SIZE = 50f; // Set the size for give up button to 500f
    private static final float BUTTON_X_OFFSET = 0.805f; // Percentage from left for close button
    private static final float BUTTON_Y_OFFSET = 0.832f; // Percentage from bottom for close button

    // Constants for give up button positioning
    private float giveUpXOffset = 0.700f; // Percentage from left for give up button
    private float giveUpYOffset = 0.1f; // Percentage from bottom for give up button

    public PauseScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();

        // Initialize viewport
        viewport = new FitViewport(virtualWidth, virtualHeight);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Create stage with viewport
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures
        pauseScreenTexture = new Texture("pause_screen.png");
        pauseScreenTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        closeButtonTexture = new Texture("close_button.png");
        closeButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        giveUpButtonTexture = new Texture("give_up_button.png");
        giveUpButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Setup buttons
        setupCloseButton();
        setupGiveUpButton();
    }

    private void setupCloseButton() {
        Skin skin = new Skin();

        // Setup close button
        skin.add("closeButton", closeButtonTexture);
        closeButton = new ImageButton(skin.getDrawable("closeButton"));

        // Set button size
        closeButton.setSize(BUTTON_SIZE, BUTTON_SIZE);

        // Calculate position based on screen size and offsets
        float xPosition = virtualWidth * BUTTON_X_OFFSET;
        float yPosition = virtualHeight * BUTTON_Y_OFFSET;

        // Position the button
        closeButton.setPosition(xPosition, yPosition);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        // Add button to stage
        stage.addActor(closeButton);
    }

    private void setupGiveUpButton() {
        Skin skin = new Skin();

        // Setup give up button with scaled image
        TextureRegionDrawable giveUpDrawable = new TextureRegionDrawable(new TextureRegion(giveUpButtonTexture));
        // Scale up the drawable - increase these numbers to make button bigger
        giveUpDrawable.setMinWidth(360);  // Increase width
        giveUpDrawable.setMinHeight(170); // Increase height

        giveUpButton = new ImageButton(giveUpDrawable);

        // Center the button horizontally and position it near bottom
        float xPosition = (virtualWidth / 2) - (giveUpDrawable.getMinWidth() / 2) ;
        float yPosition = virtualHeight * 0.09f - 25;  // 10% from bottom

        giveUpButton.setPosition(xPosition, yPosition);
        giveUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(giveUpButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Draw pause screen background
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(pauseScreenTexture, 0, 0, virtualWidth, virtualHeight);
        batch.end();

        // Update and draw stage (includes buttons)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        pauseScreenTexture.dispose();
        closeButtonTexture.dispose();
        giveUpButtonTexture.dispose(); // Dispose the give up button texture
        stage.dispose();
    }

    // Required Screen interface methods
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
