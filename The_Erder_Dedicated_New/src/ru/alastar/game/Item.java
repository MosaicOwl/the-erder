package ru.alastar.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.alastar.game.enums.UpdateItemType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ru.alastar.enums.EquipType;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddItemResponse;
import ru.alastar.main.net.responses.RemovePacket;
import ru.alastar.main.net.responses.UpdateItemResponse;
import ru.alastar.physics.IPhysic;
import ru.alastar.physics.PhysicalData;
import ru.alastar.world.ServerTile;
import ru.alastar.world.ServerWorld;

public class Item extends Transform implements IUpdate, IPhysic
{

    public int                id;
    public String             caption;
    public int                amount;
    public int                entityId;
    public EquipType          eqType;
    public Attributes         attributes;
    public ItemType           type;
    public int                worldId;
    public boolean            active = false;
    public ArrayList<IUpdate> allAround;
    private Timer             updateTimer  = null;

    // Physics
    BodyDef                   bodyDef;
    public Body               body;
    FixtureDef                fixtureDef;
    Fixture                   fixture;
    CircleShape               circle;
    PhysicalData              pData;

    public Item(int i, int ei, String c, int a, float f, float g, float h,
            EquipType et, ItemType type, Attributes a1, int wId)
    {
        super((int) h);
        this.id = i;
        this.caption = c;
        this.amount = a;
        this.entityId = ei;
        this.eqType = et;
        this.type = type;
        this.attributes = a1;
        this.worldId = wId;
        this.allAround = new ArrayList<IUpdate>();

        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(f, g);
        bodyDef.linearDamping = 1.6F;

        Server.SaveItem(this);
    }

    public void setActive()
    {

        body = Server.getWorld(worldId).getPhysic().createBody(bodyDef);
        pData = new PhysicalData(this.z, false);

        circle = new CircleShape();
        circle.setRadius(1f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData((IPhysic)this);

        circle.dispose();
        active = true; 
        final Item e = this;
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                if (e.body.isActive()){
                    Server.UpdatePosition(e);
                    CheckIfInAir();
                }
                else
                {
                }

            }
        }, 100, 100);
    }

    public void Destroy()
    {
        if(active){
            this.body.destroyFixture(fixture);
            this.getWorld().getPhysic().destroyBody(body); 
            this.getWorld().RemoveEntity(this);
        }
    }
    
    public int getAttributeValue(String s)
    {
        return attributes.getValue(s);
    }

    public boolean setAttributeValue(String s, int v)
    {
        return attributes.setValue(s, v);
    }

    public void diffValue(String s, int i)
    {
        attributes.setValue(s, attributes.getValue(s) - i);
    }

    @Override
    public void UpdateTo(ConnectedClient c)
    {
        if (c != null)
        { // MUST DO IT ALWAYS! UpdateTo() can take an AI controlled Entity
          // that have no ConnectedClient!
            AddItemResponse r = new AddItemResponse();
            r.id = this.id;
            r.caption = this.caption;
            r.type = this.type;
            r.attrs = this.attributes.values;
            r.amount = this.amount;
            r.x = this.getPosition().x;
            r.y = this.getPosition().y;
            r.z = this.z;
            Server.SendTo(c, r);
        }
    }

    @Override
    public void RemoveTo(ConnectedClient c)
    {
        if (c != null) // MUST DO IT ALWAYS! RemoveTo() can take an AI
                       // controlled Entity that have no ConnectedClient!
        {
            RemovePacket r = new RemovePacket();
            r.id = this.id;
            r.type = TypeId.getTypeId(Type.Item);
            Server.SendTo(c, r);
        }
    }
    
    private void CheckIfInAir()
    {
        ServerTile t;
        for (int z = this.z - 1; z > getWorld().zMin; --z)
        {
            t = getWorld().GetTile(new Vector3((int) body.getPosition().x,
                    (int) body.getPosition().y, z));
            if (t == null)
            {
                this.z = z;
            } else
                break;
        }
    }
    
    @Override
    public void tryRemoveNear(IUpdate i)
    {
        if (allAround.contains(i))
        {
            allAround.remove(i);
            if (i.getType() == TypeId.getTypeId(Type.Entity))
                RemoveTo(Server.getClient((Entity) i));
        }
    }

    @Override
    public void tryAddToNear(IUpdate e)
    {
        if (!allAround.contains(e))
        {
            allAround.add(e);
            if (e.getType() == TypeId.getTypeId(Type.Entity)){
                UpdateTo(Server.getClient((Entity) e));
                }
        }
    }

    @Override
    public int getType()
    {
        return TypeId.getTypeId(Type.Item);
    }

    @Override
    public void UpdateAround()
    {
        getWorld().UpdateNear(this);
        ConnectedClient c;
        UpdateItemResponse r = new UpdateItemResponse();
        r.id = id;
        r.updType = UpdateItemType.Position;
        r.x = getPosition().x;
        r.y = getPosition().y;
        r.z = z;

        for (IUpdate ent : allAround)
        {
            if (ent.getType() == TypeId.getTypeId(Type.Entity))
            {
                c = Server.getClient((Entity) ent);

                if (c != null)
                    Server.SendTo(c.connection, r);
            }
        }
    }

    @Override
    public ServerWorld getWorld()
    {
        return Server.getWorld(worldId);
    }

    @Override
    public Vector2 getPosition()
    {
        if (active)
            return body.getPosition();
        else
            return bodyDef.position;
    }

    @Override
    public ArrayList<IUpdate> getAround()
    {
        return allAround;
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
    public int getId()
    {
        return id;
    }

}
