package ru.alastar.game;

public class Statistic
{
    public String name;
    public int    value;
    public int    maxValue;
    public float  hardness;
    public int state; // 0 - down, 1 - locked, 2 - raise

    public Statistic(String n, int v, int mV, float h, int state)
    {
        this.name = n;
        this.value = v;
        this.maxValue = mV;
        this.hardness = h;
        this.state = state;
    }
}
