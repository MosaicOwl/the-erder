package ru.alastar.game.systems.gui.hadlers;

import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;

public class HireButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        Server.warnClient(c, "Sorry but we dont implement this feature yet :(",
                0);
        c.controlledEntity.closeGUI("dropdown");
        c.controlledEntity.RemoveGUIHandler("dropdown");
    }

}
