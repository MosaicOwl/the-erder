package com.alastar.game;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

public class Transform implements Serializable {

	private static final long serialVersionUID = -862176953291711450L;
	public Vector3 position = Vector3.Zero;
    public int z = 0;
	
	public Transform(Vector3 pos) {
		this.position = pos;
		z = (int) pos.z;
	}
	
	public void setPosition(Vector3 p)
	{ 
	    this.position.z = p.z;
	    z = (int)p.z;
	    Map.AddInter(new Interpolation(this, getTransformedVector3(p)));	  
	    //this.position = p;
	}
	
	public Vector3 getTransformedVector3(Vector3 v)
	{
	    return new Vector3(v.x, v.y + v.z, v.z);
	}
}

