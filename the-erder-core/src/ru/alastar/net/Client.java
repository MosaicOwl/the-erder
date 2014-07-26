package ru.alastar.net;

import java.io.IOException;
import java.util.Hashtable;

import ru.alastar.main.net.requests.AuthPacketRequest;
import ru.alastar.main.net.responses.AddEquipResponse;

import com.alastar.game.ContainersInfo;
import com.alastar.game.Entity;
import com.alastar.game.ErderGame;
import com.alastar.game.Item;
import com.alastar.game.Map;
import com.alastar.game.TexturedObject;
import com.alastar.game.Vars;
import com.alastar.game.gui.GUICore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.math.Vector3;

public class Client
{
    private static String                             host             = "127.0.0.1";
    private static int                                port;
    public static com.esotericsoftware.kryonet.Client client           = null;
    public static ErderGame                           game             = null;
    public static int                                 id               = 0;
    public static Entity                              controlledEntity = null;
    public static Hashtable<String, String>           characters       = new Hashtable<String, String>();
    public static Hashtable<String, Stat>             stats            = new Hashtable<String, Stat>();
    public static Hashtable<String, Skill>            skills           = new Hashtable<String, Skill>();
    public static Hashtable<Integer, Item>            inventory        = new Hashtable<Integer, Item>();
    private static Entity                             target           = null;
    public static String                              login            = "";
    public static String                              pass             = "";

    public static void StartClient() throws Exception
    {
        client = new com.esotericsoftware.kryonet.Client();
        client.start();
        client.addListener(new ClientListener(client));
    }

    public static void Connect(String address)
    {
        try
        {
            port = Integer.parseInt(address.split(":")[1]);
            host = address.split(":")[0];
            if (Gdx.app.getType() == ApplicationType.Android)
                host = "10.0.0.2";
            client.connect(100, host, port, port + 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void Log(String s)
    {
        System.out.println(s);
    }

    public static void LoadWorld(String string)
    {
        System.out.println("Client: Load World");
        ErderGame.LoadWorld(string);
    }

    public static void handleStat(String name, int sValue, int mValue)
    {
        if (stats.containsKey(name.toLowerCase()))
        {
            stats.remove(name.toLowerCase());
        }
        if (Vars.integerVars.containsKey(name.toLowerCase() + "_value"))
        {
            Vars.setVar(name.toLowerCase() + "_value", sValue);
        } else
            Vars.AddVar(name.toLowerCase() + "_value", sValue);
        if (Vars.integerVars.containsKey(name.toLowerCase() + "_max"))
        {
            Vars.setVar(name.toLowerCase() + "_max", mValue);
        } else
            Vars.AddVar(name.toLowerCase() + "_max", mValue);

        stats.put(name.toLowerCase(), new Stat(name, sValue, mValue));
    }

    public static void handleSkill(String name, int sValue, int mValue)
    {
        if (skills.containsKey(name.toLowerCase()))
        {
            skills.remove(name.toLowerCase());
        }
        if (Vars.integerVars.containsKey(name.toLowerCase() + "_value"))
        {
            Vars.setVar(name.toLowerCase() + "_value", sValue);
        } else
            Vars.AddVar(name.toLowerCase() + "_value", sValue);
        if (Vars.integerVars.containsKey(name.toLowerCase() + "_max"))
        {
            Vars.setVar(name.toLowerCase() + "_max", mValue);
        } else
            Vars.AddVar(name.toLowerCase() + "_max", mValue);

        skills.put(name.toLowerCase(), new Skill(name, sValue, mValue));
    }

    public static void handleInv(Item item)
    {
        ContainersInfo.addToContainer("inv", item);
        // if(inventory.containsKey(item.id))
        // inventory.remove(item.id);
        // inventory.put(item.id, item);
    }

    public static void Send(Object r)
    {
        if (client != null)
            client.sendUDP(r);
    }

    public static void handleChar(String name, String type)
    {
        characters.put(name, type);
    }

    public static void handleSpeech(int id2, String msg)
    {
        for (TexturedObject e : Map.entities)
        {
            if (e.getId() == id2)
            {
                ((Entity) e).drawMessageOverhead(msg);
            }
        }
    }

    public static void handleEntityRemove(int id2)
    {
        Map.handleRemoveEntity(id2);
    }

    public static void handleEquip(AddEquipResponse r)
    {
        Entity e = (Entity) Map.getObjectById(r.eid, 1);
        if (e != null)
        {
            e.addEquip(r.slot, new Item(r.id, new Vector3(), r.captiion,
                    r.type, r.amount, r.attrs));
        }
    }

    public static void handleEquip(int eid, String slot)
    {
        Entity e = (Entity) Map.getObjectById(eid, 1);
        if (e != null)
        {
            e.removeEquip(slot);
        }
    }

    public static void handleTarget(int id2)
    {
        if (id2 != -1)
        {
            Entity e = (Entity) Map.getObjectById(id2, 1);
            if (e != null)
            {

                if (target != null)
                    target.DrawTraget(false);

                target = e;
                target.DrawTraget(true);
            }
        } else
        {

            if (target != null)
                target.DrawTraget(false);

            target = null;
        }
    }

    public static void init(String address)
    {
        try
        {
            Client.StartClient();
            Client.Connect(address);

            AuthPacketRequest r = new AuthPacketRequest();
            r.login = Client.login;
            r.pass = Client.pass;
            Client.Send(r);
            GUICore.getConstructedByName("servers_list").Hide();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
