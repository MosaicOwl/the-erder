package ru.alastar.game.systems.gui.hadlers;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.game.Equip;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;

public class EquipButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        if(c.controlledEntity.haveGUI("equip"))
        {
            c.controlledEntity.closeGUI("equip");
        }
        else
        {
            NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(
                    "equip", new Vector2(300, 300), new Vector2(
                            500, 500), "", "com.alastar.game.gui.GUIWindow",
                    "", "Equip"), c);
            Equip equip = Server.getEquip(c.controlledEntity);
            for(String s: equip.contents.keySet())
            {
                if(equip.contents.get(s).item != null)
             NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(s, new Vector2(), new Vector2(), "equip", "com.alastar.game.gui.GUIEquipSlot", equip.contents.get(s).item.type.name(), s), c);
                else
             NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(s, new Vector2(), new Vector2(), "equip", "com.alastar.game.gui.GUIEquipSlot", "None", s), c);
                
            }
        }
    }

}
