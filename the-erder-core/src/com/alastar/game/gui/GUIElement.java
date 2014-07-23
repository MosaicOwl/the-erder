package com.alastar.game.gui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public interface GUIElement
{
   public Actor getElementAsActor();
   
   public String getName();
   public void setName(String s);

   public void Destroy();
   public void Hide();
   public void Show();
   public void Update(String val);
   
   public String getHandledVariable();
   public void setHandledVariable(String val);
   
   public EventListener getEventListener();
   public void setEventListener(EventListener val);
   
   public Float getHeight();
   public void setHeight(float val);
   
   public Float getWidth();
   public void setWidth(float val);
   
   public Vector2 getPosition();
   public void setPosition(Vector2 val);
   
   public Vector2 getPadTB();
   public void setPadTB(Vector2 val);
   
   public Vector2 getPadRL();
   public void setPadRL(Vector2 val);
   
   public Vector2 getMinHW();
   public void setMinHW(Vector2 val);
   
   public Vector2 getMaxHW();
   public void setMaxHW(Vector2 val);

   public void setText(String text);
   public String getText();

   public void addChild(GUIElement o);

}
