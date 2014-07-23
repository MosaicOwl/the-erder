package ru.alastar.main;

public class Stat
{
    public String name;
    public int    value;
    public int    maxValue;
    public float  hardness;
    public int state; // 0 - down, 1 - locked, 2 - raise

    public Stat(String n, int v, int mV, float h, int s)
    {
        this.name = n;
        this.value = v;
        this.maxValue = mV;
        this.hardness = h;
        this.state = s;
    }
}
