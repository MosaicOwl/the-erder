package ru.alastar.main.net;

import com.esotericsoftware.kryonet.Connection;

import ru.alastar.main.Server;

public class PacketGenerator {

	public static void generatePacketAll(Object object) {
		Server.server.sendToAllTCP(object);
	}

	public static void generatePacketTo(Connection c,
			Object object) {
		c.sendUDP(object);
	}
}
