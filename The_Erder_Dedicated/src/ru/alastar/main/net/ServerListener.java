package ru.alastar.main.net;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

import ru.alastar.database.DatabaseClient;
import ru.alastar.main.Server;
import ru.alastar.main.net.requests.AuthPacketRequest;
import ru.alastar.main.net.requests.CharacterChooseRequest;
import ru.alastar.main.net.requests.CreateCharacterRequest;
import ru.alastar.main.net.requests.InputRequest;
import ru.alastar.main.net.requests.MessagePacketRequest;
import ru.alastar.main.net.requests.RegistrationPacketRequest;
import ru.alastar.main.net.responses.AuthPacketResponse;

public class ServerListener extends Listener {

	private static final Logger logger = Logger.getLogger("Packet Handler");

	public ServerListener(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(AuthPacketRequest.class);
		kryo.register(MessagePacketRequest.class);
		kryo.register(RegistrationPacketRequest.class);
		kryo.register(CharacterChooseRequest.class);
		kryo.register(CreateCharacterRequest.class);
		kryo.register(InputRequest.class);

		logger.log(Level.INFO, "Начинаем принимать пакеты.");
	}

	public void received(Connection connection, Object object) {
		if (object instanceof AuthPacketRequest) {
			AuthPacketRequest request = (AuthPacketRequest) object;
			ResultSet r = DatabaseClient
					.commandExecute("SELECT * FROM accounts WHERE login = '"
							+ request.login + "'");
			AuthPacketResponse response = new AuthPacketResponse();
			try {
				r.next();
				if (r.getString("password").equals(request.pass)) {
					Server.Log("Auth: Login - " + request.login + " pass - "
							+ request.pass + " successful!");
					Server.AuthProcess(r, connection);
				} else {
					response.success = false;
					PacketGenerator.generatePacketTo(connection,
							response);
					Server.Log("Auth: Login - " + request.login + " pass - "
							+ request.pass + " unsuccessful!");

				}
			} catch (Exception e) {
				Server.Log("Ошибка! " + e.getLocalizedMessage());
				response.success = false;
				PacketGenerator.generatePacketTo(connection,
						response);
			}
			Server.Log("Auth: Login - " + request.login + " pass - "
					+ request.pass);
		}
		else if(object instanceof AuthPacketRequest)
		{
			
		}
	}

	@Override
	public void connected(Connection connection) {
		if (Server.getClient(connection) == null) {
			Server.addClient(new ConnectedClient(connection));
		} else {
			PacketGenerator.generatePacketTo(connection, 11);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		Server.removeClient(connection.getRemoteAddressUDP());
		connection.close();
	}

}