package com.alastar.game.gui.constructed;

import com.alastar.game.GameManager;
import com.alastar.game.gui.GUIButton;
import com.alastar.game.gui.GUILabel;
import com.alastar.game.gui.GUIWindow;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LoadingScreenGUI extends BaseConstructed
{
    private GUIWindow window;
    private GUILabel label;
    private Table table;
    
    private boolean canDisturb = false;
    
    public LoadingScreenGUI(Stage s)
    {
        super(s, "loading_screen");
       
        ChangeListener el = new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(canDisturb)
                {
                    Hide();
                }
            }
            
        };
        table = new Table();
        table.setFillParent(true);
        
        Window w = new Window("Loading", GameManager.getSkin(GameManager.selectedSkin), "window");
        label = new GUILabel("", new Label("", GameManager.getSkin(GameManager.selectedSkin), "label"), "", 25);
        GUIButton disturb = new GUIButton("X", new TextButton("X", GameManager.getSkin(GameManager.selectedSkin), "button"), el);

        window = new GUIWindow("Loading", w, new Vector2(400,400), new Vector2(700, 700), 15, 0, 0, 0);
        window.AddControl(label);
        window.AddControl(disturb);

        table.add(window.getElementAsActor());
        
        register(window.getElementAsActor());
    }

    public void ChangeCaption(String string, boolean canDisturb2)
    {
       canDisturb = canDisturb2;
       label.setText(string);
    } 
    
    @Override
    public void Hide()
    {
        window.Hide();
    }

    @Override
    public void Show()
    {    
        window.Show(); 
    }
}
