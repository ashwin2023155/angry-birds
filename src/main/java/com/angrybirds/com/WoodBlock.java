// WoodBlock.java
package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;

class WoodBlock extends Block {
    private static final float WOOD_HEALTH = 100;

    public WoodBlock(World world, float x, float y) {
        super(world, x, y, 50, 200, "wood_block.png", WOOD_HEALTH);
    }
}
