package ru.alastar.game.systems.gui;

import com.alastar.game.Tile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

import ru.alastar.game.Entity;
import ru.alastar.game.systems.gui.hadlers.TileButtonGUIHandler;
import ru.alastar.main.Main;
import ru.alastar.main.net.ConnectedClient;
import ru.alastar.main.net.Server;
import ru.alastar.main.net.requests.DropdownMenuRequest;
import ru.alastar.main.net.responses.CloseGUIResponse;

public class NetGUISystem
{

    public static void sendGUIElement(NetGUIInfo info, ConnectedClient c)
    {
        Server.SendTo(c.connection, info);
    }

    public static NetGUIInfo CreateGUIInfo(String name, Vector2 position,
            Vector2 scale, String parentName, String elementClasspath,
            String variable, String text)
    {
        NetGUIInfo r = new NetGUIInfo();
        r.name = name;
        r.position = position;
        r.parentName = parentName;
        r.scale = scale;
        r.text = text;
        r.variable = variable;
        r.elementClasspath = elementClasspath;
        return r;

    }

    // Main method, sending net gui
    public static void OpenGUI(NetGUIInfo info, ConnectedClient c)
    {
        c.controlledEntity.AddGUI(info);
        sendGUIElement(info, c);
    }

    public static void handleAnswer(NetGUIAnswer r, Connection connection)
    {
        ConnectedClient c = Server.getClient(connection);
        c.controlledEntity.invokeGUIHandler(r, c);
    }

    public static void handleDropRequest(DropdownMenuRequest r,
            Connection connection)
    {
        try
        {
            ConnectedClient c = Server.getClient(connection);

            if (r.type == 0)
            {

                if (c.controlledEntity.haveGUI("dropdown"))
                {
                    c.controlledEntity.closeGUI("dropdown");
                }

                String t = "nothing";
                Tile tile = c.controlledEntity.world.GetTile((int) r.x,
                        (int) r.y, (int) c.controlledEntity.pos.z);
                if (tile != null)
                {
                    t = tile.type.name();

                    NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(
                            "dropdown", new Vector2(r.x, r.y
                                    + tile.position.z), new Vector2(50, 50),
                            "", "com.alastar.game.gui.GUIDropdown", "",
                            "Tile(X:" + r.x + ",Y:" + r.y + ")"), c);
                    NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(
                            "tile_dropdown_info", new Vector2(r.x, r.y),
                            new Vector2(50, 50), "dropdown",
                            "com.alastar.game.gui.GUILabel", "", "Just " + t),
                            c);
                    c.controlledEntity.AddGUIHandler("dropdown",
                            new TileButtonGUIHandler());

                    Main.Log("[DEBUG]", "Tile touch on " + r.x + " " + r.y);
                }
            } else if (r.type == 1)
            {
                Entity e = Server.getEntity(r.id);
                if (e != null)
                {
                    e.ProcessDropdown(c);
                }
            }
        } catch (Exception e)
        {
            Server.handleError(e);
        }
    }

    public static void closeGUI(String string, Entity entity)
    {
        CloseGUIResponse r = new CloseGUIResponse();
        r.name = string;
        Server.SendTo(Server.getClient(entity).connection, r);
    }
}
