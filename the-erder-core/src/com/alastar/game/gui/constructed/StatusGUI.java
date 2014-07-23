package com.alastar.game.gui.constructed;

import java.util.ArrayList;

import com.alastar.game.GameManager;
import com.alastar.game.MainScreen;
import com.alastar.game.gui.GUIElement;
import com.alastar.game.gui.GUILabel;
import com.alastar.game.gui.GUIWindow;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class StatusGUI implements ConstructedGUI
{
    private GUIWindow window;
    private String name;
    private ArrayList<GUIElement> actors = new ArrayList<GUIElement>();
    public StatusGUI(String name)
    {
        this.name = name;
       
        Window w = new Window("Status", GameManager.getSkin(GameManager.selectedSkin), "window");
        GUILabel hp = new GUILabel("HP:", new Label("HP:", GameManager.getSkin(GameManager.selectedSkin), "label"), "hits_value", 25);
        GUILabel mana = new GUILabel("MANA:", new Label("MANA:", GameManager.getSkin(GameManager.selectedSkin), "label"), "mana_value", 25);
        GUILabel inte = new GUILabel("INT:", new Label("INT::", GameManager.getSkin(GameManager.selectedSkin), "label"), "int_value", 25);
        GUILabel str = new GUILabel("STR:", new Label("STR:", GameManager.getSkin(GameManager.selectedSkin), "label"), "stringth_value", 25);
        GUILabel dex = new GUILabel("DEX:", new Label("DEX:", GameManager.getSkin(GameManager.selectedSkin), "label"), "dexterity_value", 25);

        window = new GUIWindow("Status", w, new Vector2(10,10), new Vector2(300, 100), 15, 0, 0, 0);
        window.AddControl(hp);
        window.AddControl(mana);
        window.AddControl(inte);
        window.AddControl(str);
        window.AddControl(dex);

        actors.add(window);
        actors.add(hp);
        actors.add(mana);
        actors.add(inte);
        actors.add(str);
        actors.add(dex);
        
        MainScreen.currentStage.addActor(window.getElementAsActor());
        window.Show();
    }
    
    @Override
    public void Destroy()
    {
        window.Destroy();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public ArrayList<GUIElement> getElements()
    {
        return actors;
    }

    @Override
    public void notifyAllElements(String s, String val)
    {
        for(GUIElement el: actors)
        {
            if(el.getHandledVariable().equals(s))
                el.Update(val);
        }
    }
}
