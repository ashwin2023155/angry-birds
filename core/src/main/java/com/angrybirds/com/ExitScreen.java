package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ExitScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture backgroundTexture;
    private OrthographicCamera camera;

    public ExitScreen(Main game) {
        this.game = game;

        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the exit screen background image
        backgroundTexture = new Texture("exit.webp");

        // Create UI elements
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton confirmExitButton = new TextButton("Confirm Exit", skin);
        TextButton returnButton = new TextButton("Return to Menu", skin);

        // Create table for button layout
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add("Are you sure you want to exit?").padBottom(50).row();
        table.add(confirmExitButton).padBottom(20).row();
        table.add(returnButton);

        stage.addActor(table);

        // Add click listeners
        confirmExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Actually exit the game
            }
        });

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));  // Return to main menu
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        // Draw background
        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.getBatch().end();

        // Draw UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
