// Pig.java
package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

abstract class Pig {
    protected float x, y;
    protected float width, height;
    protected Texture texture;
    protected Rectangle bounds;
    protected float health;
    protected boolean isAlive;
    protected Body body;
    protected World world;
    protected static final float PIXELS_TO_METERS = 100f;



    protected int hitCount = 0;
    protected int maxHits;
    protected boolean shouldDestroy = false; // New flag for safe destruction

    public Pig(World world, float x, float y, float width, float height, String texturePath, float health,int maxHits) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.maxHits = maxHits;
        this.isAlive = true;
        this.hitCount = 0;
        this.texture = new Texture(texturePath);
        this.bounds = new Rectangle(x, y, width, height);

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x + width/2) / PIXELS_TO_METERS,
            (y + height/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius((width/2) / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void render(SpriteBatch batch,float x, float y) {
        if (isAlive && body != null) {
            x = body.getPosition().x * PIXELS_TO_METERS - width/2;
            y = body.getPosition().y * PIXELS_TO_METERS - height/2;
            bounds.x = x;
            bounds.y = y;

            float rotation = (float)Math.toDegrees(body.getAngle());

            batch.draw(texture,
                x, y,
                width/2, height/2,
                width, height,
                1, 1,
                rotation,
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }

    public int getHitCount() {
        return hitCount;
    }
    public void takeDamage(float damage) {
        if (!isAlive) return;

        hitCount++;
        if (hitCount >= maxHits) {
            isAlive = false;
            shouldDestroy = true;
        }
    }



    public boolean isAlive() {
        return isAlive;
    }

    public boolean shouldDestroy() {
        return shouldDestroy;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public void destroyBody() {
        if (body != null && !world.isLocked()) {
            world.destroyBody(body);
            body = null;
        }
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public Body getBody() {
        return body;
    }
}
