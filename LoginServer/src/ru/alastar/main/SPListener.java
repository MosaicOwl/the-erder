package ru.alastar.main;


import ru.alastar.requests.AccountRequest;
import ru.alastar.requests.AuthServerRequest;
import ru.alastar.responses.ProcessLoginResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class SPListener extends Listener
{
    public static Kryo  kryo;
    public static float packetDelay = 100F;

    public SPListener(EndPoint e)
    {
        kryo = e.getKryo();

        kryo.setRegistrationRequired(true);
        kryo.setAsmEnabled(true);
        
        kryo.register(ServerState.class);

        kryo.register(AuthServerRequest.class);
        kryo.register(AccountRequest.class);
        kryo.register(ProcessLoginResponse.class);

    }
    
    public void received(Connection connection, Object object)
    {
      if(object instanceof AuthServerRequest)
      {
          ServerPooler.ProcessAuth((AuthServerRequest)object, connection);
      }   
      if(object instanceof AccountRequest)
      {
          ServerPooler.ProcessAccountRequest((AccountRequest)object, connection);
      }     
    }
    
    @Override
    public void connected(Connection connection)
    {

    }

    @Override
    public void disconnected(Connection connection)
    {
        ServerInfo c = ServerPooler.getServerInfo(connection);
        if(c != null){
        if(ServerPooler.hasServer(c))
            ServerPooler.removeServer(c);
        }
        connection.close();
    }
}
