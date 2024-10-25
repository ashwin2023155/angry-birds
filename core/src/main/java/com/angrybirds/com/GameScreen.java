package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Stage stage;
    private ImageButton pauseButton;
    private Texture pauseButtonTexture;
    private Texture gameScreenTexture;

    // Camera and viewport for maintaining aspect ratio
    private OrthographicCamera camera;
    private Viewport viewport;

    // Constants for virtual screen size
    public static final float VIRTUAL_WIDTH = 1280;
    public static final float VIRTUAL_HEIGHT = 720;

    // Game objects
    private Catapult catapult;
    private RedBird redBird;
    private BlueBird blueBird;
    private YellowBird yellowBird;
    private Bird currentBird;

    private WoodBlock woodBlock;
    private GlassBlock glassBlock;
    private StoneBlock stoneBlock;

    private SmallPig smallPig;
    private MediumPig mediumPig;

    public GameScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();

        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);

        // Create stage with viewport
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load all textures
        loadTextures();

        // Initialize game objects
        initializeGameObjects();

        // Setup UI buttons
        setupButtons();
    }

    private void loadTextures() {
        // Load and configure all textures
        gameScreenTexture = new Texture("play_game.png");
        gameScreenTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        pauseButtonTexture = new Texture("pause_button.png");
        pauseButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void initializeGameObjects() {
        float blockSpacing = VIRTUAL_WIDTH * 0.09f;

        // Initialize catapult
        float catapultX = VIRTUAL_WIDTH * 0.05f;
        float catapultY = VIRTUAL_HEIGHT * 0.24f;
        catapult = new Catapult(catapultX, catapultY);

        // Initialize birds
        float birdStartX = catapultX - (catapult.getWidth() * 0.4f);
        float birdY = catapultY + (catapult.getHeight() * 0.05f);

        redBird = new RedBird(birdStartX, birdY);
        blueBird = new BlueBird(birdStartX + VIRTUAL_WIDTH * 0.03f, birdY);
        yellowBird = new YellowBird(birdStartX + VIRTUAL_WIDTH * 0.06f, birdY);
        currentBird = redBird;

        // Initialize blocks and pigs
        float rightSideX = VIRTUAL_WIDTH * 0.7f;
        float groundLevel = VIRTUAL_HEIGHT * 0.24f;

        woodBlock = new WoodBlock(rightSideX, groundLevel);
        stoneBlock = new StoneBlock(rightSideX  - 20 , groundLevel + 160);
        glassBlock = new GlassBlock(rightSideX + (blockSpacing * 2), groundLevel);

        smallPig = new SmallPig(rightSideX + 50, groundLevel-10);
        mediumPig = new MediumPig(rightSideX + 150, groundLevel + 3);
    }

    private void setupButtons() {
        Skin skin = new Skin();
        float buttonSize = VIRTUAL_HEIGHT * 0.1f;
        float buttonPadding = VIRTUAL_HEIGHT * 0.02f;

        // Setup pause button
        skin.add("pauseButton", pauseButtonTexture);
        pauseButton = new ImageButton(skin.getDrawable("pauseButton"));
        pauseButton.setSize(buttonSize, buttonSize);
        pauseButton.setPosition(buttonPadding, VIRTUAL_HEIGHT - buttonSize - buttonPadding);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game));
            }
        });

        // Add pause button to stage
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            game.setScreen(new WinScreen(game));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            game.setScreen(new LoseScreen(game));
            return;
        }

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Update bird physics
        if (currentBird != null) {
            currentBird.update(delta);
        }

        batch.begin();
        // Draw background
        batch.draw(gameScreenTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Render game objects
        catapult.render(batch);
        redBird.render(batch);
        blueBird.render(batch);
        yellowBird.render(batch);

        woodBlock.render(batch);
        stoneBlock.render(batch);
        glassBlock.render(batch);

        smallPig.render(batch);
        mediumPig.render(batch);

        batch.end();

        // Update and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void dispose() {
        gameScreenTexture.dispose();
        pauseButtonTexture.dispose();
        stage.dispose();
        catapult.dispose();
        redBird.dispose();
        blueBird.dispose();
        yellowBird.dispose();
        woodBlock.dispose();
        glassBlock.dispose();
        stoneBlock.dispose();
        smallPig.dispose();
        mediumPig.dispose();
    }

    // Utility methods
    public float getWorldX(float screenX) {
        return (screenX / Gdx.graphics.getWidth()) * VIRTUAL_WIDTH;
    }

    public float getWorldY(float screenY) {
        return (screenY / Gdx.graphics.getHeight()) * VIRTUAL_HEIGHT;
    }

    // Bird control methods
    public void launchCurrentBird(float velocityX, float velocityY) {
        if (currentBird != null && !currentBird.isLaunched) {
            float scaledVelocityX = velocityX * (VIRTUAL_WIDTH / Gdx.graphics.getWidth());
            float scaledVelocityY = velocityY * (VIRTUAL_HEIGHT / Gdx.graphics.getHeight());
            currentBird.launch(scaledVelocityX, scaledVelocityY);
        }
    }

    public void activateSpecialAbility() {
        if (currentBird != null) {
            currentBird.useSpecialAbility();
        }
    }

    public void switchToNextBird() {
        if (currentBird == redBird) {
            currentBird = blueBird;
        } else if (currentBird == blueBird) {
            currentBird = yellowBird;
        }
    }

    // Required Screen interface methods
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
