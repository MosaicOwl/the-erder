package com.alastar.game;

import java.util.Hashtable;

import com.alastar.game.enums.EntityType;
import com.alastar.game.enums.ItemType;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("serial")
public class Item extends Entity {

    public int amount;
    public Hashtable<String, Integer> attributes;
    public ItemType itemType;
    
	public Item(int id, Vector3 pos, String caption, ItemType itemType, int amt, Hashtable<String, Integer> attr) {
		super(id, pos, caption, EntityType.Human, false);
		this.amount = amt;
		this.attributes = attr;
		this.itemType = itemType;
	}

    public int getEquipX()
    {
        switch(itemType)
        {
            case Axe:
                break;
            case Bag:
                break;
            case Chest:
                break;
            case Gem:
                break;
            case Gloves:
                break;
            case Gold:
                break;
            case Gorget:
                break;
            case Helm:
                break;
            case Leggings:
                break;
            case None:
                break;
            case Ore:
                break;
            case Pickaxe:
                break;
            case Plant:
                break;
            case Reagent:
                break;
            case Totem:
                break;
            case Tunic:
                break;
            case Wood:
                break;
            default:
                break;
            
        }
        return 0;
    }

    public int getEquipY()
    {        switch(itemType)
        {
            case Axe:
                break;
            case Bag:
                break;
            case Chest:
                break;
            case Gem:
                break;
            case Gloves:
                return 5;
            case Gold:
                break;
            case Gorget:
                return 6;
            case Helm:
                return 7;
            case Leggings:
                return 0;
            case None:
                break;
            case Ore:
                break;
            case Pickaxe:
                break;
            case Plant:
                break;
            case Reagent:
                break;
            case Totem:
                break;
            case Tunic:
                break;
            case Wood:
                break;
            default:
                break;
            
        }
        return 0;
    }

}
