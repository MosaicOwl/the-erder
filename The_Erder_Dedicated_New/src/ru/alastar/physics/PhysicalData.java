package ru.alastar.physics;

import ru.alastar.game.IUpdate;

public class PhysicalData
{
    private int     _Z;
    private boolean IgnoreZ;
    private IPhysic object;
    
    public PhysicalData(int z, boolean affectZ, IPhysic object)
    {
        this._Z = z;
        this.IgnoreZ = affectZ;
        this.object = object;
    }
    
    public void setObject(IPhysic o)
    {
        this.object = o;
    }

    public IPhysic getObject()
    {
        return object;
    }
    
    public void setZ(int z)
    {
        this._Z = z;
    }

    public int getZ()
    {
        return _Z;
    }

    public void setIgnore(boolean b)
    {
        this.IgnoreZ = b;
    }

    public boolean getIgnore()
    {
        return IgnoreZ;
    }
}
