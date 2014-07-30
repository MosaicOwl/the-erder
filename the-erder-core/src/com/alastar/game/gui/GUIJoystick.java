package com.alastar.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GUIJoystick implements GUIElement
{

    public Table table;
    public Image background;
    public Image pressElement;
    public Texture texture;
    public Texture pressTexture;
    public Vector2 center;
    public EventListener tapListener;
    
    public GUIJoystick(float x, float y, Texture t, Texture pT, float w, float h, EventListener dl)
    {
        table = new Table();
        table.setFillParent(false);
        table.setPosition(x, y);
        table.setWidth(w);
        table.setHeight(h);
        this.texture = t;
        this.pressTexture = pT;
        background = new Image(texture);
        pressElement = new Image(pressTexture);
        pressElement.setVisible(false);
        background.setFillParent(true);
        table.add(background);
        table.add(pressElement);
        center = new Vector2(table.getCenterX(), table.getCenterY());
        tapListener = dl;
        table.addListener(dl);
    }
    
    @Override
    public Actor getElementAsActor()
    {
        return table;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void setName(String s)
    {
        
    }

    @Override
    public void Destroy()
    {
        
    }

    @Override
    public void Hide()
    {
        table.setVisible(false);
    }

    @Override
    public void Show()
    {
        table.setVisible(true);
    }

    @Override
    public void Update(String val)
    {
        
    }

    @Override
    public String getHandledVariable()
    {
        return null;
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
        return null;
    }

    @Override
    public void setHeight(float val)
    {
        
    }

    @Override
    public Float getWidth()
    {
        return null;
    }

    @Override
    public void setWidth(float val)
    {
        
    }

    @Override
    public Vector2 getPosition()
    {
        return null;
    }

    @Override
    public void setPosition(Vector2 val)
    {
        
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
        return null;
    }

    @Override
    public void setMinHW(Vector2 val)
    {        
    }

    @Override
    public Vector2 getMaxHW()
    {
        return null;
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
        return null;
    }

    @Override
    public void addChild(GUIElement o)
    {
        
    }

    public void setPressPosition(float f, float g)
    {

        
    }

}
