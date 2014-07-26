package ru.alastar.main.net.responses;

import java.util.Hashtable;

import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.UpdateItemType;

public class UpdateItemResponse
{
    public int                        id, amount;
    public float                      x, y, z;
    public String                     caption;
    public Hashtable<String, Integer> attrs;
    public ItemType                   type;
    public UpdateItemType             updType;
}
