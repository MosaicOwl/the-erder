package ru.alastar.main;

import java.util.HashMap;

import ru.alastar.main.net.Server;

public class Configuration
{
    private static HashMap<String, String> entries = new HashMap<String, String>();

    public static void AddEntry(String s1, String s2)
    {
        entries.put(s1.toLowerCase(), s2);
    }

    public static String GetEntryValue(String s1)
    {
        try
        {
            return entries.get(s1.toLowerCase());
        } catch (Exception e)
        {
            Server.handleError(e);
        }
        return null;
    }

}
