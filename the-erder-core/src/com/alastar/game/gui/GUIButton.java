package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GUIButton implements GUIElement
{
    private String     name;
    private TextButton button;

    public GUIButton()
    {
        this.name = "GenericButton";
        this.button = new TextButton(name,
                GameManager.getSkin(GameManager.selectedSkin), "button");
    }

    public GUIButton(String n, TextButton l, EventListener e)
    {
        this.name = n;
        this.button = l;
        this.button.addListener(e);
    }

    @Override
    public Actor getElementAsActor()
    {
        return button;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
        button.remove();
    }

    @Override
    public void Hide()
    {
        button.setVisible(false);
    }

    @Override
    public void Show()
    {
        button.setVisible(true);
    }

    @Override
    public void Update(String s)
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
        return this.button.getClickListener();
    }

    @Override
    public void setEventListener(EventListener val)
    {
        this.button.addListener(val);
    }

    @Override
    public void setName(String s)
    {
        this.name = s;
        this.button.setName(s);
    }

    @Override
    public Float getHeight()
    {
        return button.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        this.button.setHeight(val / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return button.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.button.setWidth(val / Vars.getInt("balancedScreenWidth"));

    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(button.getX(), button.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        button.setX(val.x / Vars.getInt("balancedScreenWidth"));
        button.setY(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadTB()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPadTB(Vector2 val)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector2 getPadRL()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPadRL(Vector2 val)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector2 getMinHW()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMinHW(Vector2 val)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector2 getMaxHW()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMaxHW(Vector2 val)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setText(String text)
    {
        button.setText(text);
    }

    @Override
    public String getText()
    {
        return (String) button.getText();
    }

    @Override
    public void addChild(GUIElement o)
    {
        // TODO Auto-generated method stub

    }
}
