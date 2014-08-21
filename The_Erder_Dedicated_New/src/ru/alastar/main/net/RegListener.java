package ru.alastar.main.net;

import ru.alastar.main.net.requests.AccountRequest;
import ru.alastar.main.net.requests.AuthServerRequest;
import ru.alastar.main.net.responses.ProcessLoginResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class RegListener extends Listener
{

    public static Kryo  kryo;
    public static float packetDelay = 100F;

    public RegListener(EndPoint e)
    {
        kryo = e.getKryo();

        // –егистраци€ пакетов. *ƒолжна выполн€тьс€ в такой же
        // последовательности, что и на сервере!*
        kryo.setRegistrationRequired(true);
        kryo.setAsmEnabled(true);

        kryo.register(ServerState.class);

        kryo.register(AuthServerRequest.class);
        kryo.register(AccountRequest.class);
        kryo.register(ProcessLoginResponse.class);
    }

    public void received(Connection connection, Object object)
    {
        if (object instanceof ProcessLoginResponse)
        {
            Server.ProcessLogin((ProcessLoginResponse) object);
        }
    }

    @Override
    public void connected(Connection connection)
    {

    }

    @Override
    public void disconnected(Connection connection)
    {
        connection.close();
    }
}
