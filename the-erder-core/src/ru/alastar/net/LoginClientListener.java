package ru.alastar.net;


import ru.alastar.main.net.requests.*;
import ru.alastar.main.net.responses.*;

import com.alastar.game.AuthState;
import com.alastar.game.ServerListingState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class LoginClientListener extends Listener {
    public Kryo kryo;
	public LoginClientListener(EndPoint e) {
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

		//System.out.println("Client Handler have been started!");
	}

	public void received(Connection connection, Object object) {
		  if (object instanceof AuthResponse) {
		      AuthResponse r = (AuthResponse)object;
	          LoginClient.handleAuth(r);
		 } 
		 else if (object instanceof AddServerResponse) {
		     AddServerResponse r = (AddServerResponse)object;   
		     LoginClient.addServer(r);
         } 
         else if (object instanceof ServerListing) {
             ServerListing r = (ServerListing)object;   
             LoginClient.handleServerListing(r);
         }

	}

	@Override
	public void connected(Connection connection) {

	}

	@Override
	public void disconnected(Connection connection) {
		connection.close();
	}

}
