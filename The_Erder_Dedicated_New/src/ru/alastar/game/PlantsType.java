package ru.alastar.game;

import java.util.Date;

import ru.alastar.game.worldwide.Location;

public class PlantsType
{

    public String   plantName;
    public Date     finish;
    public Location loc;

    public PlantsType(String n, Date f, Location l)
    {
        this.plantName = n;
        this.finish = f;
        this.loc = l;
    }

}
