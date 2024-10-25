package com.angrybirds.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

// Base class for all blocks
abstract class Block {
    protected float x, y;
    protected float width, height;
    protected Texture texture;
    protected Rectangle bounds;
    protected float health;

    public Block(float x, float y, float width, float height, String texturePath, float health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.texture = new Texture(texturePath);
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void takeDamage(float damage) {
        health -= damage;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
