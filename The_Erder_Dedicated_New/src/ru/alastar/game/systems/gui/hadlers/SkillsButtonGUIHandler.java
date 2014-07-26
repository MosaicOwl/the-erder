package ru.alastar.game.systems.gui.hadlers;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.net.ConnectedClient;

public class SkillsButtonGUIHandler implements GUIHandler
{

    @Override
    public void handle(String[] args, ConnectedClient c)
    {
        if (c.controlledEntity.haveGUI("skills_list"))
        {
            c.controlledEntity.closeGUI("skills_list");
        } else
        {
            NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("skills_list",
                    new Vector2(400, 400), new Vector2(500, 1000), "",
                    "com.alastar.game.gui.GUIList", "", "SKills"), c);
            for (String n : c.controlledEntity.skills.vals.keySet())
            {
                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo(
                        "skills_list_element_" + n.toLowerCase(),
                        new Vector2(), new Vector2(100, 100), "skills_list",
                        "com.alastar.game.gui.GUILabel", n.toLowerCase()
                                + "_value", n + ":"), c);
            }
        }
    }

}
