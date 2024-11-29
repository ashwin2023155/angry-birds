package com.angrybirds.com;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TrajectoryActor extends Actor {
    private Sprite trajectorySprite;
    public Vector2 startPosition;
    public Vector2 velocity;
    private static final float TIME_STEP = 0.1f;

    public TrajectoryActor(Sprite trajectorySprite) {
        this.trajectorySprite = trajectorySprite;
        this.startPosition = new Vector2();
        this.velocity = new Vector2();
    }

    // Set trajectory data (start position and velocity)
    public void setTrajectoryData(Vector2 startPosition, Vector2 velocity) {
        this.startPosition.set(startPosition);
        this.velocity.set(velocity);
    }

    @Override
    public void act(float delta) {
        // You can add logic for drawing the trajectory path here, for now we just update the position of the sprite
        float totalTime = 0f;
        while (totalTime <= 2f) { // Adjust how long you want to simulate the trajectory
            float x = startPosition.x + velocity.x * totalTime;
            float y = startPosition.y + velocity.y * totalTime - (0.5f * 9.8f * totalTime * totalTime); // Considering gravity
            trajectorySprite.setPosition(x, y);
            totalTime += TIME_STEP;
        }
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        trajectorySprite.draw(batch);
    }
}
