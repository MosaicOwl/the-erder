package com.alastar.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public interface TexturedObject
{
    public Texture getTexture();

    public void setTexture();

    public Transform getTransform();

    public TextureRegion getTextureRegion();

    public void Draw(SpriteBatch batch, float x, float y);

    public int getType();

    public int getId();

    public Rectangle getWindowRectangle();

    public int getZ();

    public void Remove();
}
