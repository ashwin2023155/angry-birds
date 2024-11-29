// RedBird.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    public RedBird(World world, float x, float y) {
        super(world, "red_bird.png", x, y);
    }

    @Override
    public void useSpecialAbility() {
        // Red bird has no special ability, but hits harder
        // You can implement specific collision damage here
    }
    @Override
    public void render(SpriteBatch batch) {
        if (texture != null && body != null) {
            Vector2 position = getPosition();
            float angle = (float) Math.toDegrees(body.getAngle());
            float drawWidth = width * 1; // Scale x set to 1
            float drawHeight = height * 1; // Scale y set to 1

            batch.draw(texture,
                position.x - drawWidth / 2,
                position.y - drawHeight / 2,
                drawWidth / 2, drawHeight / 2,
                drawWidth, drawHeight,
                1, 1, // Scale x and y set to 1
                angle,
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }
}
