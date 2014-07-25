package ru.alastar.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import com.alastar.game.Tile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import ru.alastar.enums.EntityType;
import ru.alastar.game.systems.BattleSystem;
import ru.alastar.game.systems.SkillsSystem;
import ru.alastar.game.systems.gui.NetGUIAnswer;
import ru.alastar.game.systems.gui.NetGUIInfo;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.game.systems.gui.hadlers.GUIHandler;
import ru.alastar.game.systems.gui.hadlers.PlayerButtonGUIHandler;
import ru.alastar.main.Main;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddEntityResponse;
import ru.alastar.main.net.responses.AddEquipResponse;
import ru.alastar.main.net.responses.RemoveEntityResponse;
import ru.alastar.main.net.responses.SpeechResponse;
import ru.alastar.main.net.responses.TargetInfoResponse;
import ru.alastar.main.net.responses.TargetResponse;
import ru.alastar.world.ServerWorld;

public class Entity extends Transform
{

    public int                           id           = -1;
    public String                        caption      = "Generic Entity";
    public EntityType                    type         = EntityType.Human;
    public Stats                         stats;
    public Skills                        skills;

    public boolean                       invul        = false;
    public float                         invulTime    = 15;                                 // in seconds
    public static int                    startHits    = 15;
    public ServerWorld                   world;
    public int                           height       = 2;
    public long                          lastMoveTime = System.currentTimeMillis();
    public Hashtable<String, NetGUIInfo> gui          = new Hashtable<String, NetGUIInfo>();
    public Hashtable<String, GUIHandler> handlingGUIs = new Hashtable<String, GUIHandler>();

    public Entity                        target       = null;
    public boolean                       warMode      = false;
    public Timer                         battleTimer  = null;
    public ArrayList<Entity>             entitiesAround;
  
    public boolean                       isAI          = false;
    public ru.alastar.game.ai.AI         AI            = null;
    public float                         speedMod      = 4F;
    
    //Physics
    BodyDef                              bodyDef;
    public Body                          body;
    FixtureDef                           fixtureDef;
    Fixture                              fixture;
    CircleShape                          circle;
    private int                          MAX_VELOCITY = 3;
    private Timer                        updateTimer;
    
    public Entity(int i, String c, EntityType t, float f, float g, float h,
            Skills sk, Stats st, ServerWorld w)
    {
        super((int) h);
        this.id = i;
        this.caption = c;
        this.type = t;
        this.skills = sk;
        this.stats = st;
        this.world = w;
        this.entitiesAround = new ArrayList<Entity>();
    
        // physics
        bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(f, g);
        bodyDef.linearDamping = 1.5F;

        // Create our body in the world using our body definition
        body = world.getPhysic().createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        circle = new CircleShape();
        circle.setRadius(0.5f);
     
        // Create a fixture definition to apply our shape to
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        // Create our fixture and attach it to the body
        fixture = body.createFixture(fixtureDef);
        
        circle.dispose();
        if(updateTimer == null){ 
            final Entity e = this;
            updateTimer = new Timer();
            updateTimer.scheduleAtFixedRate(new TimerTask(){

                @Override
                public void run()
                {
                    if(e.body.isActive())
                    Server.UpdateEntityPosition(e);
                    else{ 
                    }
                    
                }}, 100, 100);
        }
    }

    public void RemoveYourself(int aId)
    {   
        updateTimer.cancel();
        tryStopAttack();
        for(Entity e: this.entitiesAround)
        {
            if(e != this)
            e.tryRemoveNearEntity(this);
        }
        world.RemoveEntity(this);

        Server.saveEntity(this, aId);
        
        Server.unloadInventory(id);
        Server.unloadEquip(id);
        
        Server.inventories.remove(id);
        Server.equips.remove(id);
        Server.entities.remove(id);
       // Main.Log("[LOGIN]", "Entity removed. Count:" + Server.entities.size());

    }

    public void setRebirthHitsAmount()
    {
        this.stats.set("Hits", startHits, this);
        world.UpdateTargetInfo(this, stats);
    }

    public void toggleWar(boolean val)
    {
        this.warMode = val;
        this.world.UpdateEntity(this);
    }


    public boolean tryMove(float x, float y)
    {
        // Main.Log("[DEBUG]", "Try move. x:" + x + " y:" + y);
            /*int obstacleHeight = 0;
            for (int i = 0; i < height; ++i)
            {
                if (world.GetTile(((int) (this.pos.x + Math.floor(x))),
                        ((int) (this.pos.y + Math.floor(y))), (int) this.pos.z + i) != null)
                    ++obstacleHeight;
            }
            // Main.Log("[INPUT]","obstacle height: " + obstacleHeight);
            if (obstacleHeight < height)
            {
                this.pos.x += x;
                this.pos.y += y;
                this.pos.z += obstacleHeight;
                CheckIfInAir();
                Server.UpdateEntityPosition(this);
               // lastMoveTime = System.currentTimeMillis();
               //  Main.Log("[INPUT]","player moved " + this.pos.x + " " + this.pos.y );
                return true;
            } 
            else
            {
                Tile t = world.GetTile(((int) (this.pos.x + Math.floor(x))),
                        ((int) (this.pos.y + Math.floor(y))), (int) this.pos.z);
                // Main.Log("[INPUT]","Tile is not null");
                if (t != null)
                {
                    if (t.passable)
                    {
                        // Main.Log("[INPUT]","Tile is passable!");

                        this.pos.x += x;
                        this.pos.y += y;
                        this.pos.z += 1;
                        CheckIfInAir();

                        Server.UpdateEntityPosition(this);
                        //lastMoveTime = System.currentTimeMillis();
                        // Main.Log("[INPUT]","player moved");
                        return true;
                    } else
                    {
                        // Main.Log("[INPUT]","Tile is not passable!");

                        return false;
                    }
                } else
                {
                    // Main.Log("[INPUT]","path is passable!");

                    this.pos.x += x;
                    this.pos.y += y;
                    CheckIfInAir();

                    Server.UpdateEntityPosition(this);
                    //lastMoveTime = System.currentTimeMillis();
                   //  Main.Log("[INPUT]","player moved");
                    return true;
                }

            }
        */
        if(System.currentTimeMillis() - lastMoveTime > 100){
        Vector2 vel = this.body.getLinearVelocity();
        Vector2 pos = this.body.getPosition(); 

      //   Main.Log("[INPUT]","max velocity " + MAX_VELOCITY + " x velocity " + vel.x + " y velocity " + vel.y);
        if (vel.x > -MAX_VELOCITY && vel.y > -MAX_VELOCITY && vel.x < MAX_VELOCITY && vel.y < MAX_VELOCITY) {
            this.body.applyLinearImpulse(x * speedMod, y * speedMod, pos.x + x, pos.y + y, true);
            CheckIfInAir();
            lastMoveTime = System.currentTimeMillis();
            return true;     

       }
        }
        else
            return false;
        
           return false;
      }

    private void CheckIfInAir()
    {
        Tile t = world.GetTile(new Vector3(body.getPosition().x, body.getPosition().y, z - 1));
        for (int z = this.z; z > world.zMin; --z)
        {
            t = world.GetTile(new Vector3(body.getPosition().x, body.getPosition().y, z));
            if (t == null)
            {
                this.z = z;
            } else
                break;
        }
    }

    public void AddGUI(NetGUIInfo info)
    {
        this.gui.put(info.name, info);
    }

    public boolean haveGUI(String name)
    {
        if (getGUI(name) != null)
            return true;
        else
            return false;
    }

    public NetGUIInfo getGUI(String name)
    {
        return this.gui.get(name);
    }

    public void tryAttack(Entity e)
    {
        if (e != null)
        {
            if (SkillsSystem.getChanceFromSkill(this.skills.vals.get("Swords")) > Server.random
                    .nextFloat())
            {
                if(this.isAI)
                {
                    this.AI.OnHitEntity(e);
                }
                e.dealDamage(this, BattleSystem.calculateDamage(this, e));
                SkillsSystem
                        .tryRaiseSkill(this, this.skills.vals.get("Swords"));
            } else
            {
                Server.sendSpeech(this, "miss");
                SkillsSystem
                        .tryRaiseSkill(this, this.skills.vals.get("Swords"));
            }
        }
    }

    private void dealDamage(Entity entity, int calculateDamage)
    {          
        int curHits = this.stats.get("Hits").value;

        if ((curHits - calculateDamage) > 0)
        {
        if (SkillsSystem.getChanceFromSkill(this.skills.vals.get("Parrying")) > Server.random
                .nextFloat())
        {          
            SkillsSystem.tryRaiseSkill(this, this.skills.vals.get("Parrying"));
        }
        else
        {
            this.stats.set("Hits", curHits - calculateDamage, this);
            world.UpdateTargetInfo(this, this.stats);
            SkillsSystem.tryRaiseSkill(this, this.skills.vals.get("Parrying"));
        } 
        
        if(this.isAI)
        {
            this.AI.OnGetDamage(entity, calculateDamage);
        } 
        
        if (target == null)
        {
            target = entity;
            startAttack(entity);
        }
        Server.sendSpeech(this, Integer.toString(calculateDamage));
        }
        else
        {
            Server.EntityDead(this, entity);
        }
    }

    public void tryStopAttack()
    {
        if (battleTimer != null)
            battleTimer.cancel();
        battleTimer = null;
        NullfillTarget();
        if(this.isAI)
            this.AI.OnLostTarget();
    }

    private void NullfillTarget()
    {
        if(!isAI){
        TargetResponse r = new TargetResponse();
        r.id = -1;
        Server.SendTo(Server.getClient(this).connection, r);
        }
        target = null;
    }

    public void startAttack(Entity entity)
    {
        if (battleTimer == null)
        {
            // System.out.println("Start Attack. Weapon speed: " +
            // (long)BattleSystem.getWeaponSpeed(this)*1000);
            if (entity.target == null)
            {
                entity.target = this;
                entity.startAttack(this);
            }
            battleTimer = new Timer();
            final Entity e = this;
            battleTimer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (target != null)
                    {
                        // System.out.println("target is not null");
                        if (target.stats.get("Hits").value > 0)
                        {
                            // System.out.println("and it's alive");
                            // System.out.println("Dst is " +
                            // target.pos.dst2(e.pos));
                            if (target.body.getPosition().dst2(e.body.getPosition()) <= BattleSystem
                                    .getWeaponRange(e))
                            {
                                // System.out.println("and reachable for weapon("+target.pos.dst(e.pos)+")");

                                if (!target.invul)
                                {
                                    // System.out.println("and not invul");
                                    tryAttack(target);
                                } else
                                {
                                    target = null;
                                    battleTimer.cancel();
                                    battleTimer = null;
                                }
                            }
                        } else
                        {
                            target = null;
                            battleTimer.cancel();
                            battleTimer = null;
                        }
                    } else
                    {
                        target = null;
                        battleTimer.cancel();
                        battleTimer = null;
                    }
                }
            }, 0, (long) BattleSystem.getWeaponSpeed(this) * 1000);
        }
    }

    public void AddGUIHandler(String string, GUIHandler guiHandler)
    {
        handlingGUIs.put(string, guiHandler);
    }

    public void RemoveGUIHandler(String string)
    {
        handlingGUIs.remove(string);
    }

    public void invokeGUIHandler(NetGUIAnswer r, ConnectedClient c)
    {
        if (handlingGUIs.containsKey(r.name))
            handlingGUIs.get(r.name).handle(r.value.split(" "), c);
    }

    public void closeGUI(String string)
    {
        this.gui.remove(string.toLowerCase());
        NetGUISystem.closeGUI(string, this);
    }

    public void actToTarget(Entity e)
    {
        if (e != this)
        {
            TargetResponse r = new TargetResponse();
            r.id = e.id;
            
            TargetInfoResponse tir = new TargetInfoResponse();
            tir.id = e.id;
            tir.hits = e.stats.get("Hits").value;
            tir.mhits = e.stats.get("Hits").maxValue;
            
            if(this.isAI)
                AI.OnTarget(e);
            else{
                Server.SendTo(Server.getClient(this).connection, tir);
                Server.SendTo(Server.getClient(this).connection, r);
            }
            
            if (warMode)
            { 
                if (target != e)
            {
                Server.warnEntity(this, "You're attacking " + e.caption
                        + "!", 1);
                Server.warnEntity(e, "You were attacked by " + this.caption
                        + "!", 1);
            }  
                this.target = e;

                startAttack(e);
              //  Main.Log("[DEBUG]", "Player " + this.caption + " attacks "
               //         + e.caption + "!");


            } else
            {  
           //     Main.Log("[DEBUG]", "Player " + this.caption
             //           + " just like to click on " + e.caption);
            }
        }
    }

    public void ProcessDropdown(ConnectedClient c)
    {
        if (c.controlledEntity.haveGUI("dropdown"))
        {
            c.controlledEntity.closeGUI("dropdown");
        }

        NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("dropdown",
                new Vector2(this.body.getPosition().x, this.body.getPosition().y + this.z), new Vector2(
                        50, 50), "", "com.alastar.game.gui.GUIDropdown", "",
                "Player"), c);

        c.controlledEntity.AddGUIHandler("dropdown",
                new PlayerButtonGUIHandler());
        if (isAI)
        {
            Server.sendDropdownFor(AI, c, this.body.getPosition().x,
                    this.body.getPosition().y + this.z);
        }
    }

    public void handleSpeech(String msg, Entity e)
    {
        if (isAI)
        {
            AI.OnHear(e, msg);
        } 
        else if(Server.getClient(this) != null)
        {
            SpeechResponse r = new SpeechResponse();
            r.msg = "\'" + msg + "\'";
            r.id = e.id;
            Server.SendTo(Server.getClient(this).connection, r);
        }
    }

    public void tryRemoveNearEntity(Entity ent)
    {
        if(entitiesAround.contains(ent)){
            entitiesAround.remove(ent);
         //   Main.Log("[DEBUG]","Remove " + ent.caption + " from near of " + caption + ". Count: " + entitiesAround.size());
        RemoveEntityResponse rer = new RemoveEntityResponse();
        rer.id = ent.id;
        
        ConnectedClient c = Server.getClient(this);
        
        if(c != null)
        Server.SendTo(c.connection, rer);
        
        if(isAI && ent.id != this.id)
            AI.OnLostEntity(ent);
        }
    }

    public void tryAddToNear(Entity e)
    {     

        if(!entitiesAround.contains(e)){
        entitiesAround.add(e);
       // Main.Log("[DEBUG]","Add " + e.caption + " to the near of " + caption + ". Count: " + entitiesAround.size());

        AddEntityResponse r = new AddEntityResponse();
        ConnectedClient c = Server.getClient(this);
        
        r.caption = e.caption;
        r.id = e.id;
        r.x = (int) e.body.getPosition().x;
        r.y = (int) e.body.getPosition().y;
        r.z = (int) e.z;
        r.type = e.type;
        r.warMode = e.warMode;
        
        if(c != null){
            Server.SendTo(c.connection, r);
            Equip eq = Server.getEquip(e);
            AddEquipResponse aer = new AddEquipResponse();
            Item it;
            for(String slot: eq.contents.keySet())
            {
                it = eq.contents.get(slot).item;
                if(it != null)
                {
                    aer.amount = it.amount;
                    aer.attrs = it.attributes.values;
                    aer.captiion = it.caption;
                    aer.eid = e.id;
                    aer.id = it.id;
                    aer.slot = slot;
                    aer.type = it.type;
                   // Main.Log("[DEBUG]", "Slot " + slot + " was sended!");
                    Server.SendTo(c.connection, aer);
                }
            }  
        }
        
        if(isAI && e.id != this.id)
            AI.OnSeeEntity(e);
        
        }
        
    }

    public boolean alreadyTargets()
    {
        if(target == null)
        return false;
        else
        return true;
    }
}
