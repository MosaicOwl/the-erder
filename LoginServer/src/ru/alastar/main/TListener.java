package ru.alastar.main;


import ru.alastar.requests.AuthPacketRequest;
import ru.alastar.requests.RegistrationPacketRequest;
import ru.alastar.responses.AddServerResponse;
import ru.alastar.responses.AuthResponse;
import ru.alastar.responses.RegisterResponse;
import ru.alastar.responses.ServerListing;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class TListener extends Listener
{
    public static Kryo  kryo;
    public static float packetDelay = 100F;

    public TListener(EndPoint e)
    {
        kryo = e.getKryo();

        kryo.setRegistrationRequired(true);
        kryo.setAsmEnabled(true);
        
        kryo.register(ServerListingState.class);
        kryo.register(AuthState.class);

        kryo.register(AuthPacketRequest.class);
        kryo.register(AuthResponse.class);
        kryo.register(AddServerResponse.class);
        kryo.register(ServerListing.class);
        kryo.register(RegistrationPacketRequest.class);
        kryo.register(RegisterResponse.class);

        
    }
    
    public void received(Connection connection, Object object)
    {
        if(object instanceof AuthPacketRequest)
        {
             Server.ProcessAuth((AuthPacketRequest)object, connection);
        }
        else if(object instanceof RegistrationPacketRequest)
        {
            Server.ProcessRegistration((RegistrationPacketRequest)object, connection);
        }
    }
    
    @Override
    public void connected(Connection connection)
    {
       if(!Server.hasClient(connection))
       {
           Server.addClient(connection);
       }
       else
       {
           connection.close();
       }
    }

    @Override
    public void disconnected(Connection connection)
    {
        ConnectedClient c = Server.getClient(connection);
        if(c != null){
        if(Server.hasAccount(c))
            Server.removeAccount(c);
        
        Server.removeClient(connection);
        }
        connection.close();
    }
}
