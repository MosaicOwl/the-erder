package com.alastar.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ru.alastar.net.Client;

import com.alastar.game.lang.LanguageManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ErderGame extends Game {

    public static SpriteBatch batch = null;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
    public static boolean canMove = true;

	@Override
	public void create() {
		batch = new SpriteBatch();

        Vars.AddVar("screenWidth", Gdx.graphics.getWidth());
        Vars.AddVar("screenHeight", Gdx.graphics.getHeight());
        Vars.AddVar("balancedScreenHeight", 1024 / Gdx.graphics.getHeight());
        Vars.AddVar("balancedScreenWidth", 1280 / Gdx.graphics.getWidth());
        
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            Gdx.graphics.setDisplayMode(
                    Gdx.graphics.getDesktopDisplayMode().width,
                    Gdx.graphics.getDesktopDisplayMode().height, false);
            Gdx.graphics.setTitle("The Erder");
        }
        Gdx.graphics.setVSync(true);
        Client.game = this;
        
        try
        {
            Client.StartClient();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        GameManager.LoadContent();

        this.setScreen(new MainScreen(this));
	}
	
	public static void LoadWorld(String worldName) {
	        try {
	            System.out.println("Load World");

	            File file = null;
	            for (int i = 0; i <= GameManager.getWorldsDataDir().length; ++i) {
	                System.out.println(GameManager.getWorldsDataDir()[i].name());
	                System.out.println(worldName);

	                if (GameManager.getWorldsDataDir()[i].name().equals(
	                        worldName + ".bin")) {
	                    file = GameManager.getWorldsDataDir()[i].file();
	                    System.out.println("File have been found!");
	                    break;
	                }

	            }

	            FileInputStream f_in = new FileInputStream(file);

	            ObjectInputStream obj_in = new ObjectInputStream(f_in);
	            Map.handleWorld((World) obj_in.readObject());
	            System.out.println("Current world zMin: "
	                    + Map.world.zMin + " zMax: "
	                    + Map.world.zMax);
	            System.out.println("File have been loaded!");

	            obj_in.close();
	            f_in.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		LanguageManager.dispose();
	}
}
