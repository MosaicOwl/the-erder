package com.alastar.game.gui;

import com.alastar.game.ContainersInfo;
import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.alastar.game.enums.ContainerType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GUIContainer implements GUIElement
{
    public String        name;
    public Window        modal;
    public ContainerType type;
    private Table        table;
    private ScrollPane   scroll;
    public String        containerArrayName;

    public GUIContainer()
    {
        this.name = "GenericContainer";
        type = ContainerType.Bag;
        modal = new Window(this.name,
                GameManager.getSkin(GameManager.selectedSkin), "window");
    }

    public GUIContainer(String n, Window w, ContainerType type,
            Vector2 vector2, Vector2 vector22, int j, int i, int k, int l)
    {
        this.name = n;
        this.type = type;
        modal = w;
        this.modal.setPosition(vector2.x / Vars.getInt("balancedScreenWidth"),
                vector2.y / Vars.getInt("balancedScreenHeight"));
        this.modal.setHeight(vector22.y / Vars.getInt("balancedScreenHeight"));
        this.modal.setWidth(vector22.x / Vars.getInt("balancedScreenWidth"));
        modal.defaults().padLeft(i);
        modal.defaults().padRight(j);
        modal.defaults().padTop(k);
        modal.defaults().padBottom(l);
        modal.defaults().minWidth(100);
        modal.defaults().minHeight(30);
        modal.setMovable(true);

        table = new Table();
        table.defaults().padLeft(i);
        table.defaults().padRight(j);
        table.defaults().padTop(k);
        table.defaults().padBottom(l);
        table.setFillParent(true);
        table.setWidth(vector22.x - i - j);
        table.setHeight(vector22.y - k - l);
        table.left();
        table.bottom();

        scroll = new ScrollPane(table);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollingDisabled(true, false);
        scroll.setScrollbarsOnTop(true);
        scroll.setFadeScrollBars(false);
        scroll.setFillParent(true);
        scroll.setSmoothScrolling(false);
        scroll.setWidth(vector22.x - 10);
        scroll.setHeight(vector22.y - 10);

        modal.add(table).fill();
    }

    @Override
    public Actor getElementAsActor()
    {
        return modal;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void Destroy()
    {
        //modal.remove(); 
        Hide();
        modal.clear();
    }

    public void AddControl(GUIElement element)
    {
        modal.add(element.getElementAsActor());
        modal.pack();
    }

    @Override
    public void Hide()
    {
        modal.setVisible(false);
    }

    @Override
    public void Show()
    {
        modal.setVisible(true);
    }

    @Override
    public void Update(String s)
    {

    }

    @Override
    public String getHandledVariable()
    {
        return containerArrayName;
    }

    @Override
    public void setHandledVariable(String val)
    {
        this.containerArrayName = val;
        ContainersInfo.fillContainer(this, containerArrayName);
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
        this.modal.setName(s);
    }

    @Override
    public Float getHeight()
    {
        return modal.getHeight();
    }

    @Override
    public void setHeight(float val)
    {
        this.modal.setHeight(val / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return modal.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.modal.setWidth(val / Vars.getInt("balancedScreenWidth"));

    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(modal.getX(), modal.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        modal.setX(val.x / Vars.getInt("balancedScreenWidth"));
        modal.setY(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadTB()
    {
        return new Vector2(modal.getPadTop(), modal.getPadBottom());
    }

    @Override
    public void setPadTB(Vector2 val)
    {
        modal.defaults().padTop(val.x / Vars.getInt("balancedScreenWidth"));
        modal.defaults().padBottom(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadRL()
    {
        return new Vector2(modal.getPadRight(), modal.getPadLeft());
    }

    @Override
    public void setPadRL(Vector2 val)
    {
        modal.defaults().padRight(val.x / Vars.getInt("balancedScreenWidth"));
        modal.defaults().padLeft(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMinHW()
    {
        return new Vector2(modal.getMinHeight(), modal.getMinWidth());
    }

    @Override
    public void setMinHW(Vector2 val)
    {
        modal.defaults().minWidth(val.x / Vars.getInt("balancedScreenWidth"));
        modal.defaults().minHeight(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMaxHW()
    {
        return new Vector2(modal.getMaxHeight(), modal.getMaxWidth());
    }

    @Override
    public void setMaxHW(Vector2 val)
    {
        modal.defaults().maxWidth(val.x / Vars.getInt("balancedScreenWidth"));
        modal.defaults().maxHeight(val.y / Vars.getInt("balancedScreenHeight"));
    }

    @Override
    public void setText(String text)
    {
        modal.setTitle(text);
    }

    @Override
    public String getText()
    {
        return (String) modal.getTitle();
    }

    @Override
    public void addChild(GUIElement o)
    {
        modal.add(o.getElementAsActor());
    }

}
