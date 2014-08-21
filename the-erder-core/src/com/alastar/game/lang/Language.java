package com.alastar.game.lang;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Language
{

    public String       langName = "Generic Language";
    public EntryManager eManager;
    public BitmapFont   font     = null;

    public Language(String n, EntryManager m, BitmapFont f)
    {
        langName = n;
        eManager = m;
        font = f;
    }

    public Entry getLangStringById(int strId)
    {
        return eManager.getStrById(strId);
    }

    public BitmapFont getFont()
    {
        return font;
    }

    public Entry getLangByString(String strName)
    {
        return eManager.getStrByIdStrName(strName);
    }

}
