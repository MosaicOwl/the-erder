package com.alastar.game;

import ru.alastar.main.net.requests.ActInput;
import ru.alastar.main.net.requests.DropdownMenuRequest;
import ru.alastar.main.net.requests.InputRequest;
import ru.alastar.net.Client;
import ru.alastar.net.LoginClient;

import com.alastar.game.gui.GUICore;
import com.alastar.game.gui.constructed.CharacterChooseGUI;
import com.alastar.game.gui.constructed.CharacterCreateGUI;
import com.alastar.game.gui.constructed.LoadingScreenGUI;
import com.alastar.game.gui.constructed.LoginGUI;
import com.alastar.game.gui.constructed.RegisterGUI;
import com.alastar.game.gui.constructed.ServersListGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MainScreen implements Screen, InputProcessor, GestureListener
{

    final ErderGame                  game;

    public static Stage              currentStage;

    public static int                tileView = 25;

    public static Stage              gui;

    public static Label              nameLabel1;
    // public static Image back;
    public static int                id       = 0;

    public static OrthographicCamera camera;

    public static ActInput actIn;
    
    public MainScreen(ErderGame gam)
    {
        LoginClient.Connect();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, (int)(400  / Vars.getDouble("balancedScreenWidth")),
                (int)(400 / Vars.getDouble("balancedScreenHeight")));

        actIn = new ActInput();
        game = gam;

        gui = new Stage();

        currentStage = gui;

        // Constructed guis
        GUICore.addConstructedGUI(new LoginGUI(gui, "login"));
        GUICore.getConstructedByName("login").Show();

        GUICore.addConstructedGUI(new LoadingScreenGUI(gui));
        GUICore.getConstructedByName("loading_screen").Hide();

        GUICore.addConstructedGUI(new ServersListGUI(gui));
        GUICore.getConstructedByName("servers_list").Hide();

        GUICore.addConstructedGUI(new RegisterGUI(gui, "register"));
        GUICore.getConstructedByName("register").Hide();

        GUICore.addConstructedGUI(new CharacterChooseGUI(gui, "choose"));
        GUICore.getConstructedByName("choose").Hide();

        GUICore.addConstructedGUI(new CharacterCreateGUI(gui, "create"));
        GUICore.getConstructedByName("create").Hide();

        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        im.addProcessor(this);

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta)
    {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        ErderGame.batch.begin();
        ErderGame.batch.setProjectionMatrix(camera.combined);

        Map.StepInterpolations();
        if (Map.world != null && Client.controlledEntity != null)
        {
            if (Map.world.isUnderTile(Client.controlledEntity.position))
            {
                Draw(Map.world.zMin, (int) Client.controlledEntity.position.z,
                        (int) camera.position.x / GameManager.textureResolution
                                - tileView, (int) camera.position.x
                                / GameManager.textureResolution + tileView,
                        (int) camera.position.y / GameManager.textureResolution
                                - tileView, (int) camera.position.y
                                / GameManager.textureResolution + tileView);
            } else
            {
                Draw(Map.world.zMin, Map.world.zMax + 1,
                        (int) camera.position.x / GameManager.textureResolution
                                - tileView, (int) camera.position.x
                                / GameManager.textureResolution + tileView,
                        (int) camera.position.y / GameManager.textureResolution
                                - tileView, (int) camera.position.y
                                / GameManager.textureResolution + tileView);
            }
        }
        ErderGame.batch.end();
        currentStage.draw();
        currentStage.act(Gdx.graphics.getDeltaTime());
       // Table.drawDebug(currentStage);

        if (ErderGame.canMove)
        {
            InputRequest r = new InputRequest();
            if (Gdx.input.isKeyPressed(Keys.W))
            {
                r.x = 0;
                r.y = 1;
                Client.Send(r);
            }
            if (Gdx.input.isKeyPressed(Keys.S))
            {
                r.x = 0;
                r.y = -1;
                Client.Send(r);
            }
            if (Gdx.input.isKeyPressed(Keys.D))
            {
                r.x = 1;
                r.y = 0;
                Client.Send(r);
            }
            if (Gdx.input.isKeyPressed(Keys.A))
            {
                r.x = -1;
                r.y = 0;
                Client.Send(r);
            }
        }
    }

    private void Draw(int zMin, int zMax, int xMin, int xMax, int yMin, int yMax)
    {
        TexturedObject t;
        for (int z = zMin - 1; z <= zMax + 1; ++z)
        {
            for (int x = xMin; x <= xMax; ++x)
            {
                for (int y = yMax; y >= yMin; --y)
                {

                    t = Map.world.tiles.get(new Vector3(x, y, z));

                    if (t != null)
                    {
                        t.Draw(ErderGame.batch, x
                                * GameManager.textureResolution,
                                (y * GameManager.textureResolution)
                                        + (z * GameManager.textureResolution));
                    }
                }
            }
            Map.drawAllByZ(z);

        }
    }

    @Override
    public void resize(int width, int height)
    {
        currentStage.getViewport().update(width, height, true);
    }

    @Override
    public void show()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
        gui.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        currentStage.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        MainScreen.currentStage.keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        MainScreen.currentStage.keyTyped(character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return true;
    }

    private boolean ProcessTouch(int x, int y, int pointer, int button)
    {
        Vector2 toTransform = new Vector2( x - ((float)Vars.getDouble("screenWidth") / 2), y - ((float)Vars.getDouble("screenHeight") / 2)); 
        toTransform = toTransform.nor();
        actIn.angle  = toTransform.angle();
        Client.Send(actIn);
        return true;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        MainScreen.currentStage.touchUp(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        MainScreen.currentStage.touchDragged(screenX, screenY, pointer);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        MainScreen.currentStage.mouseMoved(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        MainScreen.currentStage.scrolled(amount);
        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        if (!MainScreen.currentStage.touchDown((int) x, (int) y, 0, button))
        {
            ProcessTouch((int) x, (int) y, pointer, button);
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
       return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2)
    {
        return true;
    }

    public static void PushMessage(String string, boolean canDisturb)
    {
        ((LoadingScreenGUI) GUICore.getConstructedByName("loading_screen"))
                .ChangeCaption(string, canDisturb);
        ((LoadingScreenGUI) GUICore.getConstructedByName("loading_screen"))
                .Show();
    }

}
