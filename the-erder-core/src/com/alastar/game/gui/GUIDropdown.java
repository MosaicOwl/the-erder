package com.alastar.game.gui;

import ru.alastar.net.Client;

import com.alastar.game.GameManager;
import com.alastar.game.MainScreen;
import com.alastar.game.gui.net.NetGUIAnswer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class GUIDropdown implements GUIElement
{
    private String            name;
    private SelectBox<String> box;

    public GUIDropdown()
    {
        this.name = "GenericBox";
        this.box = new SelectBox<String>(
                GameManager.getSkin(GameManager.selectedSkin), "box");
        this.box.setItems("Close");
        this.box.setSelectedIndex(0);

        ChangeListener listener = new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                NetGUIAnswer r = new NetGUIAnswer();
                r.name = name;
                r.value = box.getSelected();
                System.out.println(r.value);
                Client.Send(r);
            }
        };

        this.box.addListener(listener);
    }

    public GUIDropdown(final String n, SelectBox<String> l, EventListener e)
    {
        this.name = n;
        this.box = l;
        this.box.addListener(e);
        this.box.setItems("Close");
        this.box.setSelectedIndex(0);
    }

    @Override
    public Actor getElementAsActor()
    {
        return box;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String s)
    {
        this.name = s;
    }

    @Override
    public void Destroy()
    {
       // box.remove();
        Hide();
        box.clear();
    }

    @Override
    public void Hide()
    {
        box.setVisible(false);
    }

    @Override
    public void Show()
    {
        box.setVisible(true);
    }

    @Override
    public void Update(String val)
    {

    }

    @Override
    public String getHandledVariable()
    {
        return "";
    }

    @Override
    public void setHandledVariable(String val)
    {

    }

    @Override
    public EventListener getEventListener()
    {
        return null;
    }

    @Override
    public void setEventListener(EventListener val)
    {

    }

    @Override
    public Float getHeight()
    {
        return box.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        box.setHeight(val);
    }

    @Override
    public Float getWidth()
    {
        return box.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        box.setWidth(val);
    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(box.getX(), box.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        Vector3 vec = MainScreen.camera.project(new Vector3(
                (float) (val.x + 0.5) * GameManager.textureResolution,
                (float) (val.y - 0.5) * GameManager.textureResolution, 0));
        box.setX(vec.x);
        box.setY(vec.y);
    }

    @Override
    public Vector2 getPadTB()
    {
        return null;
    }

    @Override
    public void setPadTB(Vector2 val)
    {

    }

    @Override
    public Vector2 getPadRL()
    {
        return null;

    }

    @Override
    public void setPadRL(Vector2 val)
    {
    }

    @Override
    public Vector2 getMinHW()
    {
        return new Vector2(box.getMinHeight(), box.getMinWidth());
    }

    @Override
    public void setMinHW(Vector2 val)
    {
    }

    @Override
    public Vector2 getMaxHW()
    {
        return new Vector2(box.getMaxHeight(), box.getMaxWidth());
    }

    @Override
    public void setMaxHW(Vector2 val)
    {
    }

    @Override
    public void setText(String text)
    {
    }

    @Override
    public String getText()
    {
        return "";
    }

    @Override
    public void addChild(GUIElement o)
    {
        System.out.println("Adding " + o.getText() + " to selectBox");
        Array<String> a = new Array<String>();
        for (String old : box.getItems())
        {
            a.add(old);
        }
        a.add(o.getText());
        box.setItems(a);
        box.pack();
    }

}
