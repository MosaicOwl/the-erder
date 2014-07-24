package ru.alastar.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import ru.alastar.database.DatabaseClient;
import ru.alastar.requests.AuthPacketRequest;
import ru.alastar.responses.AddServerResponse;
import ru.alastar.responses.AuthResponse;
import ru.alastar.responses.ServerListing;

import com.esotericsoftware.kryonet.Connection;

public class Server
{

    private static com.esotericsoftware.kryonet.Server           server;
    public  static Hashtable<String, ServerInfo>                     servers  = new Hashtable<String, ServerInfo>();
    public  static Hashtable<InetSocketAddress, ConnectedClient> clients  = new Hashtable<InetSocketAddress, ConnectedClient>();
    public  static Hashtable<String, String>                     accounts = new Hashtable<String, String>();

    public static void startServer()
    {
        try
        {
            DatabaseClient.Start();
            server = new com.esotericsoftware.kryonet.Server();
            server.start();
            server.bind(Integer.parseInt(Configuration.GetEntryValue("port")),
                    Integer.parseInt(Configuration.GetEntryValue("port")) + 1);
            server.addListener(new TListener(server));
        } catch (Exception  e)
        {
            handleError(e);
        }
    }

    static void handleError(Exception e)
    {
        e.printStackTrace();
    }

    public static boolean hasClient(Connection connection)
    {
        if (clients.containsKey(connection.getRemoteAddressUDP()))
            return true;
        else
            return false;
    }

    public static void addClient(Connection connection)
    {
        clients.put(connection.getRemoteAddressUDP(), new ConnectedClient(
                connection));
    }

    public static ConnectedClient getClient(Connection c)
    {
        if(clients.containsKey(c.getRemoteAddressUDP()))
        return clients.get(c.getRemoteAddressUDP());
        else
        return null;
    }

    public static void removeClient(Connection connection)
    {
        clients.remove(connection.getRemoteAddressUDP());
        MainClass.Log("[CONN]","Connection removed. Count: " + clients.size());
    }

    public static void LoadConfig()
    {
        try
        {

            File configFile = null;
            FileReader fr;
            BufferedReader br;
            FileWriter fw;
            BufferedWriter bw;
            String s;

            configFile = new File("config.cfg");

            if (!configFile.exists())
            {
                configFile.createNewFile();
                fw = new FileWriter(configFile);
                bw = new BufferedWriter(fw);
                bw.write("port=2526\n");
                bw.write("poolerPort=3526\n");
                bw.write("dbName=theerder\n");
                bw.write("dbUser=root\n");
                bw.write("dbPass=\n");

                bw.flush();
                fw.close();
                bw.close();
            }

            fr = new FileReader(configFile);
            br = new BufferedReader(fr);

            // Reading configuration
            while ((s = br.readLine()) != null)
            {
                if (s.split("=").length > 1)
                {
                    Configuration.AddEntry(s.split("=")[0], s.split("=")[1]);
                } else
                {
                    Configuration.AddEntry(s.split("=")[0], "");
                }
            }

            br.close();
            fr.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void ProcessAuth(AuthPacketRequest object, Connection c)
    {
        try
        {
            MainClass.Log("[AUTH]", "Passing account " + object.login + " password - " + object.pass +"...", false);
            ResultSet rs = DatabaseClient
                    .commandExecute("SELECT * FROM accounts WHERE login='"
                            + object.login + "' LIMIT 1;");
            
            AuthResponse r = new AuthResponse();

            if (Logged(object))
            {
                r.locale = false;
                r.msg = "This account is already logged in!";
                r.localeId = 0;
                r.state = AuthState.AlreadyLogged;
                MainClass.Log("already logged!\n", false);

            } else if (!rs.next())
            {
              r.locale = false;
              r.msg = "This account is not exists!";
              r.localeId = 0;
              r.state = AuthState.AccountNotExists;
              MainClass.Log("not exists!\n", false);
              
            } else if(rs.getString("password").equals(encrypt(object.pass)))
            {
                r.locale = false;
                r.msg = "Succesfully logged in!";
                r.localeId = 0;
                r.state = AuthState.Success;
                getClient(c).setAccount(new Account(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("mail")));
                accounts.put(object.login, encrypt(object.pass));
                SendServerList(c);
                MainClass.Log("OK!\n", false);
                
            }
            else
            {  
              r.locale = false;
              r.msg = "Invalid credentials!";
              r.localeId = 0;
              r.state = AuthState.InvalidCredentials; 
              MainClass.Log("invalid credentials!\n", false);
              
            }
            Server.SendTo(c, r);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void SendServerList(Connection c)
    {
        ServerListing sl = new ServerListing();
        sl.state = ServerListingState.Begin;
        Server.SendTo(c, sl);
        
        AddServerResponse r = new AddServerResponse();
        
        for(String serverName: servers.keySet())
        {
            r.name = serverName;
            r.address = servers.get(serverName).address;
            Server.SendTo(c, r);
        }
        
        sl.state = ServerListingState.End;
        Server.SendTo(c, sl);        
    }

    private static void SendTo(Connection c, Object r)
    {
        c.sendUDP(r);
    }
    
    private static boolean Logged(AuthPacketRequest object)
    {
        if(accounts.get(object.login) != null)
            return true;
        else
            return false;
    }

    public static String encrypt(String string)
    {
        try
        {
            byte[] bytesOfMessage = string.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            return new String(thedigest, "UTF-8");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasAccount(ConnectedClient c)
    {
        if(c.account != null){
        if(accounts.containsKey(c.account.login))
        return true;
        else
        return false;}
        else
        return false;
    }

    public static void removeAccount(ConnectedClient c)
    {
      accounts.remove(c.account.login);        
    }

}
