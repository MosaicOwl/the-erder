package ru.alastar.mapeditor.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapEditor extends Game {
	

    SpriteBatch batch;
	
	public int positionX = 0;
	public int positionY = 0;
    public static int positionZ = 1;

	@Override
	public void create () {
		batch = new SpriteBatch();
		GameManager.LoadContent();
        Gdx.graphics.setDisplayMode(800, 800, false);
		this.setScreen(new EditorScreen(this));
	}

	
	
	@Override
	public void render () {
		super.render();
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			++positionX;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT))
		{
			--positionX;
		}
		if(Gdx.input.isKeyPressed(Keys.UP))
		{
			++positionY;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN))
		{
			--positionY;
		}

	}
}
