package ru.alastar.world;

import com.alastar.game.Tile;
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
    public boolean            passable         = false;
    //Physics
    PolygonShape              groundBox;
    BodyDef                   groundBodyDef;
    Body                      groundBody;
    public ServerTile(Vector3 pos, TileType t, boolean p, ServerWorld world)
    {
        super(pos);
        this.type = t;
        this.passable = p;
        if(!p){
        groundBodyDef =new BodyDef();  
        groundBodyDef.position.set(new Vector2(pos.x, pos.y));  
        groundBody = world.getPhysic().createBody(groundBodyDef);  
        groundBody.setUserData((int)this.position.z);
        groundBox = new PolygonShape();  
        groundBox.setAsBox(0.5f, 0.5f);
        groundBody.createFixture(groundBox, 0.0f); 
        groundBox.dispose();
        }
    }
    public ServerTile(Tile t, ServerWorld w)
    {
        super(t.position);
        this.type = t.type;
        this.passable = t.passable;
        if(!passable){
            groundBodyDef =new BodyDef();  
            groundBodyDef.position.set(new Vector2(t.position.x, t.position.y));  
            groundBody = w.getPhysic().createBody(groundBodyDef);  
            groundBody.setUserData((int)this.position.z);
            groundBox = new PolygonShape();  
            groundBox.setAsBox(0.5f, 0.5f);
            groundBody.createFixture(groundBox, 0.0f); 
            groundBox.dispose();
        }
    }
}
