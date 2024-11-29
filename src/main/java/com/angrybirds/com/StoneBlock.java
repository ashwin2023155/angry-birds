package com.angrybirds.com;

import com.badlogic.gdx.physics.box2d.World;

class StoneBlock extends Block {
    private static final float STONE_HEALTH = 150;

    public StoneBlock(World world, float x, float y) {
        super(world, x, y,
            GameScreen.VIRTUAL_WIDTH * 0.25f,
            GameScreen.VIRTUAL_HEIGHT * 0.12f,
            "stone_block.png",
            STONE_HEALTH);
    }}
