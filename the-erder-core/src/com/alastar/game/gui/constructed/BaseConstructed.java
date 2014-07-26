package com.alastar.game.gui.constructed;

import java.util.ArrayList;

import com.alastar.game.gui.GUIElement;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BaseConstructed implements ConstructedGUI
{
    protected String                name;
    protected ArrayList<GUIElement> actors = new ArrayList<GUIElement>();
    protected Stage                 s;

    public BaseConstructed(Stage s, String name)
    {
        this.name = name;
        this.s = s;
    }

    public void register()
    {
        for (GUIElement a : actors)
        {
            s.addActor(a.getElementAsActor());
        }
    }

    public void register(Actor a)
    {
        s.addActor(a);
    }

    @Override
    public void Destroy()
    {

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
        for (GUIElement el : actors)
        {
            if (el.getHandledVariable() != null)
            {
                if (el.getHandledVariable().equals(s))
                    el.Update(val);
            }
        }
    }

    @Override
    public void Hide()
    {

    }

    @Override
    public void Show()
    {
    }
}
