package ru.alastar.game.ai.npc;

import ru.alastar.game.Entity;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.game.systems.gui.hadlers.HireButtonGUIHandler;
import ru.alastar.main.net.Server;

import com.badlogic.gdx.math.Vector2;

public class GuardAI extends NPCAI
{

    public GuardAI()
    {
    }

    @Override
    public void OnDropdownRequest(Entity from)
    {
        NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("guard_dropdown_hire",
                new Vector2(this.entity.body.getPosition().x, this.entity.body.getPosition().y), new Vector2(
                        50, 50), "dropdown",
                "com.alastar.game.gui.GUILabel", "", "Hire"), Server
                .getClient(from));
        from.AddGUIHandler("dropdown", new HireButtonGUIHandler());
    }

    @Override
    public String getClassPath()
    {
        return "ru.alastar.game.ai.npc.GuardAI";
    }  
    
    @Override
    public void OnSeeEntity(Entity y)
    {
       // Main.Log("[GUARD]"," See the " + y.caption);
    }
}
