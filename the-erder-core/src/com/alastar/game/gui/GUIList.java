package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class GUIList implements GUIElement
{
    private String     name;
    private Window     window;
    private Table      table;
    private ScrollPane scroll;

    public GUIList()
    {
        this.name = "GenericGUIList";
        this.window = new Window(this.name,
                GameManager.getSkin(GameManager.selectedSkin), "window");
        this.window.setName(name);
        this.window.setPosition(0 / (float)Vars.getDouble("balancedScreenWidth"),
                0 / (float)Vars.getDouble("balancedScreenHeight"));
        this.window.setHeight(0 / (float)Vars.getDouble("balancedScreenHeight"));
        this.window.setWidth(0 / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().padLeft(0);
        window.defaults().padRight(0);
        window.defaults().padTop(0);
        window.defaults().padBottom(0);
        window.defaults().minWidth(100 / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().minHeight(30 / (float)Vars.getDouble("balancedScreenHeight"));

        table = new Table();
        table.defaults().padLeft(0);
        table.defaults().padRight(0);
        table.defaults().padTop(0);
        table.defaults().padBottom(0);
        table.setFillParent(true);
        table.setWidth(100);
        table.setHeight(30);
        table.left();
        table.bottom();

        scroll = new ScrollPane(table);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollingDisabled(true, true);
        scroll.setScrollbarsOnTop(true);
        scroll.setFadeScrollBars(false);
        scroll.setFillParent(true);
        scroll.setSmoothScrolling(false);
        scroll.setWidth(90);
        scroll.setHeight(30);

        window.add(scroll).fill();
    }

    public GUIList(String n, Window w, Vector2 vector2, Vector2 vector22,
            int i, int j, int k, int l)
    {
        this.name = n;
        this.window = w;
        this.window.setName(n);
        this.window.setPosition(vector2.x / (float)Vars.getDouble("balancedScreenWidth"),
                vector2.y / (float)Vars.getDouble("balancedScreenHeight"));
        this.window.setHeight(vector22.y / (float)Vars.getDouble("balancedScreenHeight"));
        this.window.setWidth(vector22.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().padLeft(i);
        window.defaults().padRight(j);
        window.defaults().padTop(k);
        window.defaults().padBottom(l);
        window.defaults().minWidth(100);
        window.defaults().minHeight(30);

    }

    @Override
    public Actor getElementAsActor()
    {
        return window;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
      //  window.remove();
        Hide();
        window.clear();
    }

    public void AddControl(GUIElement element)
    {
        window.add(element.getElementAsActor());
        window.pack();
    }

    @Override
    public void Hide()
    {
        window.setVisible(false);
    }

    @Override
    public void Show()
    {
        window.setVisible(true);
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
    }

    @Override
    public void setName(String s)
    {
        this.name = s;
        this.window.setName(s);
    }

    @Override
    public Float getHeight()
    {
        return window.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        this.window.setHeight(val / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return window.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.window.setWidth(val / (float)Vars.getDouble("balancedScreenWidth"));

    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(window.getX(), window.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        window.setX(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.setY(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadTB()
    {
        return new Vector2(window.getPadTop(), window.getPadBottom());
    }

    @Override
    public void setPadTB(Vector2 val)
    {
        window.defaults().padTop(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults()
                .padBottom(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadRL()
    {
        return new Vector2(window.getPadRight(), window.getPadLeft());
    }

    @Override
    public void setPadRL(Vector2 val)
    {
        window.defaults().padRight(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().padLeft(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMinHW()
    {
        return new Vector2(window.getMinHeight(), window.getMinWidth());
    }

    @Override
    public void setMinHW(Vector2 val)
    {
        window.defaults().minWidth(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults()
                .minHeight(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMaxHW()
    {
        return new Vector2(window.getMaxHeight(), window.getMaxWidth());
    }

    @Override
    public void setMaxHW(Vector2 val)
    {
        window.defaults().maxWidth(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults()
                .maxHeight(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public void setText(String text)
    {
        window.setTitle(text);
    }

    @Override
    public String getText()
    {
        return (String) window.getTitle();
    }

    @Override
    public void addChild(GUIElement o)
    {
        table.row();
        ((Label) o.getElementAsActor()).setAlignment(Align.left);
        ((Label) o.getElementAsActor()).setWrap(true);
        table.add(o.getElementAsActor());
    }
}
