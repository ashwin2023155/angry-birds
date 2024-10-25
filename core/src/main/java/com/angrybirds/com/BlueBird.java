// BlueBird.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class BlueBird extends Bird {
    private List<BlueBird> splits;
    private boolean hasSplit;

    public BlueBird(float x, float y) {
        super("blue_bird.png", x, y);
        splits = new ArrayList<>();
        hasSplit = false;
    }

    @Override
    public void useSpecialAbility() {
        if (!hasSplit && isLaunched) {
            // Create two additional blue birds
            for (int i = 0; i < 2; i++) {
                BlueBird splitBird = new BlueBird(position.x, position.y);
                splitBird.launch(velocity.x,
                    velocity.y + (i == 0 ? 5 : -5)); // Spread pattern
                splits.add(splitBird);
            }
            hasSplit = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        // Render split birds if they exist
        for (BlueBird split : splits) {
            split.render(batch);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Update split birds
        for (BlueBird split : splits) {
            split.update(delta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (BlueBird split : splits) {
            split.dispose();
        }
    }
}
