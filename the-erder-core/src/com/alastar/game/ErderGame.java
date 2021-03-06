package com.alastar.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ru.alastar.net.Client;
import ru.alastar.net.LoginClient;

import com.alastar.game.lang.LanguageManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ErderGame extends Game
{

    public static SpriteBatch batch   = null;
    public static boolean     canMove = true;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        if (Gdx.app.getType() == ApplicationType.Desktop)
        {
            Gdx.graphics.setDisplayMode(
                    Gdx.graphics.getDesktopDisplayMode().width,
                    Gdx.graphics.getDesktopDisplayMode().height, false);
            Gdx.graphics.setTitle("The Erder");
        } 
        
        Vars.AddVar("screenWidth", (double)Gdx.graphics.getWidth());
        Vars.AddVar("screenHeight", (double)Gdx.graphics.getHeight());
        Vars.AddVar("balancedScreenHeight", 1024 / (double)Gdx.graphics.getHeight());
        Vars.AddVar("balancedScreenWidth", 1280 / (double)Gdx.graphics.getWidth());
       
        Gdx.graphics.setVSync(true);
        Client.game = this;

        try
        {
            LoginClient.StartClient();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        GameManager.LoadContent();

        this.setScreen(new MainScreen(this));
    }

    public static void LoadWorld(String worldName)
    {
        try
        {
            System.out.println("Load World");

            File file = null;
            for (int i = 0; i <= GameManager.getWorldsDataDir().length; ++i)
            {
                System.out.println(GameManager.getWorldsDataDir()[i].name());
                System.out.println(worldName);

                if (GameManager.getWorldsDataDir()[i].name().equals(
                        worldName + ".bin"))
                {
                    file = GameManager.getWorldsDataDir()[i].file();
                    System.out.println("File have been found!");
                    break;
                }

            }

            FileInputStream f_in = new FileInputStream(file);

            ObjectInputStream obj_in = new ObjectInputStream(f_in);
            Map.handleWorld((World) obj_in.readObject());
            System.out.println("Current world zMin: " + Map.world.zMin
                    + " zMax: " + Map.world.zMax);
            System.out.println("File have been loaded!");

            obj_in.close();
            f_in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render()
    {
        super.render();
    }

    public void dispose()
    {
        batch.dispose();
        LanguageManager.dispose();
    }
}
