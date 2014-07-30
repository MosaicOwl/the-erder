package com.alastar.game.gui.constructed;

import java.util.Timer;
import java.util.TimerTask;

import com.alastar.game.GameManager;
import com.alastar.game.gui.GUIJoystick;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class JoysticksGUI extends BaseConstructed
{

    public GUIJoystick moveJoystick;
    public GUIJoystick attackJoystick;
    
    public JoysticksGUI(Stage s, String name)
    {
        super(s, name);
        InputListener dl = new InputListener() {
            Timer timer;

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch down at (" + x + ", " + y + ")");
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask(){

                    @Override
                    public void run()
                    {
                        
                        moveJoystick.setPressPosition(Gdx.input.getX(), Gdx.input.getY());
                    }}, 0, 100);
                return true;
            }
         
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(timer != null)
                    timer.cancel();
                
                timer = null;
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
         };

        moveJoystick = new GUIJoystick(100,100, GameManager.getGUITexture("lwindow"), GameManager.getGUITexture("lbutton"), 250, 250, dl);

        InputListener adl =  new InputListener() {


            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
         
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
         };
        attackJoystick = new GUIJoystick(1200, 100, GameManager.getGUITexture("lwindow"), GameManager.getGUITexture("lbutton"), 250, 250, adl); 
        
        actors.add(attackJoystick);
        actors.add(moveJoystick);
        this.register();

    }

}
