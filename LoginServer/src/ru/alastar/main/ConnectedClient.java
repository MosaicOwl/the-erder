package ru.alastar.main;

import java.util.Date;

import com.esotericsoftware.kryonet.Connection;

public class ConnectedClient
{
    public Connection connection;
    public Account    account;
    public Date       lastPacket;

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
    
    public void setAccount(Account a)
    {
        this.account = a;
    }
    
    public Account getAccount()
    {
        return this.account;
    }
}
