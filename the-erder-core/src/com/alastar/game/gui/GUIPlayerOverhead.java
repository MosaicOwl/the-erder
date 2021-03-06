package com.alastar.game.gui;

import java.util.Timer;
import java.util.TimerTask;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GUIPlayerOverhead implements GUIElement
{

    public String     name;
    public Window     window;
    public ScrollPane scroll;
    public Table      table;
    public Timer      overheadTimer = null;

    public GUIPlayerOverhead(String n, Vector2 vector2, Vector2 vector22)
    {
        this.name = n;
        this.window = new Window(this.name,
                GameManager.getSkin(GameManager.selectedSkin),
                "overhead_window");
        window.setPosition(vector2.x / (float)Vars.getDouble("balancedScreenWidth"),
                vector2.y / (float)Vars.getDouble("balancedScreenHeight"));
        window.setWidth(vector22.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.setHeight(vector22.y / (float)Vars.getDouble("balancedScreenHeight"));
        window.defaults().prefWidth(
                vector22.x / (float)Vars.getDouble("balancedScreenWidth"));
        window.defaults().prefHeight(
                vector22.y / (float)Vars.getDouble("balancedScreenHeight"));

        table = new Table();
        table.setFillParent(true);
        table.setWidth(vector22.x);
        table.setHeight(vector22.y);
        table.center();
        table.bottom();
        table.setClip(false);

        scroll = new ScrollPane(table);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollingDisabled(true, true);
        scroll.setScrollbarsOnTop(true);
        scroll.setFadeScrollBars(false);
        scroll.setFillParent(true);
        scroll.setSmoothScrolling(false);
        scroll.setWidth(vector22.x);
        scroll.setHeight(vector22.y);
        scroll.setClamp(false);

        window.add(scroll).fill();
        window.setTitle("");
        window.setClip(false);
        InputListener el = new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        };
        window.addListener(el);
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
        window.add(o.getElementAsActor());
        window.pack();
    }

    public void addMsg(String msg)
    {
        Show();
        table.row();
        final Label text = new Label(msg,
                GameManager.getSkin(GameManager.selectedSkin), "label");
        table.add(text);
        scroll.setScrollY(scroll.getMaxY());
        overheadTimer = new Timer();
        overheadTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                text.remove();
                scroll.setScrollY(scroll.getMaxY());
                if(table.getChildren().size == 0)
                {
                    Hide();
                }
                // System.out.println("Overhead removed");
            }
        }, 10000);
    }

}
