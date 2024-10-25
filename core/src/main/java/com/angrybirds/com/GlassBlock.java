package com.angrybirds.com;

class GlassBlock extends Block {
    private static final float GLASS_HEALTH = 50;

    public GlassBlock(float x, float y) {
        super(x, y, 50, 200, "glass_block.png", GLASS_HEALTH);
    }
}
