package ru.alastar.physics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.alastar.game.Entity;
import ru.alastar.game.IUpdate;
import ru.alastar.main.Main;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddProjectileResponse;
import ru.alastar.main.net.responses.RemovePacket;
import ru.alastar.main.net.responses.UpdateProjectileResponse;
import ru.alastar.world.ServerWorld;

import com.alastar.game.enums.ProjectileType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class BaseProjectile implements IUpdate, IPhysic
{
    public int           id;
    public Entity        shooter;
    public Vector3       from;
    public float         lifeTime;
    public float         speed;
    public float         maxSpeed;
    public ServerWorld   world;
    private Date         m_StartTime;
    public boolean       m_Shooted = false;
    public ProjectileType type;
    private ArrayList<IUpdate> allAround;
    public float angle;
    // Physics
    BodyDef              bodyDef;
    public Body          body;
    FixtureDef           fixtureDef;
    Fixture              fixture;
    CircleShape          circle;
    private Timer        updateTimer;
    private PhysicalData pData;
    Vector2 add = new Vector2(1,1);

    public BaseProjectile(int id, Vector3 from, Entity shooter, float angle)
    {
        this.id = id;
        this.from = from;
        this.shooter = shooter;
        this.world = shooter.world;
        this.allAround = new ArrayList<IUpdate>();
        this.angle = angle;
        Server.RegisterProjectile(this);
    }

    public void Launch()
    {
        m_StartTime = new Date();
        // physics
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.bullet = true;
        Vector2 vec = new Vector2(from.x, from.y);
        // What the function rotate vector properly????!
        //add.rotate((float) -angle);
        //add.setAngle((float) angle);
        //add.setAngleRad((float) angle);
        add.setAngle(angle);
        Main.Log("[DEBUG]","Angle: " + add.angle());
        //add.scl(speed);
        vec.add(add);
        bodyDef.position.set(vec.x, vec.y);

        bodyDef.linearDamping = 1.5F;
        pData = new PhysicalData((int) from.z, true);
        body = world.getPhysic().createBody(bodyDef);

        circle = new CircleShape();
        circle.setRadius(0.1f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData((IPhysic)this);
        circle.dispose();
        if (updateTimer == null)
        {
            final BaseProjectile e = this;
            updateTimer = new Timer();
            updateTimer.scheduleAtFixedRate(new TimerTask()
            {

                @Override
                public void run()
                {
                    if (lifeTime * 10000 + m_StartTime.getTime() > new Date().getTime())
                    {
                        if(body.getLinearVelocity().x > -maxSpeed && body.getLinearVelocity().y > -maxSpeed
                                && body.getLinearVelocity().x < maxSpeed && body.getLinearVelocity().y < maxSpeed){
                           // body.applyLinearImpulse(add, getPosition(), true);
                            body.applyForceToCenter(/*add.scl(speed)*/ add, true);
                            }
                            Server.UpdatePosition(e);
                    } else
                    {
                        Destroy();
                        cancel();
                    }

                }
            }, 100, 100);
        }
        m_Shooted = true;  
        world.AddEntity(this);
        world.UpdateNear(this);
    }

    public void Destroy()
    {
        Server.RemoveProjectile(this);
        world.RemoveEntity(this);
    }

    @Override
    public void UpdateTo(ConnectedClient c)
    {
        if(c != null){
            AddProjectileResponse r = new AddProjectileResponse();
            r.id = this.id;
            r.projectileType = this.type;
            r.x = this.getPosition().x;
            r.y = this.getPosition().y;
            r.z = this.from.z;
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
            r.type = TypeId.getTypeId(Type.Projectile);
            Server.SendTo(c, r);
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
        return TypeId.getTypeId(Type.Projectile);
    }

    @Override
    public void UpdateAround()
    {
       UpdateProjectileResponse r = new UpdateProjectileResponse();
       r.id = this.id;
       r.x = this.getPosition().x;
       r.y = this.getPosition().y;
       r.z = this.from.z;
       r.type = this.type;
       ConnectedClient c;
       IUpdate upd;
       for(int i = allAround.size() - 1; i > -1; i--)
       {
        upd = allAround.get(i);
        if(upd != null) // i told u that it was called sanity?
        {
            if(upd.getType() == TypeId.getTypeId(Type.Entity))
            {
                c = Server.getClient((Entity)upd);
                if(c != null)
                {
                    Server.SendTo(c, r);
                }
            }
        }
       }
    }

    @Override
    public ServerWorld getWorld()
    {
        return world;
    }

    @Override
    public Vector2 getPosition()
    {
        if (m_Shooted)
            return body.getPosition();
        else
            return new Vector2(from.x, from.y);
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
