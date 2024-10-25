package com.angrybirds.com;

import com.angrybirds.com.Block;

class WoodBlock extends Block {
    private static final float WOOD_HEALTH = 100;

    public WoodBlock(float x, float y) {
        super(x, y, 50, 200, "wood_block.png", WOOD_HEALTH);
    }
}
