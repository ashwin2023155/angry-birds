package com.angrybirds.com;

class StoneBlock extends Block {
    private static final float STONE_HEALTH = 150; // More health than wood and glass

    public StoneBlock(float x, float y) {
        // Same size as other blocks for consistency
        super(x, y,
            GameScreen.VIRTUAL_WIDTH * 0.25f,  // 15% of screen width
            GameScreen.VIRTUAL_HEIGHT * 0.12f,  // 25% of screen height
            "stone_block.png",
            STONE_HEALTH);
    }
}
