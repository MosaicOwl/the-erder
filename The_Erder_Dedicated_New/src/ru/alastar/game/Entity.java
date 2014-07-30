package ru.alastar.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.alastar.game.enums.UpdateType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import ru.alastar.enums.EntityType;
import ru.alastar.game.projectiles.TestProjectile;
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
import ru.alastar.main.net.responses.RemovePacket;
import ru.alastar.main.net.responses.SpeechResponse;
import ru.alastar.main.net.responses.TargetInfoResponse;
import ru.alastar.main.net.responses.TargetResponse;
import ru.alastar.main.net.responses.UpdatePlayerResponse;
import ru.alastar.physics.IPhysic;
import ru.alastar.physics.PhysicalData;
import ru.alastar.world.ServerTile;
import ru.alastar.world.ServerWorld;

public class Entity extends Transform implements IUpdate, IPhysic
{

    public int                           id           = -1;
    public String                        caption      = "Generic Entity";
    public EntityType                    type         = EntityType.Human;
    public Stats                         stats;
    public Skills                        skills;

    public boolean                       invul        = false;
    public float                         invulTime    = 15;                                 // in
                                                                                             // seconds
    public static int                    startHits    = 15;
    public ServerWorld                   world;
    public int                           height       = 2;
    public long                          lastMoveTime = System.currentTimeMillis();
    public Hashtable<String, NetGUIInfo> gui          = new Hashtable<String, NetGUIInfo>();
    public Hashtable<String, GUIHandler> handlingGUIs = new Hashtable<String, GUIHandler>();

    public Entity                        target       = null;
    public boolean                       warMode      = false;
    public Timer                         battleTimer  = null;
    public ArrayList<IUpdate>            allAround;

    public boolean                       isAI         = false;
    public ru.alastar.game.ai.AI         AI           = null;
    public float                         speedMod     = 4F;

    // Physics
    public BodyDef                       bodyDef;
    public Body                          body;
    FixtureDef                           fixtureDef;
    Fixture                              fixture;
    CircleShape                          circle;
    private int                          MAX_VELOCITY = 3;
    private Timer                        updateTimer  = null;
    private PhysicalData                 pData;

    private boolean                      active       = false;

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
        this.allAround = new ArrayList<IUpdate>();

        // Physics
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(f, g);
        bodyDef.linearDamping = 1.5F;
    }

    public Vector2 getCurrentPosition()
    {
        if (active)
            return body.getPosition();
        else
            return bodyDef.position;
    }

    public void setActive()
    {
        body = world.getPhysic().createBody(bodyDef);
        pData = new PhysicalData(this.z, false);
        // body.setUserData(pData);

        circle = new CircleShape();
        circle.setRadius(0.5f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData((IPhysic)this);

        active = true;
        circle.dispose();
        final Entity e = this;
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                if (e.body.isActive())
                    Server.UpdatePosition(e);
                else
                {
                }

            }
        }, 100, 100);

    }

    @Override
    public void UpdatePhysicalData(int z, boolean b)
    {
        this.pData.setZ(z);
        this.pData.setIgnore(b);
    }

    public void RemoveYourself(int aId)
    {
        if (active)
        {
            updateTimer.cancel();

            tryStopAttack();

            body.destroyFixture(fixture);
            world.getPhysic().destroyBody(body);

            for (IUpdate e : allAround)
            {
                if (e != this)
                    e.tryRemoveNear(this);
            }
            world.RemoveEntity(this);

            Server.saveEntity(this, aId);

            Server.unloadInventory(id);
            Server.unloadEquip(id);

            Server.inventories.remove(id);
            Server.equips.remove(id);
            Server.entities.remove(id);
        }
        // Main.Log("[LOGIN]", "Entity removed. Count:" +
        // Server.entities.size());

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
        /*
         * int obstacleHeight = 0; for (int i = 0; i < height; ++i) { if
         * (world.GetTile(((int) (this.pos.x + Math.floor(x))), ((int)
         * (this.pos.y + Math.floor(y))), (int) this.pos.z + i) != null)
         * ++obstacleHeight; } // Main.Log("[INPUT]","obstacle height: " +
         * obstacleHeight); if (obstacleHeight < height) { this.pos.x += x;
         * this.pos.y += y; this.pos.z += obstacleHeight; CheckIfInAir();
         * Server.UpdateEntityPosition(this); // lastMoveTime =
         * System.currentTimeMillis(); // Main.Log("[INPUT]","player moved " +
         * this.pos.x + " " + this.pos.y ); return true; } else { Tile t =
         * world.GetTile(((int) (this.pos.x + Math.floor(x))), ((int)
         * (this.pos.y + Math.floor(y))), (int) this.pos.z); //
         * Main.Log("[INPUT]","Tile is not null"); if (t != null) { if
         * (t.passable) { // Main.Log("[INPUT]","Tile is passable!");
         * 
         * this.pos.x += x; this.pos.y += y; this.pos.z += 1; CheckIfInAir();
         * 
         * Server.UpdateEntityPosition(this); //lastMoveTime =
         * System.currentTimeMillis(); // Main.Log("[INPUT]","player moved");
         * return true; } else { // Main.Log("[INPUT]","Tile is not passable!");
         * 
         * return false; } } else { // Main.Log("[INPUT]","path is passable!");
         * 
         * this.pos.x += x; this.pos.y += y; CheckIfInAir();
         * 
         * Server.UpdateEntityPosition(this); //lastMoveTime =
         * System.currentTimeMillis(); // Main.Log("[INPUT]","player moved");
         * return true; }
         * 
         * }
         */
        if (System.currentTimeMillis() - lastMoveTime > 100)
        {
            Vector2 vel = this.body.getLinearVelocity();
            Vector2 pos = this.body.getPosition();

            // Main.Log("[INPUT]","max velocity " + MAX_VELOCITY +
            // " x velocity " + vel.x + " y velocity " + vel.y);
            if (vel.x > -MAX_VELOCITY && vel.y > -MAX_VELOCITY
                    && vel.x < MAX_VELOCITY && vel.y < MAX_VELOCITY)
            {
                this.body.applyLinearImpulse(x * speedMod, y * speedMod, pos.x
                        + x, pos.y + y, true);
                CheckZ(pos, new Vector2(pos.x + x, pos.y + y));
                CheckIfInAir();
                UpdatePhysicalData((int) this.z, false);
                lastMoveTime = System.currentTimeMillis();
                return true;

            }
        } else
            return false;

        return false;
    }

    private void CheckZ(Vector2 from, Vector2 to)
    {
        ServerTile toTile = world.GetTile(new Vector3((int) to.x, (int) to.y,
                this.z));
        if (toTile != null)
        {
            int obstacleHeight = 0;
            ServerTile toTileAbove;
            for (int i = 1; i <= this.height; ++i)
            {
                toTileAbove = world.GetTile(new Vector3((int) to.x, (int) to.y,
                        this.z + i));
                if (toTileAbove != null)
                    ++obstacleHeight;
            }
            if (obstacleHeight < 1)
            {
                if (toTile.passable)
                {
                    // Main.Log("[ZCheck]", "New z! Its now " + z + " after " +
                    // (toTile.position.z + 1));
                    this.z = (int) toTile.position.z + 1;
                }
            }
        }

    }

    private void CheckIfInAir()
    {
        ServerTile t;
        for (int z = this.z - 1; z > world.zMin; --z)
        {
            t = world.GetTile(new Vector3((int) body.getPosition().x,
                    (int) body.getPosition().y, z));
            if (t == null)
            {
                this.z = z;
                // Main.Log("[ZCheck]", "Fallen! now z is " + this.z);
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
                if (this.isAI)
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
            if (SkillsSystem.getChanceFromSkill(this.skills.vals
                    .get("Parrying")) > Server.random.nextFloat())
            {
                SkillsSystem.tryRaiseSkill(this,
                        this.skills.vals.get("Parrying"));
            } else
            {
                this.stats.set("Hits", curHits - calculateDamage, this);
                world.UpdateTargetInfo(this, this.stats);
                SkillsSystem.tryRaiseSkill(this,
                        this.skills.vals.get("Parrying"));
            }

            if (this.isAI)
            {
                this.AI.OnGetDamage(entity, calculateDamage);
            }

            if (target == null)
            {
                target = entity;
                startAttack(entity);
            }
            Server.sendSpeech(this, Integer.toString(calculateDamage));
        } else
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
        if (this.isAI)
            this.AI.OnLostTarget();
    }

    private void NullfillTarget()
    {
        if (!isAI)
        {
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
                            if (target.body.getPosition().dst2(
                                    e.body.getPosition()) <= BattleSystem
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

            if (this.isAI)
                AI.OnTarget(e);
            else
            {
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
                // Main.Log("[DEBUG]", "Player " + this.caption + " attacks "
                // + e.caption + "!");

            } else
            {
                // Main.Log("[DEBUG]", "Player " + this.caption
                // + " just like to click on " + e.caption);
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
                new Vector2(this.body.getPosition().x,
                        this.body.getPosition().y + this.z),
                new Vector2(50, 50), "", "com.alastar.game.gui.GUIDropdown",
                "", "Player"), c);

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
        } else if (Server.getClient(this) != null)
        {
            SpeechResponse r = new SpeechResponse();
            r.msg = "\'" + msg + "\'";
            r.id = e.id;
            Server.SendTo(Server.getClient(this).connection, r);
        }
    }

    @Override
    public void tryAddToNear(IUpdate e)
    {
        if (!allAround.contains(e))
        {
            allAround.add(e);
            e.UpdateTo(Server.getClient(this));
        }
        /*
         * AddEntityResponse r = new AddEntityResponse(); ConnectedClient c =
         * Server.getClient(this);
         * 
         * r.caption = e.caption; r.id = e.id; r.x = (int)
         * e.body.getPosition().x; r.y = (int) e.body.getPosition().y; r.z =
         * (int) e.z; r.type = e.type; r.warMode = e.warMode;
         * 
         * if(c != null){ Server.SendTo(c.connection, r); Equip eq =
         * Server.getEquip(e); AddEquipResponse aer = new AddEquipResponse();
         * Item it; for(String slot: eq.contents.keySet()) { it =
         * eq.contents.get(slot).item; if(it != null) { aer.amount = it.amount;
         * aer.attrs = it.attributes.values; aer.captiion = it.caption; aer.eid
         * = e.id; aer.id = it.id; aer.slot = slot; aer.type = it.type; //
         * Main.Log("[DEBUG]", "Slot " + slot + " was sended!");
         * Server.SendTo(c.connection, aer); } } }
         * 
         * if(isAI && e.id != this.id) AI.OnSeeEntity(e);
         * 
         * }
         */
    }

    public boolean alreadyTargets()
    {
        if (target == null)
            return false;
        else
            return true;
    }

    @Override
    public void UpdateTo(ConnectedClient c)
    {
        AddEntityResponse r = new AddEntityResponse();

        r.caption = caption;
        r.id = id;
        r.x = (int) body.getPosition().x;
        r.y = (int) body.getPosition().y;
        r.z = (int) z;
        r.type = type;
        r.warMode = warMode;

        if (c != null)
        {
            Server.SendTo(c.connection, r);
            Equip eq = Server.getEquip(this);
            AddEquipResponse aer = new AddEquipResponse();
            Item it;
            for (String slot : eq.contents.keySet())
            {
                it = eq.contents.get(slot).item;
                if (it != null)
                {
                    aer.amount = it.amount;
                    aer.attrs = it.attributes.values;
                    aer.captiion = it.caption;
                    aer.eid = this.id;
                    aer.id = it.id;
                    aer.slot = slot;
                    aer.type = it.type;
                    Server.SendTo(c.connection, aer);
                }
            }
        }
    }

    @Override
    public void RemoveTo(ConnectedClient c)
    {
        RemovePacket r = new RemovePacket();
        r.id = this.id;
        r.type = TypeId.getTypeId(Type.Entity);
        
        if (c != null)
            Server.SendTo(c.connection, r);
    }

    @Override
    public void tryRemoveNear(IUpdate i)
    {
        if (allAround.contains(i))
        {
            allAround.remove(i);
            i.RemoveTo(Server.getClient(this));
        }
    }

    @Override
    public int getType()
    {
        return TypeId.getTypeId(Type.Entity);
    }

    @Override
    public void UpdateAround()
    {
        world.UpdateNear(this);
        ConnectedClient c;
        UpdatePlayerResponse r = new UpdatePlayerResponse();
        r.id = id;
        r.updType = UpdateType.Position;
        r.x = body.getPosition().x;
        r.y = body.getPosition().y;
        r.z = z;

        if (Server.getClient(this) != null)
            Server.SendTo(Server.getClient(this).connection, r);

        for (IUpdate ent : allAround)
        {
            if (ent.getType() == TypeId.getTypeId(Type.Entity))
            {
                c = Server.getClient((Entity) ent);

                if (c != null)
                    Server.SendTo(c.connection, r);
            }
        }

    }

    @Override
    public ServerWorld getWorld()
    {
        return world;
    }

    @Override
    public Vector2 getPosition()
    {
        return this.body.getPosition();
    }

    @Override
    public ArrayList<IUpdate> getAround()
    {
        return allAround;
    }

    @Override
    public PhysicalData getData()
    {
        return pData;
    }

    public void act(double angle)
    {
        if(warMode)
        {
          TestProjectile t = new TestProjectile(Server.getProjFreeId(), new Vector3(this.getPosition().x, this.getPosition().y, this.z), this, angle);
          t.Launch();
          Main.Log("[ACTION]"," War action with angle: " + angle);
        }
        else
        {
           Main.Log("[ACTION]"," Action with angle: " + angle);
        }
    }

    @Override
    public int getId()
    {
        return id;
    }
}
