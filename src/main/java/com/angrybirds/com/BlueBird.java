// BlueBird.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class BlueBird extends Bird {
    private List<BlueBird> splits;
    private boolean hasSplit;
    private World world;
    private static final float SPLIT_ANGLE = 30f; // Angle between split birds
    private static final float VELOCITY_MULTIPLIER = 1.1f; // Slight speed boost for split birds
    private static final float SPLIT_SCALE = 0.8f; // Size reduction for split birds

    public BlueBird(World world, float x, float y) {
        super(world, "blue_bird.png", x, y);
        this.world = world;
        splits = new ArrayList<>();
        hasSplit = false;
    }

    @Override
    public void useSpecialAbility() {
        if (!hasSplit && isLaunched) {
            Vector2 position = getPosition();
            Vector2 velocity = getBody().getLinearVelocity();
            float speed = velocity.len();

            // Calculate angles for the three split trajectories (-30°, 0°, 30°)
            float[] splitAngles = {-SPLIT_ANGLE, 0, SPLIT_ANGLE};

            // Create three additional blue birds
            for (int i = 0; i < 3; i++) {
                // Create new bird at current position
                BlueBird splitBird = new BlueBird(world, position.x, position.y);
                splitBird.getBody().setType(getBody().getType());

                // Convert angle to radians
                double radians = Math.toRadians(splitAngles[i]);

                // Calculate new velocity components with spread
                float newVx = (float) (velocity.x * Math.cos(radians) - velocity.y * Math.sin(radians));
                float newVy = (float) (velocity.x * Math.sin(radians) + velocity.y * Math.cos(radians));

                // Create normalized velocity vector and scale it
                Vector2 newVelocity = new Vector2(newVx, newVy).nor().scl(speed * VELOCITY_MULTIPLIER);

                // Launch the split bird
                splitBird.launch(newVelocity.x, newVelocity.y);
                splitBird.getBody().setBullet(true); // Enable continuous collision detection

                // Make split birds slightly smaller
                splitBird.setScale(SPLIT_SCALE);

                // Add to splits list
                splits.add(splitBird);
            }

            // Mark as split to prevent multiple splits
            hasSplit = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Only render the main bird if it hasn't split or is still visible
        if (!hasSplit) {
            super.render(batch);
        }
        // Render split birds if they exist
        for (BlueBird split : splits) {
            split.render(batch);
        }
    }

    @Override
    public void update(float delta) {
        // Update main bird if it hasn't split
        if (!hasSplit) {
            super.update(delta);
        }
        // Update split birds
        for (BlueBird split : splits) {
            split.update(delta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Dispose split birds
        for (BlueBird split : splits) {
            split.dispose();
        }
        splits.clear();
    }

    public boolean hasSplit() {
        return hasSplit;
    }

    public List<BlueBird> getSplits() {
        return splits;
    }
}
