package com.angrybirds.com;

class SmallPig extends Pig {
    private static final float SMALL_PIG_HEALTH = 50;

    public SmallPig(float x, float y) {
        super(x, y, 70, 70, "small_pig.png", SMALL_PIG_HEALTH);
    }
}
