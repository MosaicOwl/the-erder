package ru.alastar.main.net.responses;

import java.util.Hashtable;

import com.alastar.game.enums.ItemType;

public class AddItemResponse
{
    public float                      x, y, z;
    public int                        id, amount;
    public String                     caption;
    public Hashtable<String, Integer> attrs;
    public ItemType                   type;
}
