// GlassBlock.java
package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;

class GlassBlock extends Block {
    private static final float GLASS_HEALTH = 50;

    public GlassBlock(World world, float x, float y) {
        super(world, x, y, 50, 200, "glass_block.png", GLASS_HEALTH);
    }
}
