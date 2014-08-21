package ru.alastar.net;

import java.io.IOException;
import java.util.Hashtable;

import ru.alastar.main.net.responses.AddServerResponse;
import ru.alastar.main.net.responses.AuthResponse;
import ru.alastar.main.net.responses.RegisterResponse;
import ru.alastar.main.net.responses.ServerListing;

import com.alastar.game.MainScreen;
import com.alastar.game.ServerListingState;
import com.alastar.game.gui.GUICore;
import com.alastar.game.gui.constructed.ServersListGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

public class LoginClient
{
    private static String                             host    = "127.0.0.1";
    private static int                                port    = 2526;
    public static com.esotericsoftware.kryonet.Client client  = null;
    public static Hashtable<String, String>           servers = new Hashtable<String, String>();

    public static void StartClient() throws Exception
    {
        client = new com.esotericsoftware.kryonet.Client();
        client.start();
        client.addListener(new LoginClientListener(client));
    }

    public static void Connect()
    {
        try
        {
            if (Gdx.app.getType() == ApplicationType.Android)
                host = "10.0.0.2";
            client.connect(100, host, port, port + 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void Send(Object r)
    {
        client.sendUDP(r);
    }

    public static void Log(String s)
    {
        System.out.println(s);
    }

    public static void handleAuth(AuthResponse r)
    {
        switch (r.state)
        {
            case AccountNotExists:
                MainScreen.PushMessage(r.msg, true);
                break;
            case AlreadyLogged:
                MainScreen.PushMessage(r.msg, true);
                break;
            case InvalidCredentials:
                MainScreen.PushMessage(r.msg, true);
                break;
            case ServerUnavailable:
                MainScreen.PushMessage(r.msg, true);
                break;
            case Success:
                MainScreen.PushMessage(r.msg, false);
                break;
            case UnknownError:
                MainScreen.PushMessage(r.msg, true);
                break;
            default:
                break;
        }
    }

    public static void addServer(AddServerResponse r)
    {
        ((ServersListGUI) GUICore.getConstructedByName("servers_list"))
                .addServer(r.name, r.address);
        servers.put(r.name, r.address);
    }

    public static void handleServerListing(ServerListing r)
    {
        if (r.state == ServerListingState.End)
        {
            GUICore.enableOne("servers_list");
        } else
        {
            MainScreen.PushMessage("Loading Servers...", false);
        }
    }

    public static void handleRegisterResponse(RegisterResponse r)
    {
        MainScreen.PushMessage(r.reason, true);
    }
}
