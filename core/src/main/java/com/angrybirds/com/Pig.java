package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

abstract class Pig {
    protected float x, y;
    protected float width, height;
    protected Texture texture;
    protected Rectangle bounds;
    protected float health;
    protected boolean isAlive;

    public Pig(float x, float y, float width, float height, String texturePath, float health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.isAlive = true;
        this.texture = new Texture(texturePath);
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        if (isAlive) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
