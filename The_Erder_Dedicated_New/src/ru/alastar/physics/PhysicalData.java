package ru.alastar.physics;

public class PhysicalData
{
    private int     _Z;
    private boolean IgnoreZ;

    public PhysicalData(int z, boolean affectZ)
    {
        this._Z = z;
        this.IgnoreZ = affectZ;
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
