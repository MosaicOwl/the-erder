package ru.alastar.main.net.responses;

import com.alastar.game.enums.EntityType;


public class AddEntityResponse
{
    public int        id;
    public int x,y,z;
    public String     caption;
    public EntityType type;
    public boolean warMode;
}
