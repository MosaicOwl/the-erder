package ru.alastar.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.alastar.game.Tile;
import com.alastar.game.enums.ModeType;
import com.alastar.game.enums.TileType;
import com.badlogic.gdx.math.Vector3;

import ru.alastar.entities.Entity;
import ru.alastar.main.Server;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.PacketGenerator;
import ru.alastar.main.net.responses.AddEntityResponse;
import ru.alastar.main.net.responses.AddTileResponse;
import ru.alastar.main.net.responses.RemoveEntityResponse;
import ru.alastar.main.net.responses.RemoveTileResponse;

public class ServerWorld {

	public int id = 0;
	public String name = "GenericWorld";

	public HashMap<Vector3, Tile> tiles = new HashMap<Vector3, Tile>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public int version = 0;
	public int zMin = -1;
	public int zMax = 10;

	public ServerWorld(int i, String n) {
		this.id = i;
		this.name = n;
		tiles = new HashMap<Vector3, Tile>();
		entities = new ArrayList<Entity>();
	}

	public void CreateTile(int x, int y, int z, TileType type, boolean p) {
		AddTile(new Tile(new Vector3(x, y, z), type, p));
	}

	public void AddTile(Tile t) {
		AddTileResponse r;
		tiles.put(t.position, t);
		for (int i = 0; i < entities.size(); ++i) {
			if (Server.getClient(entities.get(i).id) != null) {
				r = new AddTileResponse();
				r.x = (int) t.position.x;
				r.y = (int) t.position.y;
				r.z = (int) t.position.z;
				r.ordinalType = t.type.ordinal();
				r.modeOrdinal = ModeType.World.ordinal();
				PacketGenerator.generatePacketTo(Server.getClient(entities.get(i).id).connection, r);
			}
		}
	}

	public void AddEntity(Entity e) {
		Entity ent;
		AddEntityResponse r;
		for (int i = 0; i < entities.size(); ++i) {
			ent = entities.get(i);
			r = new AddEntityResponse();
			r.caption = e.caption;
			r.id = e.id;
			r.x = (int) e.position.x;
			r.y = (int) e.position.y;
			r.z = (int) e.position.z;
			r.typeOrdinal = e.type.ordinal();
			PacketGenerator.generatePacketTo(Server.getClient(ent.id).connection, r);
		}
		entities.add(e);
	}

	public void RemoveEntity(Entity entity) {
		RemoveEntityResponse r;
		entities.remove(entity);
		for (int i = 0; i < entities.size(); ++i) {
			if (Server.getClient(entities.get(i).id) != null) {
				r = new RemoveEntityResponse();
				r.id = entity.id;
				r.modeOrdinal = ModeType.World.ordinal();
				PacketGenerator.generatePacketTo(Server.getClient(entities.get(i).id).connection, r);
			}
		}
	}

	public void RemoveTile(Tile t) {
		RemoveTileResponse r;
		for (int i = 0; i < entities.size(); ++i) {
			if (Server.getClient(entities.get(i).id) != null) {
				r = new RemoveTileResponse();
				r.x = (int) t.position.x;
				r.y = (int) t.position.y;
				r.z = (int) t.position.z;
				r.modeOrdinal = ModeType.World.ordinal();
				PacketGenerator.generatePacketTo(Server.getClient(entities.get(i).id).connection, r);
			}
		}
		try {
			tiles.remove(t.position);
		} finally {
		}
	}

	public Tile GetTile(int x, int y, int z) {
		return tiles.get(new Vector3(x, y, z));
	}

	public Tile GetTile(Vector3 xyz) {
		return tiles.get(xyz);
	}

	public void SendTiles(ConnectedClient c) {
	}

	public void SendEntities(ConnectedClient c) {
		Entity e;
		AddEntityResponse r;

		for (int i = 0; i < entities.size(); ++i) {
			e = entities.get(i);
			r = new AddEntityResponse();
			r.caption = e.caption;
			r.id = e.id;
			r.x = (int) e.position.x;
			r.y = (int) e.position.y;
			r.z = (int) e.position.z;
			r.typeOrdinal = e.type.ordinal();
			Server.Log("Send entity id: " + e.id + " caption: " + e.caption
					+ " pos: " + e.position.toString());
			PacketGenerator.generatePacketTo(c.connection, r);
		}
	}
}
