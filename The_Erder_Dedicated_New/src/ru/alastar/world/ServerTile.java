package ru.alastar.world;

import com.alastar.game.Transform;
import com.alastar.game.enums.TileType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ServerTile extends Transform
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public TileType           type;
    public boolean            physic         = false;
    //Physics
    PolygonShape              groundBox;
    BodyDef                   groundBodyDef;
    Body                      groundBody;
    public ServerTile(Vector3 pos, TileType t, boolean p, ServerWorld world)
    {
        super(pos);
        this.type = t;
        this.physic = p;
        if(!p){
        // Create our body definition
        groundBodyDef =new BodyDef();  
        // Set its world position
        groundBodyDef.position.set(new Vector2(pos.x, pos.y));  

        // Create a body from the defintion and add it to the world
        groundBody = world.getPhysic().createBody(groundBodyDef);  

        // Create a polygon shape
        groundBox = new PolygonShape();  
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(20.0f, 10.0f);
        // Create a fixture from our polygon shape and add it to our ground body  
        groundBody.createFixture(groundBox, 0.0f); 
        // Clean up after ourselves
       // groundBox.dispose();
        }
    }
}
