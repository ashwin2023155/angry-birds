// YellowBird.java
package com.angrybirds.com;

public class YellowBird extends Bird {
    private float speedMultiplier = 1.5f;
    private boolean hasSpedUp;

    public YellowBird(float x, float y) {
        super("yellow_bird.png", x, y);
        hasSpedUp = false;
    }

    @Override
    public void useSpecialAbility() {
        if (!hasSpedUp && isLaunched) {
            // Speed boost in current direction
            velocity.scl(speedMultiplier);
            hasSpedUp = true;
        }
    }
}
