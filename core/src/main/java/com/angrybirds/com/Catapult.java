// Catapult.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Catapult {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;
    private float rotation;

    // Catapult.java
    public Catapult(float x, float y) {
        texture = new Texture("catapult.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        position = new Vector2(x, y);
        // Increase catapult size
        width = GameScreen.VIRTUAL_WIDTH * 0.15f;    // Increased from 0.06f to 0.15f
        height = GameScreen.VIRTUAL_HEIGHT * 0.2f;   // Increased from 0.1f to 0.2f
        rotation = 0;
    }
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,
            position.x, position.y,
            width/2, height/2,    // Origin for rotation
            width, height,
            1, 1,                 // Scale
            rotation,
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setRotation(float degrees) {
        this.rotation = degrees;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        texture.dispose();
    }
}
