package com.alastar.game;

import java.util.ArrayList;
import java.util.Hashtable;

import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.math.Vector3;

public class Map
{

    public static ArrayList<TexturedObject> entities = new ArrayList<TexturedObject>();
    public static World                     world;

    public static ArrayList<Interpolation>  interps  = new ArrayList<Interpolation>();

    public static void AddInter(Interpolation i)
    {
        interps.add(i);
    }

    public static void RemoveInter(Interpolation i)
    {
        interps.remove(i);
    }

    public static TexturedObject getObjectById(int i, int type)
    {
        for (TexturedObject e : entities)
        {
            if (e.getId() == i && e.getType() == type)
            {
                return e;
            }
        }
        return null;
    }

    public static void handleUpdate(int id, int type, Vector3 vec)
    {

        try
        {
          //  System.out.println("Update type: " + type + " id: " + id
          //          + " vector: " + vec.toString());
            if (getObjectById(id, type) != null)
                ((Transform) getObjectById(id, type)).setPosition(vec);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setWorld(World w)
    {
        world = w;
    }

    public static void removeEntity(int i)
    {
        ((Entity) getObjectById(i, 1)).Remove();
        entities.remove(getObjectById(i, 1));
    }

    public static void addEntity(Entity p)
    {
        entities.add(p);
    }

    public static void handleUpdate(int id, boolean val)
    {
        Entity e = (Entity) getObjectById(id, 1);

        if (e != null)
            e.warMode = val;
    }

    public static void handleRemoveEntity(int id2)
    {
      //  System.out.println("Remove entity: " + id2);
        Entity e = (Entity) getObjectById(id2, 1);
        if (e != null)
        {
            removeEntity(id2);
        }
    }

    public static void StepInterpolations()
    {
        for (int i = interps.size() - 1; i > -1; i--)
        {
            if(interps.get(i) != null)
            {
                interps.get(i).Step();
                if (interps.get(i).finished)
                {
                    RemoveInter(interps.get(i));
                }
            }
        }
    }

    public static void handleEntity(Entity p)
    {
        try
        {
            if (!haveEntity(p))
            {
                addEntity(p);
                p.drawMessageOverhead(p.caption);
               // System.out.println("Entity added! Id: " + p.id);
            } else
            {
                p.Remove();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static boolean haveEntity(Entity p)
    {
        if (getObjectById(p.id, 1) == null)
            return false;
        else
            return true;
    }

    public static void handleTile(Tile t)
    {
        world.tiles.put(t.position, t);
    }

    public static void handleWorld(World readObject)
    {
        setWorld(readObject);
    }

    public static void drawAllByZ(int z)
    {   
        for (int i = entities.size() - 1; i > -1; i--)
        {
            if (entities.get(i).getZ() == z)
            {
                entities.get(i).Draw(ErderGame.batch, 0, 0);
            }
        }
    }

    public static void handleItem(Item item)
    {
        if (getObjectById(item.id, 3) == null)
            entities.add(item);
    }

    public static void handleItemUpdate(int id, int amount)
    {
        Item i = (Item) getObjectById(id, 3);
        if (i != null)
            i.amount = amount;
    }

    public static void handleItemUpdate(int id, Hashtable<String, Integer> attrs)
    {
        Item i = (Item) getObjectById(id, 3);
        if (i != null)
            i.attributes = attrs;
    }

    public static void handleItemUpdate(int id, String caption)
    {
        Item i = (Item) getObjectById(id, 3);
        if (i != null)
            i.caption = caption;
    }

    public static void handleItemUpdate(int id, ItemType type)
    {
        Item i = (Item) getObjectById(id, 3);
        if (i != null)
            i.itemType = type;
    }

    public static void handleRemove(int id, int type)
    {
        TexturedObject obj = getObjectById(id, type);
        if (obj != null)
        {
            entities.remove(obj);
            obj.Remove();
        }
    }

    public static void addProj(Projectile projectile)
    {  
        if (getObjectById(projectile.id, 2) == null)
            entities.add(projectile);        
    }

}
