package com.angrybirds.com;

public class RedBird extends Bird {
    public RedBird(float x, float y) {
        super("red_bird.png", x, y);
    }

    @Override
    public void useSpecialAbility() {
        // Red bird has no special ability, but hits harder
        // You can implement specific collision damage here
    }
}
