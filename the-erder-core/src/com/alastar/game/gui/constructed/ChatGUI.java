package com.alastar.game.gui.constructed;

import java.util.ArrayList;

import ru.alastar.main.net.responses.ChatSendResponse;
import ru.alastar.net.PacketGenerator;

import com.alastar.game.ErderGame;
import com.alastar.game.GameManager;
import com.alastar.game.MainScreen;
import com.alastar.game.gui.GUIButton;
import com.alastar.game.gui.GUIChatWindow;
import com.alastar.game.gui.GUIElement;
import com.alastar.game.gui.GUITextField;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

public class ChatGUI implements ConstructedGUI
{
    private GUIChatWindow window;
    private GUITextField field;
    private GUIButton sendMsg;
    
    private String name;
    private ArrayList<GUIElement> actors = new ArrayList<GUIElement>();
    
    public ChatGUI()
    {
        this.name = "Chat";
        
        field = new GUITextField("", new TextField("", GameManager.getSkin(GameManager.selectedSkin), "textField"));
        field.setWidth(250);
        field.setPosition(new Vector2(10,1800));
        window = new GUIChatWindow("Chat", new Window("Chat", GameManager.getSkin(GameManager.selectedSkin), "window"), new Vector2(10,1850), new Vector2(900, 250), 10, 10, 0, 0);
        
        ChangeListener e = new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                ChatSendResponse r = new ChatSendResponse();
                r.msg = field.getText();
                field.clear();
                
                PacketGenerator.generatePacket(r);
            }
            
        };
        
        FocusListener fl = new FocusListener(){
            @Override
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                focusChanged(event);
            }
            @Override
            public void scrollFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                focusChanged(event);
            }
            
            private void focusChanged (FocusEvent event) {
                 if(event.isFocused())
                 {
                     System.out.println("focus");
                     ErderGame.canMove = false;

                 }
                 else
                 {
                     System.out.println("Unfocus");
                     ErderGame.canMove = true;
                 }
            }
            
        };
         field.setEventListener(fl);
        
        sendMsg = new GUIButton("send_to_chat", new TextButton("^",GameManager.getSkin(GameManager.selectedSkin), "button"), e);
        sendMsg.setPosition(new Vector2(270, 1800));
        
        actors.add(window);
        actors.add(field);
        actors.add(sendMsg);

        MainScreen.currentStage.addActor(window.getElementAsActor());
        MainScreen.currentStage.addActor(field.getElementAsActor());
        MainScreen.currentStage.addActor(sendMsg.getElementAsActor());

        window.Show();
    }
    
    public void addEntry(String name, String msg)
    {
        window.addEntry(name, msg);
    }
    
    @Override
    public void Destroy()
    {
        window.Destroy();
        field.Destroy();
        sendMsg.Destroy();
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public ArrayList<GUIElement> getElements()
    {
        return actors;
    }
    
    @Override
    public void notifyAllElements(String s, String val)
    {
     
    }
}
