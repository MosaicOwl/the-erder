package com.alastar.game;

import java.util.Hashtable;

import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Item extends Transform implements TexturedObject
{

    /**
     * 
     */
    private static final long         serialVersionUID = 1L;
    public int                        amount, id;
    public String                     caption;
    public Hashtable<String, Integer> attributes;
    public ItemType                   itemType;

    public Item(int id, Vector3 pos, String caption, ItemType itemType,
            int amt, Hashtable<String, Integer> attr)
    {
        super(pos);
        this.id = id;
        this.amount = amt;
        this.caption = caption;
        this.attributes = attr;
        this.itemType = itemType;
    }

    public int getEquipX()
    {
        switch (itemType)
        {
            case Axe:
                break;
            case Bag:
                break;
            case Chest:
                break;
            case Gem:
                break;
            case Gloves:
                break;
            case Gold:
                break;
            case Gorget:
                break;
            case Helm:
                break;
            case Leggings:
                break;
            case None:
                break;
            case Ore:
                break;
            case Pickaxe:
                break;
            case Plant:
                break;
            case Reagent:
                break;
            case Totem:
                break;
            case Tunic:
                break;
            case Wood:
                break;
            default:
                break;

        }
        return 0;
    }

    public int getEquipY()
    {
        switch (itemType)
        {
            case Axe:
                break;
            case Bag:
                break;
            case Chest:
                break;
            case Gem:
                break;
            case Gloves:
                return 5;
            case Gold:
                break;
            case Gorget:
                return 6;
            case Helm:
                return 7;
            case Leggings:
                return 0;
            case None:
                break;
            case Ore:
                break;
            case Pickaxe:
                break;
            case Plant:
                break;
            case Reagent:
                break;
            case Totem:
                break;
            case Tunic:
                break;
            case Wood:
                break;
            default:
                break;

        }
        return 0;
    }

    @Override
    public void Draw(SpriteBatch batch, float i, float j)
    {
        float x = this.position.x * GameManager.textureResolution, y = this.position.y
                * GameManager.textureResolution;
        batch.draw(this.getTexture(), x, y);
    }

    @Override
    public Texture getTexture()
    {
        return GameManager.getItemTexture(itemType);
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
    public int getType()
    {
        return 3;
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
        // TODO Auto-generated method stub

    }

}
