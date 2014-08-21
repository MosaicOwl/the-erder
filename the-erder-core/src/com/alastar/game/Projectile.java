package com.alastar.game;

import com.alastar.game.enums.ProjectileType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("serial")
public class Projectile extends Transform implements TexturedObject
{
    public int id;
    public ProjectileType type;
    public float angle;
    
    public Projectile(int id, float angle, ProjectileType projectileType,
            float x, float y, float z)
    {
       super(new Vector3(x, y, z));
       this.id = id;
       this.angle = angle;
       this.type = projectileType;
    }

    @Override
    public Texture getTexture()
    {
        return GameManager.getTexture(type.name().toLowerCase(), TypeId.getTypeId(Type.Projectile));
    }

    @Override
    public void setTexture()
    {
        
    }

    @Override
    public Transform getTransform()
    {
        return this;
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
                this.position.y * GameManager.textureResolution,
                GameManager.textureResolution, GameManager.textureResolution);
    }
    @Override
    public void Draw(SpriteBatch batch, float x, float y)
    {
        batch.draw(this.getTexture(), (float)this.position.x * GameManager.textureResolution, (float)this.position.y* GameManager.textureResolution);
    }

    @Override
    public int getType()
    {
        return TypeId.getTypeId(Type.Projectile);
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public int getZ()
    {
        return z;
    }

    @Override
    public void Remove()
    {
        
    }

}
