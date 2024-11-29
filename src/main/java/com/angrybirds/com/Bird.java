// Bird.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Bird {
    protected Body body;
    protected Texture texture;
    protected World world;
    public boolean isLaunched;
    protected float width = 32f;
    protected float height = 32f;
    protected float scale = 1.0f;
    protected Vector2 velocity;
    private static final float PIXELS_TO_METERS = 100f;

    public Bird(World world, String texturePath, float x, float y) {
        this.world = world;
        this.velocity = new Vector2();
        texture = new Texture(texturePath);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        width = GameScreen.VIRTUAL_WIDTH * 0.04f;
        height = GameScreen.VIRTUAL_HEIGHT * 0.04f;
        isLaunched = false;
        createBody(x, y);

    }
    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius((width/2) / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
    public void setScale(float scale) {
        this.scale = scale;
        // Update the physics body size if needed
        for (Fixture fixture : body.getFixtureList()) {
            CircleShape shape = (CircleShape) fixture.getShape();
            shape.setRadius((width * scale/2) / PIXELS_TO_METERS);
        }
    }
    public void update(float delta) {
        if (isLaunched) {
            velocity = body.getLinearVelocity();
        }
    }
    public abstract void useSpecialAbility();
    public void launch(float velocityX, float velocityY) {
        isLaunched = true;
        body.setLinearVelocity(velocityX, velocityY);
        velocity.set(velocityX, velocityY);
    }
    public void setPosition(float x, float y) {
        body.setTransform(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS, body.getAngle());
    }
    public Vector2 getPosition() {
        Vector2 position = body.getPosition();
        return new Vector2(position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
    }

    public Body getBody() {
        return body;
    }

    public void render(SpriteBatch batch) {
        if (texture != null && body != null) {
            Vector2 position = getPosition();
            float angle = (float) Math.toDegrees(body.getAngle());
            float drawWidth = width * scale;
            float drawHeight = height * scale;

            batch.draw(texture,
                position.x - drawWidth/2,
                position.y - drawHeight/2,
                drawWidth/2, drawHeight/2,
                drawWidth, drawHeight,
                1, 2,
                angle,
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
    public boolean isSpecialAbilityActive() {
        return false; }}
