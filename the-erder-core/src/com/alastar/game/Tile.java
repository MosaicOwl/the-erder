package com.alastar.game;

import java.io.Serializable;

import com.alastar.game.enums.TileType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Tile extends Transform implements Serializable, TexturedObject
{

    private static final long serialVersionUID = 7420787875382412198L;
    public TileType           type;
    public boolean            passable         = false;

    public Tile(Vector3 pos, TileType t, boolean p)
    {
        super(pos);
        this.type = t;
        this.passable = p;
    }

    @Override
    public Texture getTexture()
    {
        return GameManager.getTexture(type.name().toLowerCase(), getType());
    }

    @Override
    public void setTexture()
    {
    }

    @Override
    public Transform getTransform()
    {
        return (Transform) this;
    }

    @Override
    public void Draw(SpriteBatch batch, float i, float j)
    {
        batch.draw(this.getTexture(), this.position.x
                * GameManager.textureResolution, this.position.y
                * GameManager.textureResolution + this.position.z
                * GameManager.textureResolution);
    }

    @Override
    public TextureRegion getTextureRegion()
    {
        return new TextureRegion(getTexture(), this.position.x
                * GameManager.textureResolution, this.position.y
                * GameManager.textureResolution, GameManager.textureResolution,
                GameManager.textureResolution);
    }

    @Override
    public Rectangle getWindowRectangle()
    {
        return new Rectangle(this.position.x * GameManager.textureResolution,
                this.position.y * GameManager.textureResolution
                        + this.position.z * GameManager.textureResolution,
                GameManager.textureResolution, GameManager.textureResolution);
    }

    @Override
    public int getType()
    {
        return TypeId.getTypeId(Type.Tile);
    }

    @Override
    public int getId()
    {
        return 0;
    }

    @Override
    public int getZ()
    {
        return (int) z;
    }

    @Override
    public void Remove()
    {

    }

}
