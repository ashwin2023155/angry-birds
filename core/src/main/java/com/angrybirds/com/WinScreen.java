package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

// First, create the WinScreen class
public class WinScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture winScreenTexture;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public WinScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameScreen.VIRTUAL_WIDTH, GameScreen.VIRTUAL_HEIGHT, camera);
        camera.position.set(GameScreen.VIRTUAL_WIDTH / 2, GameScreen.VIRTUAL_HEIGHT / 2, 0);

        winScreenTexture = new Texture("winscreen.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(winScreenTexture, 0, 0, GameScreen.VIRTUAL_WIDTH, GameScreen.VIRTUAL_HEIGHT);
        batch.end();

        // Return to main menu on any key press
        if (Gdx.input.isKeyJustPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        winScreenTexture.dispose();
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
