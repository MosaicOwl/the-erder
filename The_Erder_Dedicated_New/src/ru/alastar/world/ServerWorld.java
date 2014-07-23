package ru.alastar.world;

import java.util.ArrayList;
import java.util.HashMap;

import ru.alastar.game.Entity;
import ru.alastar.game.Stats;
import ru.alastar.main.Main;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddTileResponse;
import ru.alastar.main.net.responses.ChatSendResponse;
import ru.alastar.main.net.responses.RemoveEntityResponse;
import ru.alastar.main.net.responses.RemoveTileResponse;
import ru.alastar.main.net.responses.TargetInfoResponse;
import ru.alastar.main.net.responses.UpdatePlayerResponse;

import com.alastar.game.Tile;
import com.alastar.game.enums.ModeType;
import com.alastar.game.enums.TileType;
import com.alastar.game.enums.UpdateType;
import com.badlogic.gdx.math.Vector3;

public class ServerWorld
{

    public int                    id        = 0;
    public String                 name      = "GenericWorld";

    public int                    chunkSize = 16;
    public HashMap<Vector3, Tile> tiles;
    public ArrayList<Entity>      entities;
    public int                    version   = 0;

    public int                    zMin      = -1;
    public int                    zMax      = 10;

    public int                    xMin      = -1;
    public int                    xMax      = 10;

    public int                    yMax      = 10;
    public int                    yMin      = -1;

    public ServerWorld(int i, String n, int xMx, int xMn, int yMx, int yMn,
            int zMx, int zMn)
    {
        this.id = i;
        this.name = n;
        this.xMax = xMx;
        this.xMin = xMn;
        this.yMax = yMx;
        this.yMin = yMn;
        this.zMax = zMx;
        this.zMin = zMn;
        tiles = new HashMap<Vector3, Tile>();
        entities = new ArrayList<Entity>();
    }

    public void CreateTile(int x, int y, int z, TileType type, boolean p)
    {
        AddTile(new Tile(new Vector3(x, y, z), type, p));
    }

    public void AddTile(Tile t)
    {
        AddTileResponse r = new AddTileResponse();
        ConnectedClient c;
        tiles.put(t.position, t);
        for (int i = 0; i < entities.size(); ++i)
        {
            c = Server.getClient(entities.get(i));
            if (c != null)
            {
                r.x = (int) t.position.x;
                r.y = (int) t.position.y;
                r.z = (int) t.position.z;
                r.type = t.type;
                r.mode = ModeType.World;
                Server.SendTo(c.connection, r);
            }
        }
    }

    public void AddEntity(Entity e)
    {
        entities.add(e);
        for (Entity ent : entities)
        {
            if (ent.pos.dst(e.pos) <= Server.syncDistance)
            {
                ent.tryAddToNear(e);
                e.tryAddToNear(ent);
            }
        }
    }

    public void RemoveEntity(Entity entity)
    {
        RemoveEntityResponse r = new RemoveEntityResponse();
        ConnectedClient c;
        entities.remove(entity);
        for (int i = 0; i < entities.size(); ++i)
        {
            c = Server.getClient(entities.get(i));
            if (c != null)
            {
                r.id = entity.id;
                Server.SendTo(c.connection, r);
            }
        }
    }

    public void RemoveTile(Tile t)
    {
        RemoveTileResponse r = new RemoveTileResponse();
        ConnectedClient c;
        for (int i = 0; i < entities.size(); ++i)
        {
            if (Server.getClient(entities.get(i)) != null)
            {
                c = Server.getClient(entities.get(i));
                r.x = (int) t.position.x;
                r.y = (int) t.position.y;
                r.z = (int) t.position.z;
                r.mode = ModeType.World;
                if (c != null)
                    Server.SendTo(c.connection, r);
            }
        }
        try
        {
            tiles.remove(t.position);
        } finally
        {
        }
    }

    public Tile GetTile(int x, int y, int z)
    {
        return tiles.get(new Vector3(x, y, z));
    }

    public Tile GetTile(Vector3 xyz)
    {
        return tiles.get(xyz);
    }

    public void SendTiles(ConnectedClient c)
    {
    }

    /*
     * public void SendEntities(ConnectedClient c) { Entity e; AddEntityResponse
     * r = new AddEntityResponse(); for (int i = 0; i < entities.size(); ++i) {
     * 
     * e = entities.get(i);
     * 
     * if (e.pos.dst2(c.controlledEntity.pos) <= Server.syncDistance) {
     * 
     * Main.Log("[DEBUG]", "Distance is less than " + Server.syncDistance +
     * " it's " + e.pos.dst2(c.controlledEntity.pos) + " caption " + e.caption);
     * 
     * r.caption = e.caption; r.id = e.id; r.x = (int) e.pos.x; r.y = (int)
     * e.pos.y; r.z = (int) e.pos.z; r.type = e.type; r.warMode = e.warMode;
     * r.mode = ModeType.World; Main.Log("[DEBUG]", "Send entity id: " + e.id +
     * " caption: " + e.caption + " pos: " + e.pos.toString()); if (c != null)
     * Server.SendTo(c.connection, r); } else { Main.Log("[DEBUG]",
     * "Distance is more than " + Server.syncDistance + " it's " +
     * e.pos.dst2(c.controlledEntity.pos) + " dont sync"+ " caption " +
     * e.caption); } } }
     */

    public void sendAll(String msg, String caption)
    {
        ChatSendResponse r = new ChatSendResponse();
        r.msg = "\'" + msg + "\'";
        r.sender = caption;
        ConnectedClient c;
        for (Entity e1 : entities)
        {
            c = Server.getClient(e1);
            if (c != null)
                Server.SendTo(c.connection, r);
        }
        Main.Log("[CHAT]", "(" + this.name + ")" + caption + ":" + msg);
    }

    public void sendAll(String msg, Entity e)
    {
        for (Entity e1 : e.entitiesAround)
        {
            e1.handleSpeech(msg, e);
        }
    }

    public void UpdateEntity(Entity entity)
    {
        Entity ent;
        ConnectedClient c;
        UpdatePlayerResponse r = new UpdatePlayerResponse();
        r.updType = UpdateType.Mode;
        r.id = entity.id;
        r.val = entity.warMode;
        for (int i = 0; i < entities.size(); ++i)
        {
            ent = entities.get(i);
            c = Server.getClient(ent);
            if (c != null)
                Server.SendTo(c.connection, r);
        }
    }

    public void UpdateNear(Entity ent)
    {

        // Main.Log("[DEBUG]","UpdateNear");
        for (Entity e : ent.entitiesAround)
        {
            if (e.pos.dst(ent.pos) > Server.syncDistance)
            {
                e.tryRemoveNearEntity(ent);
                ent.tryRemoveNearEntity(e);
                UpdateNear(ent);
                break;
            }
        }

        for (Entity e : entities)
        {
            if (e.pos.dst(ent.pos) <= Server.syncDistance)
            {
                e.tryAddToNear(ent);
                ent.tryAddToNear(e);
            }
        }
    }

    public void UpdateTargetInfo(Entity entity, Stats stats)
    {
        TargetInfoResponse r = new TargetInfoResponse();
        r.id = entity.id;
        r.hits = stats.get("Hits").value;
        r.mhits = stats.get("Hits").maxValue;
        for(Entity e: entity.entitiesAround)
        {
            if(!e.isAI)
            {
                if(e.target == entity)
                    Server.SendTo(Server.getClient(e).connection, r);
            }
        }
    }
    
}
