package ru.alastar.game.ai;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.math.Vector3;

import ru.alastar.game.Entity;
import ru.alastar.game.Spell;
import ru.alastar.game.systems.SkillsSystem;
import ru.alastar.main.net.Server;

public class BaseAI implements AI
{

    public float  reactTime;
    public Entity entity;
    public Timer  reactionTimer = null;

    @Override
    public float getReactionTime()
    {
        return reactTime;
    }
    
    @Override
    public Timer getReactionTimer()
    {
        return reactionTimer;
    }
    
    public void StartChasingTarget()
    {  
     //   if(getReactionTimer() != null)
     //       getReactionTimer().cancel();
        
        reactionTimer = new Timer();
        
        getReactionTimer().scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run()
            {
                if(getEntity().target != null){
              if(SkillsSystem.getRange(entity) < entity.pos.dst(entity.target.pos))
              {
                 MoveTo(entity.target.pos);
              }
              else
              {
                  
              }}
                else
                {
                    this.cancel();
                }
            }
            
        }, 1000, (long) (1000*getReactionTime()));
    }
    
    public void MoveTo(Vector3 pos)
    {
       Vector3 v = getNextTileToTarget(this.getEntity(), this.getEntity().target);
       this.entity.tryMove((int)v.x, (int)v.y);
    }


    private Vector3 getNextTileToTarget(Entity from, Entity to)
    {
        Vector3 v = new Vector3(from.pos.x, from.pos.y, from.pos.z);
        if(from.pos.x < to.pos.x){
            v.x += 1;
            }
        else if(from.pos.x > to.pos.x){
            v.x -= 1;
            }
        else{}
        
        if(from.pos.y < to.pos.y){
            v.y += 1;
            }
        else if(from.pos.y > to.pos.y){
            v.y -= 1;
            }
        else{}
        

        return v;

    }

    @Override
    public void setReactionTime(float t)
    {
        reactTime = t;
    }

    @Override
    public Entity getEntity()
    {
        return entity;
    }

    @Override
    public void setEntity(Entity e)
    {
        entity = e;
    }

    @Override
    public void OnGetDamage(Entity from, int amt)
    {
    }

    @Override
    public void OnSeeEntity(Entity who)
    {
    }

    @Override
    public void OnHitEntity(Entity who)
    {
    }

    @Override
    public void OnLostTarget()
    {
    }

    @Override
    public void OnTarget(Entity who)
    {
    }

    @Override
    public void OnCast(Spell spell)
    {
    }

    @Override
    public void OnTick()
    {
    }

    @Override
    public void OnHear(Entity from, String words)
    {
    }

    @Override
    public void OnDropdownRequest(Entity from)
    {
    }

    @Override
    public void Save()
    {
        Server.SaveAI(this);
    }

    @Override
    public String getClassPath()
    {
        return "ru.alastar.game.ai.BaseAI";
    }

    @Override
    public void OnLostEntity(Entity who)
    {        
    }

    @Override
    public void OnDeath(Entity from)
    {        
    }

    @Override
    public void OnKill(Entity who)
    {        
    }

}
