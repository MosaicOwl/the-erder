package ru.alastar.main.net;

import java.util.Date;

import ru.alastar.game.Account;
import ru.alastar.game.Entity;

import com.esotericsoftware.kryonet.Connection;

public class ConnectedClient
{

    public Date       lastPacket;

    public Connection connection;

    public Entity     controlledEntity;
    public Account    account;

    public String     awaitedLogin;

    public ConnectedClient(Connection c)
    {
        this.connection = c;
        lastPacket = new Date();
    }

    public int getAccountId()
    {
        if (account != null)
            return account.id;
        return -1;
    }

}
