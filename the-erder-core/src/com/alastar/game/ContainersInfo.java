package com.alastar.game;

import java.util.Hashtable;

import com.alastar.game.gui.GUIContainer;
import com.alastar.game.gui.GUIContainerItem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ContainersInfo
{
    public static Hashtable<String, ContainerInfo> containers = new Hashtable<String, ContainerInfo>();

    public static void addContainer(String s, ContainerInfo inf)
    {
        containers.put(s, inf);
    }

    public static ContainerInfo getContainer(String s)
    {
        return containers.get(s);
    }

    public static void removeContainer(String s)
    {
        containers.remove(s);
    }

    public static void addToContainer(String s, Item i)
    {
        containers.get(s).addItem(i);
    }

    public static void fillContainer(GUIContainer guiContainer,
            String containerArrayName)
    {
        ContainerInfo container = getContainer(containerArrayName);
        GUIContainerItem i;
        for (Item item : container.items.values())
        {
            i = new GUIContainerItem(item.caption + "_slot", new Window(
                    item.caption + "_slot",
                    GameManager.getSkin(GameManager.selectedSkin), "window"),
                    item.itemType, new Vector2(), new Vector2(), 0, 0, 0, 0);
            guiContainer.addChild(i);
        }
    }

    public static void removeFromContainer(String name, int id)
    {
        getContainer(name).removeItem(id);
    }
}
