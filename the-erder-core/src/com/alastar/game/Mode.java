package com.alastar.game;

import java.util.ArrayList;

import com.alastar.game.enums.ModeType;
import com.badlogic.gdx.math.Vector3;

public class Mode {

	public ModeType type;

	public ArrayList<Entity> entities;
	public World world;

    public ArrayList<Interpolation> interps;
    
	public Mode(ModeType t) {
		this.type = t;
		entities = new ArrayList<Entity>();
		interps = new ArrayList<Interpolation>();
	}

	public void AddInter(Interpolation i)
	{
	    interps.add(i);
	}
	
	public void RemoveInter(Interpolation i)
	{
	    interps.remove(i);
	}
	
	public Entity getEntityById(int i) {
		for (Entity e : entities) {
			if (e.id == i) {
				return e;
			}
		}
		return null;
	}

	public void Off() {
	}

	public void handleUpdate(int id, Vector3 vec) {

		try{
	    getEntityById(id).setPosition(vec);    
	    
		}catch(Exception e)
		{
		   e.printStackTrace();
		}
	}

	public void setWorld(World w)
	{
	    this.world = w;
	}
	
    public void removeEntity(int i)
    {
        getEntityById(i).Remove();
        entities.remove(getEntityById(i));
    }

    public void addEntity(Entity p)
    {
        entities.add(p);
    }

    public void handleUpdate(int id, boolean val)
    {
        Entity e = getEntityById(id);
        if(e != null)
        e.warMode = val;        
    }

    public void handleRemoveEntity(int id2)
    {
        Entity e = getEntityById(id2);
        if(e != null){
            removeEntity(id2);
        }
    }

    public void StepInterpolations()
    {
        for(Interpolation i: interps)
        {
            i.Step();
        }
       RemoveUnused();
    }

    private void RemoveUnused()
    {
        for(Interpolation i: interps)
        {
            if(i.finished){
            RemoveInter(i);
            RemoveUnused();
            break;
            }
        }        
    }

    public void drawAllByZ(int z)
    {
        for(TexturedObject o: entities)
        {
            if(o.getZ() == z)
            {
                o.Draw(ErderGame.batch, 0, 0);   
            }
        }      
    }

}
