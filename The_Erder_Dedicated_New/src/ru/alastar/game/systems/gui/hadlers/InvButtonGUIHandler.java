package ru.alastar.game.systems.gui.hadlers;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;

public class InvButtonGUIHandler implements GUIHandler
{
    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        if (!c.controlledEntity.haveGUI("inventory_container"))
        {
            NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(
                    "inventory_container", new Vector2(100, 100), new Vector2(
                            500, 500), "", "com.alastar.game.gui.GUIContainer",
                    "inv", "Inventory"), c);

        }
        else
        {
            c.controlledEntity.closeGUI("inventory_container");
        }
    }
}
