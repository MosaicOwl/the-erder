package ru.alastar.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import ru.alastar.game.Entity;
import ru.alastar.game.IUpdate;
import ru.alastar.game.Stats;
import ru.alastar.main.Main;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddTileResponse;
import ru.alastar.main.net.responses.ChatSendResponse;
import ru.alastar.main.net.responses.RemovePacket;
import ru.alastar.main.net.responses.RemoveTileResponse;
import ru.alastar.main.net.responses.TargetInfoResponse;
import ru.alastar.main.net.responses.UpdatePlayerResponse;
import ru.alastar.physics.CollisionListener;

import com.alastar.game.Tile;
import com.alastar.game.enums.TileType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.alastar.game.enums.UpdateType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class ServerWorld
{

    public int                          id        = 0;
    public String                       name      = "GenericWorld";

    public int                          chunkSize = 16;
    public HashMap<Vector3, ServerTile> tiles;
    public ArrayList<IUpdate>           entities;
    public int                          version   = 0;

    public int                          zMin      = -1;
    public int                          zMax      = 10;

    public int                          xMin      = -1;
    public int                          xMax      = 10;

    public int                          yMax      = 10;
    public int                          yMin      = -1;

    World                               pWorld;
    Timer                               physicsTimer;

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
        tiles = new HashMap<Vector3, ServerTile>();
        pWorld = new World(new Vector2(0, 0), true);
        entities = new ArrayList<IUpdate>();
        physicsTimer = new Timer();
        pWorld.setContactListener(new CollisionListener());
        
        physicsTimer.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                if (pWorld != null)
                    pWorld.step(1 / 45f, 6, 2);
                else
                    cancel();
            }
        }, 0, 10);

    }

    public void CreateTile(int x, int y, int z, TileType type, boolean p)
    {
        AddTile(new ServerTile(new Vector3(x, y, z), type, p, this));
    }

    public void AddTile(ServerTile t)
    {
        AddTileResponse r = new AddTileResponse();
        ConnectedClient c;
        tiles.put(t.position, t);
        IUpdate upd;
        for (int i = 0; i < entities.size(); ++i)
        {
            upd = entities.get(i);
            if(upd != null){
                if (upd.getType() == 0)
                {
                    c = Server.getClient((Entity) entities.get(i));
                    if (c != null)
                    {
                        r.x = (int) t.position.x;
                        r.y = (int) t.position.y;
                        r.z = (int) t.position.z;
                        r.type = t.type;
                        Server.SendTo(c.connection, r);
                    }
                }
            }
        }
    }
    ////
    //This function adds IUpdate!
    ////
    public void AddEntity(IUpdate i)
    {
        entities.add(i); 
        for (int j = entities.size() - 1; j > -1; j--)
        {
            if(entities.get(j) != null){ // sanity
                if (entities.get(j).getPosition().dst(i.getPosition()) <= Server.syncDistance)
                {
                    entities.get(j).tryAddToNear(i);
                    i.tryAddToNear(entities.get(j));
                }
            }
        }
        // Main.Log("[DEBUG]","Add " + e.caption +
        // " to ServerWorld.entities. Count:  " + entities.size());

    }

    public void RemoveEntity(IUpdate u)
    {
        RemovePacket r = new RemovePacket(); 
        r.id = u.getId();
        r.type = u.getType();
        ConnectedClient c;
        int index = 0;
        IUpdate upd;
        for (int i = entities.size() - 1; i > -1; i--)
        {
            upd = entities.get(i);
            if(upd != null) // sanity
            {
                if (upd.getType() == TypeId.getTypeId(Type.Entity))
                {
                    if (u.getId() == upd.getId())
                    {   
                        index = i;
                    }
                      c = Server.getClient(((Entity) upd));
                      if (c != null)
                      {
                         Server.SendTo(c.connection, r);
                      }
                    
                }
            }
        }
        entities.remove(index);

        // Main.Log("[DEBUG]","World entitys count: " + entities.size());
    }

    public void RemoveTile(Tile t)
    {
        RemoveTileResponse r = new RemoveTileResponse();
        ConnectedClient c;
        IUpdate upd;

        for (int i = 0; i < entities.size(); ++i)
        {
            upd = entities.get(i);
            if (upd.getType() == 0)
            {
                if (Server.getClient((Entity) upd) != null)
                {
                    c = Server.getClient((Entity) upd);
                    r.x = (int) t.position.x;
                    r.y = (int) t.position.y;
                    r.z = (int) t.position.z;
                    if (c != null)
                        Server.SendTo(c.connection, r);
                }
            }
        }
        try
        {
            tiles.remove(t.position);
        } finally
        {
        }
    }

    public ServerTile GetTile(int x, int y, int z)
    {
        return tiles.get(new Vector3(x, y, z));
    }

    public ServerTile GetTile(Vector3 xyz)
    {
        return tiles.get(xyz);
    }

    public void SendTiles(ConnectedClient c)
    {
    }

    public void sendAll(String msg, String caption)
    {
        ChatSendResponse r = new ChatSendResponse();
        r.msg = "\'" + msg + "\'";
        r.sender = caption;
        ConnectedClient c;

        for (IUpdate e1 : entities)
        {
            if (e1.getType() == 0)
            {
                c = Server.getClient((Entity) e1);
                if (c != null)
                    Server.SendTo(c.connection, r);
            }
        }
        Main.Log("[CHAT]", "(" + this.name + ")" + caption + ":" + msg);
    }

    public void sendAll(String msg, Entity e)
    {
        for (IUpdate e1 : e.allAround)
        {
            if (e1.getType() == 0)
                ((Entity) e1).handleSpeech(msg, e);
        }
    }

    // Updates warmode
    public void UpdateEntity(Entity entity)
    {
        IUpdate ent;
        ConnectedClient c;
        UpdatePlayerResponse r = new UpdatePlayerResponse();
        r.updType = UpdateType.Mode;
        r.id = entity.id;
        r.val = entity.warMode;
        for (int i = 0; i < entities.size(); ++i)
        {
            ent = entities.get(i);
            if (ent.getType() == 0)
            {
                c = Server.getClient((Entity) ent);
                if (c != null)
                    Server.SendTo(c.connection, r);
            }
        }
    }

    public void UpdateNear(IUpdate e2)
    {
        IUpdate e;
        for (int i = e2.getAround().size() - 1; i > -1; i--)
        {
            e = e2.getAround().get(i);
            if( e != null) //sanity
            {
                if (e.getPosition().dst(e2.getPosition()) > Server.syncDistance)
                {
                    e.tryRemoveNear(e2);
                    e2.tryRemoveNear(e);
                    UpdateNear(e2);
                    break;
                }
            }
        }
        
        for (int i = entities.size() - 1; i > -1; i--)
        {
            e = entities.get(i);
            if(e != null) //sanity
            {
                if (e.getPosition().dst(e2.getPosition()) <= Server.syncDistance)
                {
                    e.tryAddToNear(e2);
                    e2.tryAddToNear(e);
                }
            }
        }

    }

    public void UpdateTargetInfo(Entity entity, Stats stats)
    {
        TargetInfoResponse r = new TargetInfoResponse();
        r.id = entity.id;
        r.hits = stats.get("Hits").value;
        r.mhits = stats.get("Hits").maxValue;
        for (IUpdate e : entity.allAround)
        {
            if (e.getType() == 0)
            {
                if (!((Entity) e).isAI)
                {
                    if (((Entity) e).target == entity)
                        Server.SendTo(Server.getClient((Entity) e).connection,
                                r);
                }
            }
        }
    }

    public World getPhysic()
    {
        return pWorld;
    }

}
