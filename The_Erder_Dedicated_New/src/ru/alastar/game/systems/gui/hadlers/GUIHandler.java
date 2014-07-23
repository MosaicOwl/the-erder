package ru.alastar.game.systems.gui.hadlers;

import ru.alastar.main.net.ConnectedClient;

public interface GUIHandler
{
    public void handle(String[] args, ConnectedClient c);
}
