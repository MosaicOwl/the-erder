package ru.alastar.game.worldwide;

import java.util.ArrayList;
import java.util.Hashtable;

import com.alastar.game.enums.ItemType;

import ru.alastar.enums.EquipType;
import ru.alastar.game.Attributes;
import ru.alastar.game.Entity;
import ru.alastar.game.Inventory;
import ru.alastar.game.Item;
import ru.alastar.game.Skill;
import ru.alastar.main.Main;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddEntityResponse;
import ru.alastar.main.net.responses.ChatSendResponse;
import ru.alastar.main.net.responses.RemoveEntityResponse;

public class Location
{

    public int                               id          = -1;
    public String                            name        = "generic location";
    public Hashtable<Integer, Entity>        entities;
    public Hashtable<String, LocationFlag>   flags;
    public ArrayList<Integer>                nearLocationsIDs;
    public static Hashtable<Integer, String> woods       = new Hashtable<Integer, String>();
    public static Hashtable<Integer, String> miningItems = new Hashtable<Integer, String>();

    public Location(int i, String n, ArrayList<Integer> nlIDs,
            Hashtable<String, LocationFlag> flags)
    {
        this.id = i;
        this.name = n;
        this.entities = new Hashtable<Integer, Entity>();
        this.flags = new Hashtable<String, LocationFlag>();
        this.nearLocationsIDs = nlIDs;
        this.flags = flags;
        /*
         * Main.Log("[SERVER]", "Near Locations IDs(" + nearLocationsIDs.size()
         * + ") in " + name + " is - "); for (int i1 : nearLocationsIDs) {
         * System.out.println(i1); }
         */
    }

    public Entity getEntityById(int i)
    {
        try
        {
            return entities.get(i);
        } catch (Exception e)
        {
            Main.Log("[ERROR]", e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void AddEntity(Entity e)
    {
        AddEntityResponse r = new AddEntityResponse();
        r.id = e.id;
        r.caption = e.caption;
        r.type = e.type;
        for (Entity e1 : entities.values())
        {
            Server.SendTo(Server.getClient(e1).connection, r);
        }
        entities.put(e.id, e);
    }

    public void SendEntitiesAround(Entity entity)
    {
        AddEntityResponse r;
        for (Entity e1 : entities.values())
        {
            r = new AddEntityResponse();
            r.id = e1.id;
            r.caption = e1.caption;
            r.type = e1.type;
            Server.SendTo(Server.getClient(entity).connection, r);
        }
    }

    public void sendAll(String msg, String caption)
    {
        ChatSendResponse r = new ChatSendResponse();
        r.msg = "\'" + msg + "\'";
        r.sender = caption;
        for (Entity e1 : entities.values())
        {
            Server.SendTo(Server.getClient(e1).connection, r);
        }
        Main.Log("[CHAT]", "(" + this.name + ")" + caption + ":" + msg);
    }

    public void RemoveEntity(Entity entity)
    {
        RemoveEntityResponse r = new RemoveEntityResponse();
        r.id = entity.id;
        entities.remove(entity.id);
        for (Entity e1 : entities.values())
        {
            Server.SendTo(Server.getClient(e1).connection, r);
        }
    }

    public boolean haveFlag(String string)
    {
        if (flags.containsKey(string))
            return true;
        else
            return false;
    }

    public void getRandomMaterial(Entity entity, Skill skill,
            Hashtable<Integer, String> s, ItemType t)
    {
        ArrayList<String> alloweditems = new ArrayList<String>();
        for (int skillVal : s.keySet())
        {
            if (skill.value >= skillVal)
            {
                alloweditems.add(s.get(skillVal));
            } else
            {
                continue;
            }
        }
        Item item = new Item(Server.getFreeItemId(), entity.id,
                alloweditems.get(Server.random.nextInt(alloweditems.size())),
                Server.random.nextInt(4) + 1, (int) entity.pos.x,
                (int) entity.pos.y, (int) entity.pos.z, EquipType.None,
                t, new Attributes(), entity.world.id);
        Inventory inv = Server.getInventory(entity);
        if (inv != null)
            inv.AddItem(item);
    }

    public LocationFlag getFlag(String string)
    {
        try
        {
            return flags.get(string);
        } catch (Exception e0)
        {
            Server.handleError(e0);
            return null;
        }
    }

}
