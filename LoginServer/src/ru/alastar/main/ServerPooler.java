package ru.alastar.main;


import com.esotericsoftware.kryonet.Connection;

import ru.alastar.requests.AccountRequest;
import ru.alastar.requests.AuthServerRequest;
import ru.alastar.responses.ProcessLoginResponse;

public class ServerPooler
{   
    private static com.esotericsoftware.kryonet.Server           server;

    public static void startServer()
    {
        try
        {
            server = new com.esotericsoftware.kryonet.Server();
            server.start();
            server.bind(Integer.parseInt(Configuration.GetEntryValue("poolerPort")),
                    Integer.parseInt(Configuration.GetEntryValue("poolerPort")) + 1);
            server.addListener(new SPListener(server));
        } catch (Exception  e)
        {
            handleError(e);
        }
    }

    static void handleError(Exception e)
    {
        e.printStackTrace();
    }

    public static void ProcessAuth(AuthServerRequest object,
            Connection connection)
    {  
        ServerInfo s = new ServerInfo(object.name,connection.getRemoteAddressUDP().getAddress().getHostAddress() +":"+object.port, connection, object.state);

        Server.servers.put(object.name, s);
        
        MainClass.Log("[SERVER]", "Server " + object.name + " has been registered. Address: " +  s.address);

    }

    public static ServerInfo getServerInfo(Connection connection)
    {
        for(ServerInfo s: Server.servers.values())
        {
            if(s.connection == connection)
            {
                return s;
            }
        }
        return null;
    }

    public static boolean hasServer(ServerInfo c)
    {  
        for(ServerInfo s: Server.servers.values())
       {
        if(s == c)
        {
            return true;
        }
    }
        return false;
    }

    public static void removeServer(ServerInfo c)
    {
        try{
            MainClass.Log("[SERVER]", "Server " + c.name + " has been disconnected!");
        Server.servers.remove(c.name);
        }catch(Exception e)
        {
            handleError(e);
        }
    }
    
    private static void SendTo(Connection c, Object r)
    {
        c.sendUDP(r);
    }
    
    public static void ProcessAccountRequest(AccountRequest object,
            Connection connection)
    {   
        ProcessLoginResponse r = new ProcessLoginResponse();
        Account acc;
        if(Server.hasAccount(object.login))
        {
            acc = Server.getAccount(object.login);
            r.allow = true;
            r.login = acc.login;
            r.pass = acc.pass;
            r.id = acc.id;
            r.mail = acc.mail;
        }
        else
        {
            r.allow = false;
            r.login = object.login;
        }
        SendTo(connection, r);
    }
}
