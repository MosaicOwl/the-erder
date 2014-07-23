package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GUILabel implements GUIElement
{
    private String text;
    private String name;
    private Label label;
    private String variable = "";
    
    public GUILabel()
    {
        this.name = "";
        this.label = new Label(this.name, GameManager.getSkin(GameManager.selectedSkin), "label");;
        this.variable = "";
    }
    
    public GUILabel(String n, Label l, String var, int h)
    {
        this.name = n;
        this.label = l;
        this.text = l.getText().toString();
        this.variable = var;
        this.label.setName(n);
        this.label.setHeight(h);
    }
    
    public void Update(String s)
    {
        System.out.println("Updating variable");
        this.label.setText(text+s);
    }
    
    
    public void Update()
    {
        System.out.println("Updating variable");
        if(!getHandledVariable().isEmpty())
        this.label.setText(text+Vars.getVar(getHandledVariable()));
        else
        this.label.setText(text);
    }
    
    @Override
    public Actor getElementAsActor()
    {
        return label;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
        label.remove();
    }
    
    @Override
    public void Hide()
    {
        label.setVisible(false);
    }

    @Override
    public void Show()
    {
        label.setVisible(true);
    }

    @Override
    public String getHandledVariable()
    {
        return variable;
    }

    @Override
    public void setHandledVariable(String val)
    {
        variable = val;
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
    public void setName(String s)
    {
        this.name = s;
        this.label.setName(s);
    }

    @Override
    public Float getHeight()
    {
        return label.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        this.label.setHeight(val / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return label.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.label.setWidth(val / Vars.getInt("balancedScreenWidth"));
        
    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(label.getX(), label.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        label.setX(val.x / Vars.getInt("balancedScreenWidth") );
        label.setY(val.y / Vars.getInt("balancedScreenHeight") );
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
        this.text = text;
        label.setText(text);
        Update();
    }

    @Override
    public String getText()
    {
        return label.getText().toString();
    }

    @Override
    public void addChild(GUIElement o)
    {
        // TODO Auto-generated method stub
        
    }

}
