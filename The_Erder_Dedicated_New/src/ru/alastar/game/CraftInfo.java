package ru.alastar.game;

import java.util.ArrayList;

import com.alastar.game.enums.ItemType;

import ru.alastar.enums.EquipType;

public class CraftInfo
{

    public ArrayList<String> neededItems;
    public String            skill;
    public int               skillVal;
    public String            caption;
    public EquipType         eqType;
    public Attributes        attributes;
    public ItemType          type;

    public CraftInfo(ArrayList<String> n, String sk, int skV, String c,
            EquipType eT, ItemType aT, Attributes a)
    {
        this.neededItems = n;
        this.skill = sk;
        this.skillVal = skV;
        this.caption = c;
        this.eqType = eT;
        this.type = aT;
        this.attributes = a;
    }

}
