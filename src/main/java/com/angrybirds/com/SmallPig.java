package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;
class SmallPig extends Pig {
    private static final float SMALL_PIG_HEALTH = 20;  // Updated health value

    public SmallPig(World world, float x, float y) {
        super(world, x, y, 70, 70, "small_pig.png", SMALL_PIG_HEALTH,1);
    }
}
