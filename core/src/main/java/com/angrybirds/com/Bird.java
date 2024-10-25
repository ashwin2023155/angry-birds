package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Bird {
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float width;
    protected float height;
    protected boolean isLaunched;
    protected float rotation;

    public Bird(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        // Reduce bird size
        width = GameScreen.VIRTUAL_WIDTH * 0.04f;     // Reduced from 0.08f to 0.04f
        height = GameScreen.VIRTUAL_HEIGHT * 0.04f;   // Reduced from 0.08f to 0.04f
        isLaunched = false;
        rotation = 0;
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

    public void update(float delta) {
        if (isLaunched) {
            // Basic physics update
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            velocity.y -= 9.8f * delta; // Gravity

            // Update rotation based on velocity
            rotation = velocity.angle();
        }
    }

    public void launch(float velocityX, float velocityY) {
        isLaunched = true;
        velocity.set(velocityX, velocityY);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    // Abstract method for special ability
    public abstract void useSpecialAbility();

    public void dispose() {
        texture.dispose();
    }
}
