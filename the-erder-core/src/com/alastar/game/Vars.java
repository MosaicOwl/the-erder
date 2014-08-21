package com.alastar.game;

import java.util.Hashtable;

import com.alastar.game.gui.GUICore;

public class Vars
{

    public static Hashtable<String, Double> doubleVars = new Hashtable<String, Double>();
    public static Hashtable<String, String>  stringVars  = new Hashtable<String, String>();

    public static void AddVar(String s, Double i)
    {
        System.out.println("Added var " + s + ": " + i);
        doubleVars.put(s, i);
        GUICore.notifyVariableHandlers(s, Double.toString(i));

    }

    public static void AddVar(String s, String i)
    {
        System.out.println("Added var " + s + ": " + i);
        stringVars.put(s, i);
        GUICore.notifyVariableHandlers(s, i);

    }

    public static void setVar(String s, Double i)
    {
        System.out.println("Set var " + s + ": " + i);

        if (doubleVars.containsKey(s))
            doubleVars.remove(s);

        doubleVars.put(s, i);
        GUICore.notifyVariableHandlers(s, Double.toString(i));
    }

    public static void setVar(String s, String i)
    {
        System.out.println("Set var " + s + ": " + i);

        if (stringVars.containsKey(s))
            stringVars.remove(s);

        stringVars.put(s, i);
        GUICore.notifyVariableHandlers(s, i);
    }

    public static double getDouble(String s)
    {
        return doubleVars.get(s);
    }

    public static String getStr(String s)
    {
        return stringVars.get(s);
    }

    public static String getVar(String s)
    {
        if (doubleVars.containsKey(s))
            return Double.toString(doubleVars.get(s));
        else if (stringVars.containsKey(s))
            return stringVars.get(s);
        else
            return null;
    }

}
