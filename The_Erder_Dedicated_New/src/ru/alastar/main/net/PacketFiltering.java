package ru.alastar.main.net;

import java.util.Date;
import java.util.Hashtable;

import ru.alastar.main.Main;

import com.esotericsoftware.kryonet.Connection;

@SuppressWarnings("rawtypes")
public class PacketFiltering
{
    public static Hashtable<Class, Boolean> filters     = new Hashtable<Class, Boolean>();
    public static float                     packetDelay = 100F;

    public static void addFilterFor(Class c, boolean f)
    {
        filters.put(c, f);
    }

    @SuppressWarnings("unused")
    public static boolean checkFilter(Class c, Connection conn)
    {
        for (Class cls : filters.keySet())
        {
            if (c.getEnclosingClass() == cls.getEnclosingClass())
            {
                if (filters.get(c) == true)
                {
                    ConnectedClient client = Server.getClient(conn);
                    if (c != null)
                    {
                        if ((new Date().getTime() - client.lastPacket.getTime()) > packetDelay)
                        {
                            client.lastPacket = new Date();
                            return true;
                        } else
                            return false;
                    } else
                    {
                        Main.Log("[ERROR]", "Connected client is null");
                        return false;
                    }
                } else
                    return true;
            }
        }
        return false;
    }
}
