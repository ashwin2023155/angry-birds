package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

abstract class Block {
    protected Texture texture;
    protected Body body;
    protected float width, height;
    protected float health;
    protected static final float PIXELS_TO_METERS = 100f;

    public Block(World world, float x, float y, float width, float height, String texturePath, float health) {
        this.width = width;
        this.height = height;
        this.health = health;
        this.texture = new Texture(texturePath);

        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;  // Start as dynamic
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);

        // Enable bullet physics for more accurate collision detection
        bodyDef.bullet = true;

        // Allow body to sleep when static to improve performance
        bodyDef.allowSleep = true;

        // Create body in world
        body = world.createBody(bodyDef);

        // Create rectangular shape
        PolygonShape shape = new PolygonShape();
        if (this instanceof StoneBlock) {
            // For StoneBlock, swap width and height for horizontal orientation
            shape.setAsBox((width * 0.8f) / (2 * PIXELS_TO_METERS), (height*0.3f) / (2 * PIXELS_TO_METERS));
        } else {
            // For other blocks, use normal vertical orientation
            shape.setAsBox( (width * 0.4f) / (2 * PIXELS_TO_METERS), height / (2 * PIXELS_TO_METERS));
        }


        // Create fixture with improved physics properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5.0f;        // Increased mass
        fixtureDef.friction = 0.8f;       // More friction
        fixtureDef.restitution = 0.1f;    // Low bounce

        body.createFixture(fixtureDef);
        body.setUserData(this);

        // Initialize as static until hit
        body.setType(BodyDef.BodyType.StaticBody);

        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        float angle = body.getAngle();

        batch.draw(texture,
            position.x * PIXELS_TO_METERS - width/2,
            position.y * PIXELS_TO_METERS - height/2,
            width/2, height/2,
            width, height,
            1, 1,
            angle * MathUtils.radiansToDegrees,
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false);
    }

    public void applyImpact(float forceX, float forceY) {
        Vector2 position = body.getPosition();
        body.applyLinearImpulse(
            forceX / PIXELS_TO_METERS,
            forceY / PIXELS_TO_METERS,
            position.x,
            position.y,
            true
        );
    }



    public boolean isDestroyed() {
        return health <= 0;
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }
    public void takeDamage(float damage) {
        health -= damage;

        // Don't modify body type here - let it be handled by the GameScreen's processPhysicsChanges
        if (isDestroyed()) {
            // Only modify properties that don't require body changes
            for (Fixture fixture : body.getFixtureList()) {
                fixture.setDensity(fixture.getDensity() * 0.5f);
            }
        }
    }
    // Add this new method
    public void activate() {
        if (body.getType() != BodyDef.BodyType.DynamicBody) {
            body.setType(BodyDef.BodyType.DynamicBody);
            // Add a small impulse to ensure physics simulation activates
            body.applyLinearImpulse(
                0.1f,
                0.1f,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
            );
        }
    }
}
