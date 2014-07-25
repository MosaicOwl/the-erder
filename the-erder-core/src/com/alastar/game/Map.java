package com.alastar.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class Map {

	public static ArrayList<Entity> entities= new ArrayList<Entity>();
	public static World world;

    public static ArrayList<Interpolation> interps= new ArrayList<Interpolation>();
    

	public static void AddInter(Interpolation i)
	{
	    interps.add(i);
	}
	
	public static void RemoveInter(Interpolation i)
	{
	    interps.remove(i);
	}
	
	public static Entity getEntityById(int i) {
		for (Entity e : entities) {
			if (e.id == i) {
				return e;
			}
		}
		return null;
	}


	public static void handleUpdate(int id, Vector3 vec) {

		try{
		    if(getEntityById(id) != null)
	    getEntityById(id).setPosition(vec);    
	    
		}catch(Exception e)
		{
		   e.printStackTrace();
		}
	}

	public static void setWorld(World w)
	{
	    world = w;
	}
	
    public static void removeEntity(int i)
    {
        getEntityById(i).Remove();
        entities.remove(getEntityById(i));
    }

    public static void addEntity(Entity p)
    {
        entities.add(p);
    }

    public static void handleUpdate(int id, boolean val)
    {
        Entity e = getEntityById(id);
        if(e != null)
        e.warMode = val;        
    }

    public static void handleRemoveEntity(int id2)
    {
        System.out.println("Remove entity: " + id2);
        Entity e = getEntityById(id2);
        if(e != null){
            removeEntity(id2);
        }
    }

    public static void StepInterpolations()
    {
            for (int i =interps.size()-1; i > -1; i--) {
                interps.get(i).Step();
                if(interps.get(i).finished){
                    RemoveInter(interps.get(i));
                }
            }
    }
   

    public static void handleEntity(Entity p) {
        try{
        if(!haveEntity(p)){
        addEntity(p);
        p.drawMessageOverhead(p.caption);
        System.out.println("Entity added! Id: " + p.id);}
        else
        {
            p.Remove();
        }
        }

        catch(Exception e)
        {
            System.out.println("Failed to add entity beacuse mode is null! Id: " + p.id);
            e.printStackTrace();
        }
    }

    private static boolean haveEntity(Entity p)
    {
        if(getEntityById(p.id) == null)
        return false;
        else
            return true;
    }

    public static void handleTile(Tile t) {
        world.tiles.put(t.position, t);
    }

    public static void handleWorld(World readObject)
    {
        setWorld(readObject);
    }
    
    public static void drawAllByZ(int z)
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
