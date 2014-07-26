package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GUIEquipSlot implements GUIElement
{
    private String name;
    private Label  label;
    private Table  table;
    private Image  image;

    public GUIEquipSlot()
    {
        this.name = "GenericSlot";
        this.label = new Label(this.name,
                GameManager.getSkin(GameManager.selectedSkin), "label");
        this.image = new Image(GameManager.getItemTexture(ItemType.Plant));
        this.table = new Table();
        table.add(image);
        table.add(label);
    }

    public void Update(String s)
    {
    }

    public void Update()
    {
    }

    @Override
    public Actor getElementAsActor()
    {
        return table;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
        table.remove();
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
    public String getHandledVariable()
    {
        return "";
    }

    @Override
    public void setHandledVariable(String val)
    {
        ItemType type = ItemType.valueOf(val);
        image = new Image(GameManager.getItemTexture(type));
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
        label.setX(val.x / Vars.getInt("balancedScreenWidth"));
        label.setY(val.y / Vars.getInt("balancedScreenHeight"));
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

    }

}
