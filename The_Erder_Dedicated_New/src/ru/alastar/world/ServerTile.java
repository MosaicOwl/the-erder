package ru.alastar.world;

import ru.alastar.physics.IPhysic;
import ru.alastar.physics.PhysicalData;

import com.alastar.game.Tile;
import com.alastar.game.Transform;
import com.alastar.game.enums.TileType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ServerTile extends Transform implements IPhysic
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public TileType           type;
    public boolean            passable         = false;
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
            pData = new PhysicalData((int) this.position.z, false);

            groundBox = new PolygonShape();
            groundBox.setAsBox(0.5f, 0.5f);

            fixture = groundBody.createFixture(groundBox, 0.0f);
            fixture.setUserData((IPhysic)this);

            groundBox.dispose();
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
            pData = new PhysicalData((int) this.position.z, false);

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
}
