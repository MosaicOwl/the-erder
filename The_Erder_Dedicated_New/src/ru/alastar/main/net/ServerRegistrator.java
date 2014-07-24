package ru.alastar.main.net;

import java.io.IOException;

import ru.alastar.main.Configuration;
import ru.alastar.main.net.requests.AuthServerRequest;

public class ServerRegistrator
{  
    public static com.esotericsoftware.kryonet.Client client = null;
    
    public static void StartClient() throws Exception {
        client = new com.esotericsoftware.kryonet.Client();
        client.start();
        client.addListener(new RegListener(client));
        Connect();
        Register();
    }

    public static void Connect() {
        try {
            int port = Integer.parseInt(Configuration.GetEntryValue("loginPort"));
            client.connect(100, Configuration.GetEntryValue("loginHost"), port, port + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void Send(Object r)
    {
        client.sendUDP(r);
    }
    
    private static void Register()
    {
        AuthServerRequest r = new AuthServerRequest();
        r.name = Configuration.GetEntryValue("name");
        r.state = Server.state;
        r.port = Configuration.GetEntryValue("port");
        Send(r);
        
    }
}
