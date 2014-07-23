package com.alastar.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cell
{
    
    public ArrayList<TexturedObject> renders;
    
    public Cell()
    {
        renders = new ArrayList<TexturedObject>();
    }
    
    public void add(TexturedObject o)
    {
        try{
            
            if(!renders.contains(o))
        renders.add(o);
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean contains(TexturedObject o)
    {
        return renders.contains(o);
    }
    
    public void remove(TexturedObject o)
    {
        renders.remove(o);
    }

    public TexturedObject get(int id)
    {
        for(TexturedObject o: renders)
        {
            if(o.getId() == id)
                return o;
        }
        return null;
    }

    public void Draw(SpriteBatch batch, int i, int j)
    {
        try{
          for(TexturedObject o : renders)
          {
              if(o != null)
              o.Draw(batch, i, j); 
          }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
