package ru.alastar.net;

public class PacketGenerator {
	public static void generatePacket(Object values) {
		Client.client.sendUDP(values);
	}
}
