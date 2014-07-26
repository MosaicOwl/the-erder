package com.alastar.game.gui;

import java.util.Hashtable;

import com.alastar.game.gui.constructed.ConstructedGUI;

public class GUICore
{
    public static Hashtable<String, ConstructedGUI> uis         = new Hashtable<String, ConstructedGUI>();
    public static Hashtable<String, GUIElement>     guiElements = new Hashtable<String, GUIElement>();

    public static void add(GUIElement ui)
    {
        guiElements.put(ui.getName(), ui);
    }

    public static void remove(String s)
    {
        guiElements.get(s).getElementAsActor().remove();
        guiElements.remove(s);
    }

    public static void addConstructedGUI(ConstructedGUI ui)
    {
        uis.put(ui.getName(), ui);
    }

    public static void removeConstructedGUI(String s)
    {
        uis.get(s).Destroy();
        uis.remove(s);
    }

    public static void notifyVariableHandlers(String s, String val)
    {
        for (ConstructedGUI cgui : uis.values())
        {
            cgui.notifyAllElements(s, val);
        }
    }

    public static ConstructedGUI getConstructedByName(String string)
    {
        for (ConstructedGUI cgui : uis.values())
        {
            if (cgui.getName() == string)
                return cgui;
        }
        return null;
    }

    public static GUIElement get(String name)
    {
        for (GUIElement gui : guiElements.values())
        {
            if (gui.getName() == name)
                return gui;
        }
        return null;
    }

    public static void enableOne(String string)
    {
        for (ConstructedGUI cgui : uis.values())
        {
            cgui.Hide();
        }
        getConstructedByName(string).Show();
        ;
    }
}
