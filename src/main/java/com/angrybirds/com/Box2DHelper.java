package com.angrybirds.com;

import com.badlogic.gdx.math.Vector2;

public class Box2DHelper {
    public static final float PIXELS_TO_METERS = 100f;

    // Convert screen coordinates to Box2D world coordinates
    public static float toBox2D(float pixels) {
        return pixels / PIXELS_TO_METERS;
    }

    // Convert Box2D world coordinates to screen coordinates
    public static float toPixels(float meters) {
        return meters * PIXELS_TO_METERS;
    }

    // Convert Vector2 from pixels to Box2D coordinates
    public static Vector2 toBox2D(Vector2 pixelCoords) {
        return new Vector2(toBox2D(pixelCoords.x), toBox2D(pixelCoords.y));
    }

    // Convert Vector2 from Box2D to pixel coordinates
    public static Vector2 toPixels(Vector2 meterCoords) {
        return new Vector2(toPixels(meterCoords.x), toPixels(meterCoords.y));
    }
}
