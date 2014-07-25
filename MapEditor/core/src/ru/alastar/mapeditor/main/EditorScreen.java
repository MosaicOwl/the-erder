package ru.alastar.mapeditor.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JFileChooser;

import com.alastar.game.enums.*;
import com.alastar.game.Tile;
import com.alastar.game.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class EditorScreen implements Screen {
	OrthographicCamera camera;
     
	World world;
	public TileType selectedType = TileType.Grass;
	MapEditor map;
	int index = 0;
    BitmapFont font;
	public boolean switchPos = false;
	public Vector3 fPos = null;
	public Vector3 sPos = null;
	final JFileChooser load;
	final JFileChooser save;
	public boolean passableState = true;
	public boolean physiXView = false;
    public Stage stageCreate;
	public static int tileView = 16;
	final TextField verText;
	final TextField idText;
	public boolean deleteMode = false;
	
	public EditorScreen(MapEditor map)
	{
		this.map = map;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 250, 250);
		font = GameManager.getFont();
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(new InputProcessor(){
        
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
			    if(keycode == Keys.E)
			    {
                    int ret = save.showSaveDialog(null);
                    if (ret == JFileChooser.APPROVE_OPTION) {                   
                        SaveWorld(save.getCurrentDirectory()+ "/world.bin");
                    }
			    }
	             if(keycode == Keys.Q)
	                {
	                    int ret = load.showOpenDialog(null);
	                    if (ret == JFileChooser.APPROVE_OPTION) {
	                        LoadWorld(load.getSelectedFile());
	                    }   
	                } 
	            if(keycode == Keys.D)
                {
                   deleteMode = !deleteMode; 
                }
				if(keycode == Keys.P)
				{
					physiXView = !physiXView;
				}
                if(keycode == Keys.W)
                {
                     ++MapEditor.positionZ;
                }
                if(keycode == Keys.S)
                {
                     --MapEditor.positionZ;
                }
				return true;		
				}

			@Override
			public boolean keyTyped(char character) {

				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				if(amount > 0)
				{
					if(index + 1 <= TileType.values().length){
					++index;}
					else
						index = 1;
				}
				else if(amount < 0)
				{
					if(index - 1 > 0)
					--index;
					else
						index = TileType.values().length;
				}
				selectedType = TileType.values()[index - 1];
				System.out.println(selectedType);
				return true;
			}
			
		});

	    load = new JFileChooser();
		save = new JFileChooser();
		world = new World(1, 1, new HashMap<Vector3, Tile>());
	      stageCreate = new Stage();

	        Label verLabel = new Label("Версия:", GameManager.labelStyle);
	        verLabel.setPosition(50 / Vars.balancedScreenWidth,
	                150 / Vars.balancedScreenHeight);

	        verText = new TextField("1",
	                GameManager.txtFieldStyle);
	        verText.setPosition(50 / Vars.balancedScreenWidth,
	                150 / Vars.balancedScreenHeight);

	        final Label idLabel = new Label("ID:",
	                GameManager.labelStyle);
	        idLabel.setPosition(50 / Vars.balancedScreenWidth,
	                125 / Vars.balancedScreenHeight);
	        
            idText = new TextField("1",
                    GameManager.txtFieldStyle);
            idText.setPosition(50 / Vars.balancedScreenWidth,
                    100 / Vars.balancedScreenHeight);

	        final TextButton labelOpen = new TextButton(
	                "Открыть -  Q",
	                GameManager.txtBtnStyle);
	        labelOpen.setPosition(50 / Vars.balancedScreenWidth,
	                75 / Vars.balancedScreenHeight);
	        
            final TextButton labelSave = new TextButton(
                    "Сохранить  -  E",
                    GameManager.txtBtnStyle);
            labelSave.setPosition(50 / Vars.balancedScreenWidth,
                    50 / Vars.balancedScreenHeight);
            
            final TextButton labelDel = new TextButton(
                    "Ластик  -  D",
                    GameManager.txtBtnStyle);
            labelSave.setPosition(50 / Vars.balancedScreenWidth,
                    25 / Vars.balancedScreenHeight);
            
	        stageCreate.addActor(verLabel);
	        stageCreate.addActor(verText);
	        stageCreate.addActor(idLabel);
	        stageCreate.addActor(idText);
	        stageCreate.addActor(labelSave);
	        stageCreate.addActor(labelOpen); 
	        stageCreate.addActor(labelDel);

	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.x = map.positionX;
        camera.position.y = map.positionY;
        camera.position.z = MapEditor.positionZ;
		camera.update();
		map.batch.setProjectionMatrix(camera.combined);
		map.batch.begin();
		Vector3 v;

		for(int z = world.zMin; z <= MapEditor.positionZ; ++z){
		for(int x = (int) camera.position.x / GameManager.texWidth
		        - tileView; x <= (int)camera.position.x
		        / GameManager.texWidth + tileView; ++x)
		{
			for(int y = (int) camera.position.y / GameManager.texHeight
			        - tileView; y <= (int) camera.position.y
			        / GameManager.texHeight + tileView; ++y)
			{
			    v = new Vector3(x,y,z);
				if(world.tiles.get(v) != null){
				 //   System.out.println("Draw");
				    map.batch.draw(GameManager.getTexture(world.tiles.get(v).type), x * GameManager.texWidth, y * GameManager.texHeight + z * GameManager.texHeight);
				}
		    }
		}
		}
		if(physiXView){
	        for(int z = world.zMin; z <= world.zMax; ++z){
	            for(int x = (int) camera.position.x / GameManager.texWidth
	                    - tileView; x <= (int)camera.position.x
	                    / GameManager.texWidth + tileView; ++x)
	            {
	                for(int y = (int) camera.position.y / GameManager.texHeight
	                        - tileView; y <= (int) camera.position.y
	                        / GameManager.texHeight + tileView; ++y)
	                {
	                    v = new Vector3(x,y,z);
	                    if(world.tiles.get(v) != null){
	                        if(world.tiles.get(v).passable)
	                        map.batch.draw(GameManager.passable, x * GameManager.texWidth, y * GameManager.texHeight);
	                        else
	                        map.batch.draw(GameManager.notpassable, x * GameManager.texWidth, y * GameManager.texHeight);   
	                    }
	                }
	            }
	            }
		}
		Vector3 tr = camera.unproject(new Vector3((int)Gdx.input.getX(), (int)Gdx.input.getY(),  MapEditor.positionZ));
		map.batch.draw(GameManager.getTexture(selectedType), (int)tr.x ,(int)tr.y );
		
		if(switchPos)
		{
			font.draw(map.batch, "Second Pos(x:" + (int)tr.x /GameManager.texWidth + ", y:" + (int)tr.y / GameManager.texHeight + ", z: "+MapEditor.positionZ+") P: " + passableState + " D: " + deleteMode, tr.x - GameManager.texWidth / 2, tr.y -  GameManager.texHeight / 2);
		}
		else
		{
			font.draw(map.batch, "First Pos(x:" + (int)tr.x / GameManager.texWidth + ", y:" + (int)tr.y / GameManager.texHeight + ", z: "+MapEditor.positionZ+") P: " + passableState + " D: " + deleteMode, tr.x - GameManager.texWidth / 2, tr.y -  GameManager.texHeight / 2);

		}
		map.batch.end();
		


			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
		         Vector3 tr1 = camera.unproject(new Vector3((int)Gdx.input.getX(), (int)Gdx.input.getY(), MapEditor.positionZ));

			    Vector3 v1 = new Vector3((int)tr1.x / GameManager.texWidth,(int)tr1.y / GameManager.texHeight, MapEditor.positionZ);
			    if(!deleteMode){
			      world.tiles.put(v1,new Tile(v1, selectedType, passableState));
			      if(v1.x > world.xMax)
			          world.xMax = (int)v1.x;
			      if(v1.x < world.xMin)
			          world.xMin = (int)v1.x;
			      if(v1.y > world.yMax)
			          world.yMax = (int)v1.y;
			      if(v1.y < world.yMin)
			          world.yMin = (int)v1.y;
			      if(v1.z > world.zMax)
			          world.zMax = (int)v1.z;
			      if(v1.z < world.zMin)
			          world.zMin = (int)v1.z;
		         // System.out.println("Set tile at " + (int)tr1.x / GameManager.texWidth + " " + (int)tr1.y / GameManager.texHeight);
			      }
			    else
			    {
			        world.tiles.remove(v1);
			    }
			
			}

			if(Gdx.input.justTouched())
			{
			if(Gdx.input.isButtonPressed(Buttons.RIGHT))
			{
		         Vector3 tr1 = camera.unproject(new Vector3((int)Gdx.input.getX(), (int)Gdx.input.getY(), MapEditor.positionZ));
	             Vector3 v1 = new Vector3((int)tr1.x / GameManager.texWidth,(int)tr1.y / GameManager.texHeight, MapEditor.positionZ);

				if(fPos == null){
					fPos = v1;
					switchPos = true;
					}
				else if(sPos == null){
					sPos = v1;
                    Fill();
					sPos = null;
					fPos = null;
					switchPos = false;
				}
					
			}
			if(Gdx.input.isButtonPressed(Buttons.MIDDLE))
			{
			 passableState = !passableState;	
			}
		}
	   stageCreate.act(Gdx.graphics.getDeltaTime());
       stageCreate.draw();
	   Table.drawDebug(stageCreate);
	}
	
	public void Fill()
	{
		Vector3 up = sPos;
		Vector3 down = sPos;
		Vector3 left = sPos;
		Vector3 right = sPos;
	    Vector3 bottom = sPos;
	    Vector3 upper = sPos;
	    
		if(fPos.y > sPos.x)
		{
			up = fPos;
			down = sPos;
		}
		else if(fPos.y < sPos.x)
		{
			up = sPos;
			down = fPos;
		}

		if(fPos.x > sPos.x)
		{
			right = fPos;
			left = sPos;
		}
		else if(fPos.x < sPos.x)
		{
			right = sPos;
			left = fPos;
		}
	    if(fPos.z > sPos.z)
	      {
	            upper = fPos;
	            bottom = sPos;
	     }
	    else if(fPos.z < sPos.z)
	     {
	        upper = sPos;
	        bottom = fPos;
	     }
	    
        if(right.x > world.xMax)
            world.xMax = (int)right.x;
        if(left.x < world.xMin)
            world.xMin = (int)left.x;
        if(up.y > world.yMax)
            world.yMax = (int)up.y;
        if(down.y < world.yMin)
            world.yMin = (int)down.y;
        if(upper.z > world.zMax)
            world.zMax = (int)upper.z;
        if(bottom.z < world.zMin)
            world.zMin = (int)bottom.z;
        
        Vector3 vec;
		for(int x = (int) left.x; x <= right.x; ++x)
		{
			for(int y = (int)down.y; y <= up.y; ++y)
			{
			    for(int z = (int)bottom.z; z <= upper.z; ++z){
	              vec = new Vector3(x, y, z);

				if(world.tiles.containsKey(vec))
				{
				    world.tiles.remove(vec);
				}
				if(!deleteMode)
                world.tiles.put(vec,new Tile(vec, selectedType, passableState));
			    }
			}
		}
	}
	
	private void LoadWorld(File selectedFile) {
		try {
			FileInputStream f_in = new FileInputStream(selectedFile);
			ObjectInputStream obj_in = new ObjectInputStream (f_in);
			   world = (World)obj_in.readObject();
			   idText.setText(Integer.toBinaryString(world.id));
			   verText.setText(Integer.toBinaryString(world.version));

               obj_in.close();
               f_in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void SaveWorld(String path)
	{
		try {

		FileOutputStream f_out = new FileOutputStream(path);
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		        world.version = Integer.parseInt(verText.getText());
		        world.id = Integer.parseInt(idText.getText());
				obj_out.writeObject(world);
				obj_out.close();
				f_out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
