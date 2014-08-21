package com.alastar.game.gui;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.Type;
import com.alastar.game.enums.TypeId;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GUIContainerItem implements GUIElement
{
    public String   name;
    public Window   modal;
    public ItemType type;

    public GUIContainerItem()
    {
        this.name = "GenericContainerItem";
        modal = new Window(this.name,
                GameManager.getSkin(GameManager.selectedSkin), "window");
    }

    public GUIContainerItem(String n, Window w, ItemType type, Vector2 vector2,
            Vector2 vector22, int j, int i, int k, int l)
    {
        this.name = n;
        this.type = type;
        modal = w;
        this.modal.setPosition(vector2.x / (float)Vars.getDouble("balancedScreenWidth"),
                vector2.y / (float)Vars.getDouble("balancedScreenHeight"));
        this.modal.setHeight(vector22.y / (float)Vars.getDouble("balancedScreenHeight"));
        this.modal.setWidth(vector22.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.defaults().padLeft(i);
        modal.defaults().padRight(j);
        modal.defaults().padTop(k);
        modal.defaults().padBottom(l);
        modal.defaults().minWidth(10);
        modal.defaults().minHeight(10);
        modal.add(new Image(GameManager.getTexture(type.name().toLowerCase(), TypeId.getTypeId(Type.Item))));
        modal.setTitle("");
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
        this.modal.setHeight(val / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Float getWidth()
    {
        return modal.getWidth();
    }

    @Override
    public void setWidth(float val)
    {
        this.modal.setWidth(val / (float)Vars.getDouble("balancedScreenWidth"));

    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(modal.getX(), modal.getY());
    }

    @Override
    public void setPosition(Vector2 val)
    {
        modal.setX(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.setY(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadTB()
    {
        return new Vector2(modal.getPadTop(), modal.getPadBottom());
    }

    @Override
    public void setPadTB(Vector2 val)
    {
        modal.defaults().padTop(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.defaults().padBottom(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getPadRL()
    {
        return new Vector2(modal.getPadRight(), modal.getPadLeft());
    }

    @Override
    public void setPadRL(Vector2 val)
    {
        modal.defaults().padRight(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.defaults().padLeft(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMinHW()
    {
        return new Vector2(modal.getMinHeight(), modal.getMinWidth());
    }

    @Override
    public void setMinHW(Vector2 val)
    {
        modal.defaults().minWidth(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.defaults().minHeight(val.y / (float)Vars.getDouble("balancedScreenHeight"));
    }

    @Override
    public Vector2 getMaxHW()
    {
        return new Vector2(modal.getMaxHeight(), modal.getMaxWidth());
    }

    @Override
    public void setMaxHW(Vector2 val)
    {
        modal.defaults().maxWidth(val.x / (float)Vars.getDouble("balancedScreenWidth"));
        modal.defaults().maxHeight(val.y / (float)Vars.getDouble("balancedScreenHeight"));
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
    }

}
