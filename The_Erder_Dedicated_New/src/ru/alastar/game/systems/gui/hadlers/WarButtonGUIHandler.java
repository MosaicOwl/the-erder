package ru.alastar.game.systems.gui.hadlers;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;

public class WarButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        c.controlledEntity.closeGUI("war_button");
        c.controlledEntity.toggleWar(false);
        NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("peace_button",
                new Vector2(250, 70), new Vector2(100, 50), "",
                "com.alastar.game.gui.GUIButton", "", "peace"), c);
        c.controlledEntity.AddGUIHandler("peace_button",
                new PeaceButtonGUIHandler());
    }

}
