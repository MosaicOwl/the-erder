package com.alastar.game.gui.constructed;


import ru.alastar.net.Client;

import com.alastar.game.GameManager;
import com.alastar.game.gui.GUIButton;
import com.alastar.game.gui.GUIServerList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ServersListGUI extends BaseConstructed
{
    private GUIServerList list;
    
    public ServersListGUI(Stage serversStage)
    {
        super(serversStage, "servers_list");
        list = new GUIServerList();
        actors.add(list);
        register();
    }

    @Override
    public void Destroy()
    {
        list.Destroy();
    }
    
    public void addServer(String name, final String address)
    {
        ChangeListener el = new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Client.init(address);
            }
        };
        list.addChild(new GUIButton(name, new TextButton(name, GameManager.getSkin(GameManager.selectedSkin), "button"), el));
    } 
    
    @Override
    public void Hide()
    {
        list.Hide();
    }
    
    @Override
    public void Show()
    {
        list.Show();
    }

}
