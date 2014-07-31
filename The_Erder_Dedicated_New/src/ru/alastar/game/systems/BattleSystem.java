package ru.alastar.game.systems;

import com.alastar.game.IDestroyable;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;

import ru.alastar.game.Entity;
import ru.alastar.main.Main;
import ru.alastar.main.net.Server;
import ru.alastar.physics.BaseProjectile;

public class BattleSystem
{
    public static int   baseDamage = 1;
    public static float baseSpeed  = 3F; // in seconds
    public static float baseRange  = 2;

    public static int calculateDamage(Entity from, Entity to)
    {
        int d = baseDamage;
        /*
         * Calculating attack damage over here
         */

        d += from.stats.get("Strength").value / 25;
        return d;
    }

    public static float getWeaponSpeed(Entity from)
    {
        float s = baseSpeed;
        /*
         * Calculating attack speed over here
         */
        s += from.stats.get("Dexterity").value / 25;
        return s;
    }

    public static float getWeaponRange(Entity e)
    {
        return baseRange;
    }
    // type, id - Type and Id of the first participant, type2 id2 - Type and Id of the second participant
    public static void CheckForBattle(int type, int id, int type2, int id2)
    {
        int entityType = TypeId.getTypeId(Type.Entity);
        int itemType = TypeId.getTypeId(Type.Item);
        int projType = TypeId.getTypeId(Type.Projectile);
        int tileType = TypeId.getTypeId(Type.Tile);
        
       // if(type == entityType && type2 == entityType)
       // {
       //    CheckForEntityBattle(id, id2);
       // }
        if((type == entityType && type2 == projType) || (type == projType && type2 == entityType))
        {
            CheckForRangedEntityBattle(id, id2);
        }
        else if((type == tileType && type2 == projType) || (type == projType && type2 == tileType))
        {
            CheckForDestroy(id, id2);
        }
    }

    private static void CheckForDestroy(int id, int id2)
    {
        // TODO Add destroy code, now we dont need it 
        BaseProjectile p = Server.getProjectile(id);
        IDestroyable iD; 
        if(p != null)
        {
            iD = Server.getDestroyable(id2);
            if(iD != null) //sanity
            {
                //code goes here
                p.Destroy(); 
            }
            //Target can be non-destroyable tile, so destroy projectile
            p.Destroy(); 
        }
        else
        {
            p = Server.getProjectile(id2);
            iD = Server.getDestroyable(id);
            if(iD != null) //sanity
            {
                //code goes here

            } 
            if(p != null){ // sanity
            //Target can be non-destroyable tile, so destroy projectile
            p.Destroy(); 
            }
        }
    }

    private static void CheckForRangedEntityBattle(int id, int id2)
    {
      Entity e = Server.getEntity(id);
      BaseProjectile p;
      if(e == null)
      {
          e = Server.getEntity(id2);
          if(e != null)
          {
              p = Server.getProjectile(id);
              if(p != null){ //sanity
              //Code goes here
              p.Destroy(); 
              }
          }
      }
      else
      {
          p = Server.getProjectile(id2);
          if(p != null){
          //Code goes here
           p.Destroy();
          }
      }
    }

    private static void CheckForEntityBattle(int id, int id2)
    {
        Entity e1 = Server.getEntity(id);
        Entity e2 = Server.getEntity(id2);
    }

}
