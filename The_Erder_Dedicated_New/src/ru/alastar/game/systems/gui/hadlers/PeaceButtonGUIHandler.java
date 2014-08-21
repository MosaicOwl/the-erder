package ru.alastar.game.systems.gui.hadlers;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;

public class PeaceButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        c.controlledEntity.closeGUI("peace_button");
        c.controlledEntity.toggleWar(true);    
        NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("war_button",
                new Vector2(100, 35), new Vector2(50, 25), "",
                "com.alastar.game.gui.GUIButton", "", "war"), c);
        c.controlledEntity.AddGUIHandler("war_button",
                new WarButtonGUIHandler());
    }

}
