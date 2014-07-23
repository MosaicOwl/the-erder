package ru.alastar.game.systems.gui.hadlers;

import ru.alastar.main.net.ConnectedClient;

public class TileButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        c.controlledEntity.closeGUI("dropdown");
        c.controlledEntity.RemoveGUIHandler("dropdown");
    }

}
