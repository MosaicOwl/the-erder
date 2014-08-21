package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GUIServerList implements GUIElement
{
    public Window     window;
    public Table      table;
    public ScrollPane scroll;
    public Table      mainTable;

    public GUIServerList()
    {
        this.window = new Window("servers_list",
                GameManager.getSkin(GameManager.selectedSkin), "window");
        this.window.setHeight(500 / (float)Vars.getDouble("balancedScreenHeight"));
        this.window.setWidth(500 / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().padLeft(10);
        window.defaults().padRight(10);
        window.defaults().padTop(10);
        window.defaults().padBottom(10);
        window.defaults().prefWidth(500 / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults()
                .prefHeight(500 / (float)Vars.getDouble("balancedScreenHeight"));

        table = new Table();
        table.defaults().padLeft(10);
        table.defaults().padRight(10);
        table.defaults().padTop(10);
        table.defaults().padBottom(10);
        table.setFillParent(true);
        table.setWidth(480);
        table.setHeight(480);
        table.left();
        table.bottom();

        scroll = new ScrollPane(table);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollingDisabled(true, true);
        scroll.setScrollbarsOnTop(true);
        scroll.setFadeScrollBars(false);
        scroll.setFillParent(true);
        scroll.setSmoothScrolling(false);
        scroll.setWidth(480);
        scroll.setHeight(480);

        window.add(scroll).fill();
        window.setTitle("Pick a server");
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(window);
    }

    @Override
    public Actor getElementAsActor()
    {
        return mainTable;
    }

    @Override
    public String getName()
    {
        return "servers_list";
    }

    @Override
    public void setName(String s)
    {

    }

    @Override
    public void Destroy()
    {
        mainTable.remove();
    }

    @Override
    public void Hide()
    {
        mainTable.setVisible(false);
    }

    @Override
    public void Show()
    {
        mainTable.setVisible(true);
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
        table.row();
        table.add(o.getElementAsActor());
    }

}
