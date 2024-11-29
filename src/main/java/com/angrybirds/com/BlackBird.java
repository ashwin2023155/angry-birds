package com.angrybirds.com;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BlackBird extends Bird {
    private boolean specialAbilityUsed = false;

    public BlackBird(World world, float x, float y) {
        super(world, "black_bird.png", x, y);
    }

    @Override
    public void useSpecialAbility() {
        if (!specialAbilityUsed && isLaunched) {
            specialAbilityUsed = true;
            // Screen shake is handled in GameScreen
        }
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

    @Override
    public boolean isSpecialAbilityActive() {
        return specialAbilityUsed;
    }
}
