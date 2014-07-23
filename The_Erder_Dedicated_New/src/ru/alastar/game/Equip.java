package ru.alastar.game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.database.DatabaseClient;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;



public class Equip
{

    public Entity entity;
    public Hashtable<String, SlotInfo> contents;
    
    public Equip(Entity e)
    {
      this.entity = e;
      contents  = new Hashtable<String, SlotInfo>();
    }
    
    public void addEquipSlot(String slotName){
        if(contents.get(slotName) == null){
            //Main.Log("[DEBUG]","Slot " + slotName + " has been added");
            contents.put(slotName, new SlotInfo());
            
            ConnectedClient c = Server.getClient(entity);
            if(entity.haveGUI("equip") && !entity.haveGUI(slotName))
            {
                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(slotName, new Vector2(), new Vector2(), "equip", "ru.alastar.game.gui.GUIEquipSlot", "", slotName), c);
            } 
            SaveSlot(entity.id, slotName, -1);
        }
    }
    
    public void SaveSlot(int id, String slotName, int i)
    {
        try
        {
        ResultSet rs = DatabaseClient.commandExecute("SELECT * FROM equip WHERE entityId=" + id + " AND slotName='"+slotName+"'");

            if(rs.next()){
               DatabaseClient.commandExecute("UPDATE equip SET itemId="+i+" WHERE entityId=" + id + " AND slotName='"+slotName+"'"); 
            }
            else
            {
                DatabaseClient.commandExecute("INSERT INTO equip(entityId, slotName, itemId) VALUES("+id+",'"+slotName+"',"+i+");");   
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addToEquip(String slotName, Item i){
        if(contents.get(slotName) != null){
            SlotInfo s = new SlotInfo();
            s.item = i;
            contents.put(slotName, s);
           // Main.Log("[DEBUG]","Item " + i.caption + " has been added to slot " + slotName);
            ConnectedClient c = Server.getClient(entity);
            if(entity.haveGUI("equip") && entity.haveGUI(slotName))
            {
                entity.closeGUI(slotName);
                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(slotName, new Vector2(), new Vector2(), "equip", "ru.alastar.game.gui.GUIEquipSlot", i.type.name(), slotName), c);
            }
            SaveSlot(entity.id, slotName, i.id);
        }
    }
}
