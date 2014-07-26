package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class GUITextField implements GUIElement
{

    private String    name;
    private TextField field;

    public GUITextField()
    {
        this.name = "GenericField";
        this.field = new TextField(this.name,
                GameManager.getSkin(GameManager.selectedSkin), "textField");
    }

    public GUITextField(String n, TextField l)
    {
        this.name = n;
        this.field = l;
    }

    @Override
    public Actor getElementAsActor()
    {
        return field;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
       // field.remove();
        Hide();
        field.clear();
    }

    @Override
    public void Hide()
    {
        field.setVisible(false);
    }

    @Override
    public void Show()
    {
        field.setVisible(true);
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
        return null;
    }

    @Override
    public void setEventListener(EventListener val)
    {
        this.field.addListener(val);
    }

    @Override
    public void setName(String s)
    {
        this.name = s;
        this.field.setName(s);
    }

    @Override
    public Float getHeight()
    {
        return field.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        this.field.setHeight(val / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return field.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.field.setWidth(val / Vars.getInt("balancedScreenWidth"));

    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(field.getX(), field.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        field.setX(val.x / Vars.getInt("balancedScreenWidth"));
        field.setY(val.y / Vars.getInt("balancedScreenHeight"));
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
        field.setText(text);
    }

    @Override
    public String getText()
    {
        return (String) field.getText();
    }

    @Override
    public void addChild(GUIElement o)
    {
        // TODO Auto-generated method stub

    }

    public void clear()
    {
        this.field.setText("");
        this.field.getStage().setKeyboardFocus(null);
    }

}
