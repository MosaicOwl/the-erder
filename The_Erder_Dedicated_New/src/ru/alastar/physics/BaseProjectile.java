package ru.alastar.physics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.alastar.game.Entity;
import ru.alastar.game.IUpdate;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.world.ServerWorld;

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
    public Entity        shooter;
    public Vector3       from;
    public float         lifeTime;
    public float         speed;
    public float         maxSpeed;
    public ServerWorld   world;
    private Date         m_StartTime;
    public boolean       m_Shooted = false;

    // Physics
    BodyDef              bodyDef;
    public Body          body;
    FixtureDef           fixtureDef;
    Fixture              fixture;
    CircleShape          circle;
    private Timer        updateTimer;
    private PhysicalData pData;

    public BaseProjectile(Vector3 from, Entity shooter)
    {
        this.from = from;
        this.shooter = shooter;
        this.world = shooter.world;
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

    }

    @Override
    public void RemoveTo(ConnectedClient c)
    {
        // TODO Add projectile packets
    }

    @Override
    public void tryRemoveNear(IUpdate i)
    {
        // TODO Add projectile packets

    }

    @Override
    public void tryAddToNear(IUpdate e)
    {
        // TODO Add projectile packets

    }

    @Override
    public int getType()
    {
        return 1;
    }

    @Override
    public void UpdateAround()
    {
        // TODO Add projectile packets

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
        return null;
    }

}
