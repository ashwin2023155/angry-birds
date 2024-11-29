package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
    private float speedMultiplier = 1.5f;
    private boolean hasSpedUp;

    public YellowBird(World world, float x, float y) {
        super(world, "yellow_bird.png", x, y);
        hasSpedUp = false;
    }

    @Override
    public void useSpecialAbility() {
        if (!hasSpedUp && isLaunched) {
            // Speed boost in current direction
            velocity.scl(speedMultiplier);
            body.setLinearVelocity(velocity.x, velocity.y);
            hasSpedUp = true;
        }
    }
}
