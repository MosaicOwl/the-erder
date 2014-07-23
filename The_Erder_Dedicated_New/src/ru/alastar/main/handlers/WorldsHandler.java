package ru.alastar.main.handlers;

import ru.alastar.main.Main;
import ru.alastar.main.net.Server;

import com.esotericsoftware.kryonet.Connection;

public class WorldsHandler extends Handler
{
    public WorldsHandler()
    {
        this.description = "Shows loaded world on the server";
    }

    @Override
    public void execute(String[] args, Connection c)
    {
        try
        {
            for (Integer s : Server.worlds.keySet())
            {
                Main.Log("[WORLD]", s + " - id: " + Server.worlds.get(s).id
                        + " name: " + Server.worlds.get(s).name);
            }
        } catch (Exception e)
        {
            Server.handleError(e);
        }
    }
}
