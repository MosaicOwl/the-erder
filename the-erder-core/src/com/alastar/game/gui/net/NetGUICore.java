package com.alastar.game.gui.net;

import ru.alastar.net.Client;

import com.alastar.game.MainScreen;
import com.alastar.game.gui.GUICore;
import com.alastar.game.gui.GUIElement;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class NetGUICore
{
    
    public static void createConstructedGUI(String name, String classpath)
    {
        
    }
    
    public static void createGUIElement(final NetGUIInfo info)
    {
        try
        {
            GUIElement o = (GUIElement) Class.forName(info.elementClasspath).newInstance();
            ChangeListener el = new ChangeListener(){
                

                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    NetGUIAnswer r = new NetGUIAnswer();
                    r.name = info.name;
                    r.value = "";
                    Client.Send(r);
                }
                
            };
                
            o.setName(info.name);
            o.setPosition(info.position);
            o.setWidth(info.scale.x);
            o.setHandledVariable(info.variable);
            o.setHeight(info.scale.y);
            o.setText(info.text);
            o.setEventListener(el);
            
            if(!info.parentName.isEmpty())
               getNetGUIElement(info.parentName).addChild(o);
            else
               MainScreen.gui.addActor(o.getElementAsActor());
            
            GUICore.guiElements.put(info.name, o);
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    public static GUIElement getNetGUIElement(String name)
    {
        return GUICore.guiElements.get(name);
    }
    
    
}
