package com.angrybirds.com;

class MediumPig extends Pig {
    private static final float MEDIUM_PIG_HEALTH = 75;

    public MediumPig(float x, float y) {
        super(x, y, 80, 80, "medium_pig.png", MEDIUM_PIG_HEALTH);
    }
}
