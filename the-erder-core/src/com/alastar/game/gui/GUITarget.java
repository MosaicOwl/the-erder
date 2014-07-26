package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class GUITarget implements GUIElement
{

    public Table table;
    public Image target;
    public Image bar;
    public float w;

    public GUITarget(float w, float h)
    {
        table = new Table();
        table.setWidth(w);
        table.setHeight(h);

        this.w = w;

        target = new Image(GameManager.getSkin(GameManager.selectedSkin).get(
                "lwindow", Texture.class));
        target.setFillParent(true);

        bar = new Image(GameManager.getSkin(GameManager.selectedSkin).get(
                "lbutton", Texture.class));
        bar.setFillParent(true);
        bar.setAlign(Align.left);
        bar.setWidth(w);

        table.align(Align.left);

        table.add(target);
        table.row();
        table.add(bar);
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
        this.table.setX(val.x);
        this.table.setY(val.y);
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

    public void setPrecentage(int i)
    {
        bar.setWidth(w / 100 * i);
    }

}
