package ru.alastar.game;

import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddStatResponse;

public class Skill
{
    public String name;
    public int    value;
    public int    maxValue;
    public float  hardness;
    public String primaryStat;
    public String secondaryStat;
    public int state; // 0 - down, 1 - locked, 2 - raise
    
    public Skill(String n, int v, int mV, float h, String pS, String sS, int s)
    {
        this.name = n;
        this.value = v;
        this.maxValue = mV;
        this.hardness = h;
        this.primaryStat = pS;
        this.secondaryStat = sS;
        this.state = s;
    }

    public void raise(int how, Entity e)
    {
        if(state == 2){
        value += how;
          if(Server.getClient(e) != null){
            try
            {
                AddStatResponse r = new AddStatResponse();
                r.name = name;
                r.sValue = value;
                r.mValue = maxValue;
                Server.SendTo(Server.getClient(e).connection, r);
            } catch (Exception e1)
            {
                Server.handleError(e1);
            }
          }
    }}
}
