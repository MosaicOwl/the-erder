package com.alastar.game;

import java.io.Serializable;

import com.alastar.game.enums.TileType;
import com.badlogic.gdx.math.Vector3;

public class Tile extends Transform implements Serializable
{

    private static final long serialVersionUID = 7420787875382412198L;
    public TileType           type;
    public boolean            passable         = false;

    public Tile(Vector3 pos, TileType t, boolean p)
    {
        super(pos);
        this.type = t;
        this.passable = p;
    }

}
