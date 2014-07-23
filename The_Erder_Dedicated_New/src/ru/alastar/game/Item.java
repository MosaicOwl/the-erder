package ru.alastar.game;

import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.math.Vector3;

import ru.alastar.enums.EquipType;
import ru.alastar.main.net.Server;

public class Item extends Transform
{

    public int         id;
    public String      caption;
    public int         amount;
    public int         entityId;
    public EquipType   eqType;
    public Attributes  attributes;
    public ItemType type;
    public int worldId;

    public Item(int i, int ei, String c, int a, int x, int y, int z,
            EquipType et, ItemType type, Attributes a1, int wId)
    {
        super(new Vector3(x, y, z));
        this.id = i;
        this.caption = c;
        this.amount = a;
        this.entityId = ei;
        this.eqType = et;
        this.type = type;
        this.attributes = a1;
        this.worldId = wId;
        
        Server.SaveItem(this);
    }

    public Vector3 getPos()
    {
        return this.pos;
    }

    public int getAttributeValue(String s)
    {
        return attributes.getValue(s);
    }

    public boolean setAttributeValue(String s, int v)
    {
        return attributes.setValue(s, v);
    }

    public void diffValue(String s, int i)
    {
        attributes.setValue(s, attributes.getValue(s) - i);
    }

}
