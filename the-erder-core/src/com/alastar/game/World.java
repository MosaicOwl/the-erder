package com.alastar.game;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;

public class World implements Serializable
{

    /**
	 * 
	 */
    private static final long               serialVersionUID = -3791347989965503519L;
    public int                              version          = 0;
    public HashMap<Vector3, TexturedObject> tiles;
    public int                              id               = 0;

    public int                              xMax;
    public int                              yMax;
    public int                              zMax;
    public int                              xMin;
    public int                              yMin;
    public int                              zMin;

    public World(int id, int ver, HashMap<Vector3, TexturedObject> tiles)
    {
        this.id = id;
        this.version = ver;
        this.tiles = tiles;

        xMax = 1;
        yMax = 1;
        zMax = 1;

        xMin = 0;
        yMin = 0;
        zMin = 0;
    }

    public TexturedObject getTileAbove(Vector3 position)
    {
        for (int z = (int) (position.z + 1); z <= zMax; ++z)
        {
            if (tiles.get(new Vector3(position.x, position.y, z)) != null)
            {
                return tiles.get(new Vector3(position.x, position.y, z));
            }
        }
        return null;
    }

    public boolean isUnderTile(Vector3 position)
    {
        for (int z = (int) (position.z + 1); z <= zMax; ++z)
        {
            if (tiles.get(new Vector3(position.x, position.y, z)) != null)
            {
                return true;
            }
        }
        return false;
    }
}
