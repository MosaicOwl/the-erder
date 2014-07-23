package ru.alastar.main.net;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;

import ru.alastar.entities.Player;

public class ConnectedClient {
	public int accountId = 0;
	public InetSocketAddress address = null;
	public ArrayList<String> characters = new ArrayList<String>();
	public String mail;
	public String pass;
	public Player player;
	public Connection connection = null;

	public ConnectedClient(Connection ctx) {
		this.address = ctx.getRemoteAddressUDP();
		connection = ctx;
	}

}
