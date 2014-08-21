package ru.alastar.world;

import java.util.ArrayList;

import ru.alastar.game.IUpdate;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.physics.IPhysic;
import ru.alastar.physics.PhysicalData;

import com.alastar.game.IDestroyable;
import com.alastar.game.Tile;
import com.alastar.game.Transform;
import com.alastar.game.enums.TileType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ServerTile extends Transform implements IPhysic, IDestroyable
{
    /**
     * 
     */
    private static final long serialVersionUID    = 1L;
    public int                id                  = 0; // ONLY for destroyable Tiles!!!!!
    public TileType           type;
    public boolean            passable            = false;
    public boolean            destroyable         = false;
    // Physics
    PolygonShape              groundBox;
    BodyDef                   groundBodyDef;
    Body                      groundBody;
    PhysicalData              pData;
    Fixture                   fixture;

    public ServerTile(Vector3 pos, TileType t, boolean p, ServerWorld world)
    {
        super(pos);
        this.type = t;
        this.passable = p;
        if (!p)
        {
            groundBodyDef = new BodyDef();
            groundBodyDef.position.set(new Vector2(pos.x, pos.y));

            groundBody = world.getPhysic().createBody(groundBodyDef);
            pData = new PhysicalData((int) this.position.z, false, this);

            groundBox = new PolygonShape();
            groundBox.setAsBox(0.5f, 0.5f);

            fixture = groundBody.createFixture(groundBox, 0.0f);
            fixture.setUserData((IPhysic)this);

            groundBox.dispose();
        }
    }

    public void SetDestroyable()
    {
      if(!destroyable){
        destroyable = true;
        id = Server.RegisterDestroyableTile(this);  
      }
    }
    
    public ServerTile(Tile t, ServerWorld w)
    {
        super(t.position);
        this.type = t.type;
        this.passable = t.passable;
        if (!passable)
        {
            groundBodyDef = new BodyDef();
            groundBodyDef.position.set(new Vector2(t.position.x, t.position.y));

            groundBody = w.getPhysic().createBody(groundBodyDef);
            pData = new PhysicalData((int) this.position.z, false, this);

            groundBox = new PolygonShape();
            groundBox.setAsBox(0.5f, 0.5f);

            fixture = groundBody.createFixture(groundBox, 0.0f);
            fixture.setUserData((IPhysic)this);

            groundBox.dispose();
        }
    }

    @Override
    public PhysicalData getData()
    {
        return pData;
    }

    @Override
    public void UpdatePhysicalData(int z, boolean b)
    {
        this.pData.setZ(z);
        this.pData.setIgnore(b);
    }

    @Override
    public void UpdateTo(ConnectedClient c)
    {
        // Tile doesn't need to update itself
        
    }

    @Override
    public void RemoveTo(ConnectedClient c)
    {
        // Tile doesn't need to update itself
        
    }

    @Override
    public void tryRemoveNear(IUpdate i)
    {
        // Tile doesn't need to update itself
        
    }

    @Override
    public void tryAddToNear(IUpdate e)
    {
        // Tile doesn't need to update itself
        
    }

    @Override
    public void UpdateAround()
    {
        // Tile doesn't need to update itself
    }

    @Override
    public ServerWorld getWorld()
    {
        // Always returns null
        return null;
    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(this.position.x, this.position.y);
    }

    @Override
    public ArrayList<IUpdate> getAround()
    {
        // Tile doesn't need to update itself
        return null;
    }

    @Override
    public int getType()
    {
        return TypeId.getTypeId(Type.Tile);
    }

    @Override
    public int getId()
    {
        // Always returns 0 if tile is not destroyable, else it will return its dynamic given id
        return id;
    }

    @Override
    public void DestroyDynamcally()
    {
        // TODO Add destroy code
        
    }
}
