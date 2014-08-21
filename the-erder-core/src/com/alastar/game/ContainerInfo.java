package com.alastar.game;

import java.util.Hashtable;

public class ContainerInfo
{

    public Hashtable<Integer, Item> items;
    public String                   name;
    public int                      maximum;

    public ContainerInfo(String name, int max)
    {
        this.name = name;
        this.maximum = max;
        this.items = new Hashtable<Integer, Item>();
    }

    public void addItem(Item i)
    {
        if (items.containsKey(i.id))
            items.remove(i.id);

        this.items.put(i.id, i);
    }

    public Item getItem(int i)
    {
        for (Item item : items.values())
        {
            if (item.id == i)
            {
                return item;
            }
        }
        return null;
    }

    public void removeItem(int i)
    {
        this.items.remove(getItem(i));
    }

}
