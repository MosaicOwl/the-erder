package ru.alastar.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

import com.alastar.game.enums.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Connection;

import ru.alastar.database.DatabaseClient;
import ru.alastar.entities.Entity;
import ru.alastar.entities.Player;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.PacketGenerator;
import ru.alastar.main.net.ServerListener;
import ru.alastar.main.net.responses.AddCharacterResponse;
import ru.alastar.main.net.responses.LoadWorldResponse;
import ru.alastar.main.net.responses.SetDataResponse;
import ru.alastar.main.net.responses.UpdatePlayerResponse;
import ru.alastar.world.ServerWorld;

public class Server {
	public static com.esotericsoftware.kryonet.Server server = null;
	private int port;
	private static ArrayList<ConnectedClient> clients;
	private static final Logger logger = Logger.getLogger("Server");
	private static ArrayList<ServerWorld> worlds = new ArrayList<ServerWorld>();

	public Server(int port) {
		this.port = port;
		clients = new ArrayList<ConnectedClient>();
	}

	public void run() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		try {
			server = new com.esotericsoftware.kryonet.Server();
			server.start();
			server.bind(port, port + 1);
			server.addListener(new ServerListener(server));
		} catch (IOException e) {
			System.out.println("[Сервер]: Ошибка! " + e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		logger.log(Level.SEVERE, "Запуск сервера...");
		try {
			ExecutorService service = Executors.newCachedThreadPool();
			service.submit(new Runnable() {
				public void run() {
					CommandListen();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Server(25565).run();
	}

	public static void AuthProcess(ResultSet r, Connection a) {
		ConnectedClient c = getClient(a);
		try {
			c.accountId = r.getInt("id");
			c.mail = r.getString("mail");
			c.pass = r.getString("password");
			ResultSet characters = DatabaseClient
					.commandExecute("SELECT * FROM entities WHERE accountId = "
							+ c.accountId);
			AddCharacterResponse r1;
			while (characters.next()) {
				c.characters.add(characters.getString("caption"));
				Log(characters.getString("caption"));
			}

			for (int i = 0; i < c.characters.size(); ++i) {
				r1 = new AddCharacterResponse();
				r1.name = c.characters.get(i);
				PacketGenerator.generatePacketTo(a, r1);
				Log(c.characters.get(i));

			}
			// Log("Send auth success");
			PacketGenerator.generatePacketTo(a, true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void Save() {

	}

	public static void Init() {
		LoadWorlds();
	}

	public static void LoadWorlds() {
		try {
			File worldDir = new File(System.getProperty("user.dir")
					+ "\\worlds\\");
			// Log(System.getProperty("user.dir") + "\\worlds\\ is Directory: "
			// + worldDir.isDirectory());
			String fileName = "";
			ServerWorld w;
			com.alastar.game.World clientW;
			FileInputStream f_in = null;
			ObjectInputStream obj_in = null;
			File worldFile;
			if (worldDir.listFiles().length > 0) {
				for (int i = 0; i < worldDir.listFiles().length; ++i) {
					worldFile = worldDir.listFiles()[i];
					fileName = worldFile.getName();
					fileName = fileName.replaceAll(".bin", "");
					f_in = new FileInputStream(worldFile);
					obj_in = new ObjectInputStream(f_in);
					w = new ServerWorld(i, fileName);
					clientW = (com.alastar.game.World) obj_in.readObject();
					w.tiles = clientW.tiles;
					w.version = clientW.version;
					w.zMax = clientW.zMax;
					w.zMin = clientW.zMin;
					Log("Loaded world " + w.name + " id: " + w.id);
					worlds.add(w);
				}
				obj_in.close();
				f_in.close();
			} else {
				Log("World files not found!");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void CommandListen() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));

			for (;;) {
				String line;

				line = in.readLine();

				if (line == null) {
					continue;
				}

				if ("save".equals(line.toLowerCase())) {
					Save();
					continue;
				}

				if ("stop".equals(line.toLowerCase())) {
					server.close();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Log(String s) {
		logger.info(s);
	}

	public static void addClient(ConnectedClient c) {
		if (!clients.contains(c)) {
			clients.add(c);
			logger.log(Level.SEVERE,
					"Клиент добавлен! Клиентов: " + +clients.size());
		}
	}

	public static void removeClient(InetSocketAddress a) {
		for (int i = 0; i < clients.size(); ++i) {
			if (clients.get(i).address.equals(a)) {
				if (clients.get(i).player != null) {
					clients.get(i).player.removeYourself();
				}
				clients.remove(i);
				logger.log(Level.SEVERE,
						"Клиент выпилен! Клиентов: " + clients.size());
				break;
			}
		}
	}

	public static ConnectedClient getClient(Connection ctx) {
		for (int i = 0; i < clients.size(); ++i) {
			if (ctx.getRemoteAddressUDP() == clients.get(i).address) {
				return clients.get(i);
			}
		}
		return null;
	}

	public static ConnectedClient getClient(int playerId) {
		for (int i = 0; i < clients.size(); ++i) {
			if (clients.get(i).player.id == playerId) {
				return clients.get(i);
			}
		}
		return null;
	}

	public static void SendData(Connection ctx, String nick) {
		Server.Log("Character Choose! Nick: " + nick);
		try {

			ConnectedClient c = getClient(ctx);

			ResultSet playerInfo = DatabaseClient
					.commandExecute("SELECT * FROM entity WHERE caption = '"
							+ nick + "'");

			if (playerInfo.next()) {
				Server.Log("parsing data");

				ServerWorld w = getWorld(playerInfo.getInt("worldId"));
				Server.Log("w");

				int id = playerInfo.getInt("id");
				Server.Log("id");

				Vector3 vec = new Vector3(playerInfo.getInt("x"),
				        playerInfo.getInt("y"), playerInfo.getInt("z"));
				EntityType t = EntityType.valueOf(playerInfo.getString("type"));
				c.player = new Player(id, w, nick, vec, t, c.accountId);

				Server.Log("Add entity!");

				c.player.world.AddEntity(c.player);
				Server.Log("Send data!");
			}
			// c.player.world.SendTiles(c);
			SetDataResponse sdResponse = new SetDataResponse();
			sdResponse.id = c.player.id;
			PacketGenerator.generatePacketTo(c.connection,
					sdResponse);

			c.player.world.SendEntities(c);
			LoadWorldResponse lResponse = new LoadWorldResponse();
			lResponse.name = c.player.world.name;
			PacketGenerator.generatePacketTo(c.connection,
					lResponse);
			Server.Log("Send load world packet!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static ServerWorld getWorld(int j) {
		for (int i = 0; i < worlds.size(); ++i) {
			if (worlds.get(i).id == j)
				return worlds.get(i);
		}
		return null;
	}

	public static void TryCreate(Connection ctx, String nick, Race r) {
		ResultSet nickReq = DatabaseClient
				.commandExecute("SELECT * FROM entities WHERE caption = '" + nick
						+ "'");
		try {
			if (!nickReq.next()) {
				AddCharacterResponse r1 = new AddCharacterResponse();
				r1.name = nick;
				PacketGenerator
						.generatePacketTo(ctx, r1);
				Player p = CreatePlayer(nick, r, getClient(ctx));
				saveEntity(p);
				Log("Create Successful! Nick: " + p.caption + " Race: "
						+ p.type.name());

			} else {
				PacketGenerator.generatePacketTo(ctx, 8);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void saveEntity(Entity p) {
		ResultSet r = null;
		try {
			r = DatabaseClient
					.commandExecute("SELECT * FROM entities WHERE id="
							+ p.id);
			if (r.next()) {
				r.close();

				r = DatabaseClient.commandExecute("UPDATE entities SET caption='"
						+ p.caption
						+ "', z= "
						+ p.position.z
						+ ", x="
						+ p.position.x
						+ ", y="
						+ p.position.y
						+ ", worldId="
						+ p.world.id
						+ ", type='"
						+ p.type.name()						
						+ "', staffLevel=0, gold=0, WHERE id=" + p.id);
			} else {
				r.close();

				r = DatabaseClient
						.commandExecute("INSERT INTO entities(id, accountId, staffLevel, gold, caption, x, y, z, worldId, type)  VALUES("
								+ p.id
								+ ","
								+ p.accountId
								+ ",0,0,'"
								+ p.caption
								+ "',"
								+ p.position.x
								+ ","
								+ p.position.y
								+ ","
								+ p.position.z
								+ ","
								+ p.world.id
								+ ",'"
								+ p.type.name()
								+"')");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private static Player CreatePlayer(String nick, Race r, ConnectedClient c) {
		return new Player(getFreeId(), getWorld(0), nick, new Vector3(0, 0, 0),
				EntityType.valueOf(r.name()), c.accountId);
	}

	private static int getFreeId() {
		try {
			ResultSet rs = DatabaseClient
					.commandExecute("SELECT max(id) as id FROM entities");
			int i = 0;
			if (rs.next()) {
				i = rs.getInt("id");
			}
			return i + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void handleMove(Vector2 vec, Player p) {
		if (!p.inBattle) {
			// Server.Log("handle Move");
			p.Move((int) vec.x, (int) vec.y);
		} else {

		}
	}

	public static void UpdateEntityPosition(Entity entity) {
		ConnectedClient c;
		UpdatePlayerResponse r;
		for (int i = 0; i < entity.world.entities.size(); ++i) {
			c = Server.getClient(entity.world.entities.get(i).id);
			r = new UpdatePlayerResponse();
			r.id = entity.id;
			r.updTypeOrdinal = UpdateType.Position.ordinal();
			r.x = (int) entity.position.x;
			r.y = (int) entity.position.y;
			r.z = (int) entity.position.z;
			PacketGenerator.generatePacketTo(c.connection, r);
		}
	}
}
