package ru.alastar.main;

public class Skill
{
    public String name;
    public int    value;
    public int    maxValue;
    public float  hardness;
    public String primaryStat;
    public String secondaryStat;
    public int state; // 0 - down, 1- locked, 2 - raise

    public Skill(String n, int v, int mV, float h, String pS, String sS, int st)
    {
        this.name = n;
        this.value = v;
        this.maxValue = mV;
        this.hardness = h;
        this.primaryStat = pS;
        this.secondaryStat = sS;
        this.state = st;
    }

}
