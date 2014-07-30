package ru.alastar.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener
{

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void beginContact(Contact contact)
    {

    }

    @Override
    public void postSolve(Contact arg0, ContactImpulse arg1)
    {

    }

    @Override
    public void preSolve(Contact arg0, Manifold arg1)
    {
        final PhysicalData pData1 = (PhysicalData) ((IPhysic)arg0.getFixtureA()
                .getUserData()).getData();
        final PhysicalData pData2 = (PhysicalData) ((IPhysic)arg0.getFixtureB()
                .getUserData()).getData();
        if (!pData1.getIgnore() && !pData2.getIgnore()
                && pData1.getZ() != pData2.getZ())
            arg0.setEnabled(false);
    }
};
