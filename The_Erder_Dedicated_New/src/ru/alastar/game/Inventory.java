package ru.alastar.game;

import java.util.ArrayList;

import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.responses.AddToContainerResponse;
import ru.alastar.main.net.responses.RemoveFromContainerResponse;

public class Inventory implements IContainer
{

    public int             entityId, maxItems;
    public ArrayList<Item> items;

    public Inventory(int i, int m)
    {
        this.entityId = i;
        this.maxItems = m;
        this.items = new ArrayList<Item>();
    }

    public Inventory(int i, int m, ArrayList<Item> its)
    {
        this.entityId = i;
        this.maxItems = m;
        this.items = its;
    }

    @Override
    public void AddItem(Item i)
    {
        ConnectedClient c;
        if (!items.contains(i))
        {
            Item st = getSameTypeItem(i.caption);
            if (st == null)
            {
                if (items.size() + 1 <= maxItems)
                {
                    items.add(i);
                    AddToContainerResponse r = new AddToContainerResponse();
                    r.name = "inv";
                    r.amount = i.amount;
                    r.captiion = i.caption;
                    r.id = i.id;
                    if (Server.getEntity(entityId) != null)
                    {
                        c = Server.getClient(Server.getEntity(entityId));
                        if (c != null)
                            Server.SendTo(c.connection, r);
                    }
                } else
                {
                    Server.warnEntity(Server.getEntity(entityId),
                            "Your backpack is full!");
                }
            } else
            {
                // Main.Log("[INVENTORY]",
                // "Items stack exists! Adding items to it. Stack size: " +
                // st.amount + ". Adding " + i.amount);
                st.amount += i.amount;
                // Main.Log("[INVENTORY]", "Added! Now its " + st.amount);
                AddToContainerResponse r = new AddToContainerResponse();
                r.name = "inv";
                r.amount = st.amount;
                r.captiion = st.caption;
                r.id = st.id;
                if (Server.getEntity(entityId) != null)
                {
                    c = Server.getClient(Server.getEntity(entityId));
                    if (c != null)
                        Server.SendTo(c.connection, r);
                }
                Server.DestroyItem(i);
            }

        }
    }

    @Override
    public void RemoveItem(Item i)
    {
        if (items.contains(i))
            items.remove(i);

        RemoveFromContainerResponse r = new RemoveFromContainerResponse();
        r.name = "inv";
        r.id = i.id;
        Server.SendTo(Server.getClient(Server.getEntity(entityId)).connection,
                r);
    }

    @Override
    public void RemoveItem(int i)
    {
        for (Item it : items)
        {
            if (it.id == i)
            {
                items.remove(it);

                RemoveFromContainerResponse r = new RemoveFromContainerResponse();
                r.name = "inv";
                r.id = it.id;
                Server.SendTo(
                        Server.getClient(Server.getEntity(entityId)).connection,
                        r);
                break;
            }
        }
    }

    @Override
    public Item getItem(int i)
    {
        for (Item it : items)
        {
            if (it.id == i)
            {
                return it;
            }
        }
        return null;
    }

    public Item getSameTypeItem(String s)
    {
        for (Item it : items)
        {
            if (it.caption.equals(s))
            {
                return it;
            }
        }
        return null;
    }

    @Override
    public boolean haveItem(int i)
    {
        for (Item it : items)
        {
            if (it.id == i)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Item> getItems()
    {
        return items;
    }

    public Item getItem(String s)
    {
        for (Item it : items)
        {
            if (it.caption.equals(s))
            {
                return it;
            }
        }
        return null;
    }

    public void consume(Item item)
    {
        if (item.amount <= 1)
        {
            Server.DestroyItem(this, item);

            RemoveFromContainerResponse r = new RemoveFromContainerResponse();
            r.name = "inv";
            r.id = item.id;
            Server.SendTo(
                    Server.getClient(Server.getEntity(entityId)).connection, r);
        } else
        {
            --item.amount;
            AddToContainerResponse r = new AddToContainerResponse();
            r.name = "inv";
            r.id = item.id;
            r.amount = item.amount;
            r.attrs = item.attributes.values;
            r.captiion = item.caption;
            Server.SendTo(
                    Server.getClient(Server.getEntity(entityId)).connection, r);
        }
    }

}
