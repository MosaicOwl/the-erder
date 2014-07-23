package ru.alastar.main.handlers;

import ru.alastar.main.net.Server;

import com.esotericsoftware.kryonet.Connection;

public class SaveHandler extends Handler
{
    public SaveHandler()
    {
        this.description = "Saves server data";
    }

    @Override
    public void execute(String[] args, Connection c)
    {
        try
        {
            Server.Save();
        } catch (Exception e)
        {
            Server.handleError(e);
        }
    }
}
