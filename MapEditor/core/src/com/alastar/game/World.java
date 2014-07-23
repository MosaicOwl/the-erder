package com.alastar.game;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;

public class World implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791347989965503519L;
	public int version = 0;
	public HashMap<Vector3, Tile> tiles;
    public int id = 0;
	
	public int xMax;
	public int yMax;
	public int zMax;
	public int xMin;
	public int yMin;
	public int zMin;

	public World(int id, int ver, HashMap<Vector3, Tile> tiles) {
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

}
