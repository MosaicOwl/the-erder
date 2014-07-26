package ru.alastar.physics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.alastar.game.Entity;
import ru.alastar.game.IUpdate;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddProjectileResponse;
import ru.alastar.main.net.responses.RemovePacket;
import ru.alastar.main.net.responses.UpdateProjectileResponse;
import ru.alastar.world.ServerWorld;

import com.alastar.game.enums.ProjectileType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BaseProjectile implements IUpdate
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
    // Physics
    BodyDef              bodyDef;
    public Body          body;
    FixtureDef           fixtureDef;
    Fixture              fixture;
    CircleShape          circle;
    private Timer        updateTimer;
    private PhysicalData pData;

    public BaseProjectile(int id, Vector3 from, Entity shooter)
    {
        this.from = from;
        this.shooter = shooter;
        this.world = shooter.world;
        this.allAround = new ArrayList<IUpdate>();
    }

    public void Launch()
    {
        m_StartTime = new Date();
        // physics
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.position.set(from.x, from.y);

        bodyDef.linearDamping = 1.5F;
        pData = new PhysicalData((int) from.z, true);
        body = world.getPhysic().createBody(bodyDef);

        circle = new CircleShape();
        circle.setRadius(0.5f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(pData);

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
                    if (lifeTime + m_StartTime.getTime() < new Date().getTime())
                    {
                        if (e.body.isActive())
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
    }

    public void Destroy()
    {

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
            r.type = 2; // 2 - Projectile
            Server.SendTo(c, r);
        }
    }
    @Override
    public void tryRemoveNear(IUpdate i)
    {
        if (allAround.contains(i))
        {
            allAround.remove(i);
            if (i.getType() == 1)
                RemoveTo(Server.getClient((Entity) i));
        }
    }

    @Override
    public void tryAddToNear(IUpdate e)
    {
        if (!allAround.contains(e))
        {
            allAround.add(e);
            if (e.getType() == 1){
                UpdateTo(Server.getClient((Entity) e));
            }
        }
    }

    @Override
    public int getType()
    {
        return 2;
    }

    @Override
    public void UpdateAround()
    {
       UpdateProjectileResponse r = new UpdateProjectileResponse();
       r.id = this.id;
       r.x = this.getPosition().x;
       r.y = this.getPosition().y;
       r.type = this.type;
       ConnectedClient c;
       for(IUpdate upd: allAround)
       {
           if(upd.getType() == 1)
           {
               c = Server.getClient((Entity)upd);
               if(c != null)
               {
                   Server.SendTo(c, r);
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

}
