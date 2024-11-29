package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;

class MediumPig extends Pig {
    private static final float MEDIUM_PIG_HEALTH = 50;  // Updated health value

    public MediumPig(World world, float x, float y) {
        super(world, x, y, 80, 80, "medium_pig.png", MEDIUM_PIG_HEALTH,3);
    }
}
