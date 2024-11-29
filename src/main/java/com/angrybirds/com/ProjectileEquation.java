package com.angrybirds.com;

import com.badlogic.gdx.math.Vector2;

public class ProjectileEquation {

    public float gravity;
    public Vector2 startVelocity = new Vector2();
    public Vector2 startPoint = new Vector2();

    // Calculate X position based on time t
    public float getX(float t) {
        return startVelocity.x * t + startPoint.x;
    }

    // Calculate Y position based on time t
    public float getY(float t) {
        return 0.5f * gravity * t * t + startVelocity.y * t + startPoint.y;
    }

    // Calculate the time t for a given horizontal distance (x)
    public float getTForGivenX(float x) {
        return (x - startPoint.x) / startVelocity.x;
    }
}
