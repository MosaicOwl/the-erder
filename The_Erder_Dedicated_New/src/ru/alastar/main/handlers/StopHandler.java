package ru.alastar.main.handlers;

import ru.alastar.main.net.Server;

import com.esotericsoftware.kryonet.Connection;

public class StopHandler extends Handler
{
    public StopHandler()
    {
        this.description = "Stops the server";
    }

    @Override
    public void execute(String[] args, Connection c)
    {
        try
        {
            Server.Stop();
        } catch (Exception e)
        {
            Server.handleError(e);
        }
    }
}
