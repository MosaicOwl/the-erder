package ru.alastar.net;

import java.io.IOException;
import java.util.Hashtable;

import ru.alastar.main.net.responses.AddEquipResponse;

import com.alastar.game.ContainersInfo;
import com.alastar.game.Entity;
import com.alastar.game.ErderGame;
import com.alastar.game.Item;
import com.alastar.game.ModeManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.math.Vector3;

public class Client {
	private static String host = "127.0.0.1";
	private static int port = 25565;
	public static com.esotericsoftware.kryonet.Client client = null;
	public static ErderGame game = null;
	public static int id = 0;
	public static Entity controlledEntity = null;
	public static Hashtable<String, String> characters = new Hashtable<String, String>();
    public static Hashtable<String, Stat> stats = new Hashtable<String, Stat>();
    public static Hashtable<String, Skill> skills = new Hashtable<String, Skill>();
    public static Hashtable<Integer, Item> inventory = new Hashtable<Integer, Item>();
    private static Entity                  target    = null;

	
	public static void StartClient() throws Exception {
		client = new com.esotericsoftware.kryonet.Client();
		client.start();
		client.addListener(new ClientListener(client));
	}

	public static void Connect() {
		try {
		    if(Gdx.app.getType() == ApplicationType.Android)
		        host = "10.0.0.2";
			client.connect(100, host, port, port + 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Log(String s) {
		System.out.println(s);
	}

	public static void LoadWorld(String string) {
		System.out.println("Client: Load World");
		ErderGame.LoadWorld(string);
	}

    public static void handleStat(String name, int sValue, int mValue)
    {
        if(stats.containsKey(name.toLowerCase())){
            stats.remove(name.toLowerCase());
        }
        if(Vars.integerVars.containsKey(name.toLowerCase()+"_value")){
            Vars.setVar(name.toLowerCase()+"_value", sValue);
        }
        else
            Vars.AddVar(name.toLowerCase()+"_value", sValue);
        if(Vars.integerVars.containsKey(name.toLowerCase()+"_max")){
            Vars.setVar(name.toLowerCase()+"_max", mValue);
        }
        else
            Vars.AddVar(name.toLowerCase()+"_max", mValue);
        
        stats.put(name.toLowerCase(), new Stat(name, sValue, mValue));
    }

    public static void handleSkill(String name, int sValue, int mValue)
    {
        if(skills.containsKey(name.toLowerCase())){
            skills.remove(name.toLowerCase());
        }
        if(Vars.integerVars.containsKey(name.toLowerCase()+"_value")){
            Vars.setVar(name.toLowerCase()+"_value", sValue);
        }
        else
            Vars.AddVar(name.toLowerCase()+"_value", sValue);
        if(Vars.integerVars.containsKey(name.toLowerCase()+"_max")){
            Vars.setVar(name.toLowerCase()+"_max", mValue);
        }
        else
            Vars.AddVar(name.toLowerCase()+"_max", mValue);
        
        skills.put(name.toLowerCase(), new Skill(name, sValue, mValue));  
    }

    public static void handleInv(Item item)
    {
        ContainersInfo.addToContainer("inv", item);
      //  if(inventory.containsKey(item.id))
      //      inventory.remove(item.id);
      //  inventory.put(item.id, item);
    }

    public static void handleChar(String name, String type)
    {
        characters.put(name, type);
    }

    public static void handleSpeech(int id2, String msg)
    {
        for(Entity e: ModeManager.currentMode.entities){
            if(e.id == id2){
                e.drawMessageOverhead(msg);
          }
        }
    }

    public static void handleEntityRemove(int id2)
    {
        ModeManager.currentMode.handleRemoveEntity(id2);
    }

    public static void handleEquip(AddEquipResponse r)
    {
        Entity e = ModeManager.currentMode.getEntityById(r.eid);
        if(e != null)
        {
            e.addEquip(r.slot, new Item(r.id, new Vector3(), r.captiion, r.type, r.amount, r.attrs));
        }
    }

    public static void handleEquip(int eid, String slot)
    {
        Entity e = ModeManager.currentMode.getEntityById(eid);
        if(e != null)
        {
            e.removeEquip(slot);
        }
    }

    public static void handleTarget(int id2)
    {
        if(id2 != -1){
        Entity e = ModeManager.currentMode.getEntityById(id2);
         if(e != null)
         {
            
            if(target != null)
            target.DrawTraget(false);
            
            target = e;
            target.DrawTraget(true);
         }
        }
        else
        {
            
            if(target != null)
            target.DrawTraget(false);
            
            target = null;
        }
    }
}