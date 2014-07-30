package com.alastar.game;

import java.util.Hashtable;

import ru.alastar.net.Client;

import com.alastar.game.enums.EntityType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.alastar.game.gui.GUIPlayerOverhead;
import com.alastar.game.gui.GUITarget;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("serial")
public class Entity extends Transform implements TexturedObject
{

    public int                     id         = 0;
    public String                  caption    = "generic Entity";
    public EntityType              type;
    public boolean                 warMode    = false;
    public GUIPlayerOverhead       overhead   = null;
    public GUITarget               target     = null;
    public Hashtable<String, Slot> equip;
    public boolean                 drawTarget = false;

    public Entity(int i, Vector3 pos, String c, EntityType t, boolean mode)
    {
        super(pos);
        this.id = i;
        this.caption = c;
        this.type = t;
        this.warMode = mode;
        this.equip = new Hashtable<String, Slot>();
        overhead = new GUIPlayerOverhead(caption + "_overhead", new Vector2(0,
                0), new Vector2(400, 200));
        target = new GUITarget(this.getTexture().getWidth(), this.getTexture()
                .getHeight());
        target.Hide();
        MainScreen.currentStage.addActor(overhead.getElementAsActor());
        MainScreen.currentStage.addActor(target.getElementAsActor());

    }

    public void addEquip(String s, Item i)
    {
        if (this.equip.containsKey(s))
            this.equip.remove(s);
        this.equip.put(s, new Slot(i));
    }

    public void removeEquip(String s)
    {
        if (this.equip.containsKey(s))
            this.equip.remove(s);
    }

    public Slot getEquipSlot(String s)
    {
        return this.equip.get(s);
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

        float x = this.position.x * GameManager.textureResolution, y = this.position.y
                * GameManager.textureResolution;
        if (id == Client.id)
        {
            if (Client.controlledEntity != null)
            {
                MainScreen.camera.position.x = x;
                MainScreen.camera.position.y = y;
            }
        }
        batch.draw(this.getTexture(), x, y);
        Vector3 vec = MainScreen.camera.project(new Vector3(x, y, 0));
        DrawEquip(batch, x, y);
        target.setPosition(new Vector2(vec.x
                * Vars.getInt("balancedScreenWidth"), vec.y
                * Vars.getInt("balancedScreenHeight")));
        DrawTarget(batch, x, y);
        overhead.setPosition(new Vector2((vec.x * Vars
                .getInt("balancedScreenWidth"))
                - (overhead.getWidth() / 2)
                - 40, (vec.y + this.getTexture().getHeight() + 60)
                * Vars.getInt("balancedScreenHeight")));
    }

    private void DrawTarget(SpriteBatch batch, float i, float j)
    {
        if (drawTarget)
        {
            if (TargetInfo.hits > 0 && TargetInfo.mhits > 0)
            {
                target.setPrecentage((TargetInfo.hits / TargetInfo.mhits) * 100);
            } else
            {
                target.setPrecentage(0);
            }
        }
    }

    private void DrawEquip(SpriteBatch batch, float i, float j)
    {
        Item item;
        for (String slot : equip.keySet())
        {
            if (getEquipSlot(slot) != null)
            {
                if (getEquipSlot(slot).item != null)
                {
                    item = getEquipSlot(slot).item;
                    batch.draw(GameManager.getTexture(item.itemType.name().toLowerCase(), TypeId.getTypeId(Type.Item)), i
                            + item.getEquipX(), j + item.getEquipY());
                }
            }
        }
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
      return TypeId.getTypeId(Type.Entity);
    }

    @Override
    public int getId()
    {
        return id;
    }

    public void drawMessageOverhead(String msg)
    {
        // System.out.println("Overhead of " + caption +
        // " have been displayed. Msg: " + msg);
        overhead.addMsg(msg);
    }

    public void Remove()
    {
        this.overhead.Destroy();
        this.target.Destroy();
    }

    public void DrawTraget(boolean b)
    {
        drawTarget = b;
        System.out.println("Draw target: " + b);
        if (b)
            target.Show();
        else
            target.Hide();
    }

    @Override
    public int getZ()
    {
        return z;
    }

}
