package ru.alastar.game;

import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ru.alastar.enums.EquipType;
import ru.alastar.main.net.Server;

public class Item extends Transform
{

    public int         id;
    public String      caption;
    public int         amount;
    public int         entityId;
    public EquipType   eqType;
    public Attributes  attributes;
    public ItemType type;
    public int worldId;
    
    //Physics
    BodyDef                              bodyDef;
    public Body                          body;
    FixtureDef                           fixtureDef;
    Fixture                              fixture;
    CircleShape                          circle;
    
    public Item(int i, int ei, String c, int a, float f, float g, float h,
            EquipType et, ItemType type, Attributes a1, int wId)
    {
        super((int) h);
        this.id = i;
        this.caption = c;
        this.amount = a;
        this.entityId = ei;
        this.eqType = et;
        this.type = type;
        this.attributes = a1;
        this.worldId = wId;
        
        bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(f, g);

        // Create our body in the world using our body definition
        body = Server.getWorld(wId).getPhysic().createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        circle = new CircleShape();
        circle.setRadius(1f);
     
        // Create a fixture definition to apply our shape to
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f; 
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

         // Create our fixture and attach it to the body
        fixture = body.createFixture(fixtureDef);
        
        circle.dispose();

        Server.SaveItem(this);
    }

   // public Vector2 getPos()
   // {
   //     return this.pos;
   // }

    public int getAttributeValue(String s)
    {
        return attributes.getValue(s);
    }

    public boolean setAttributeValue(String s, int v)
    {
        return attributes.setValue(s, v);
    }

    public void diffValue(String s, int i)
    {
        attributes.setValue(s, attributes.getValue(s) - i);
    }

}
