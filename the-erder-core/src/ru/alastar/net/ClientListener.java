package ru.alastar.net;

import java.util.ArrayList;
import java.util.Hashtable;

import ru.alastar.main.net.requests.*;
import ru.alastar.main.net.responses.*;

import com.alastar.game.ContainerInfo;
import com.alastar.game.ContainersInfo;
import com.alastar.game.Entity;
import com.alastar.game.GameManager;
import com.alastar.game.Item;
import com.alastar.game.MainScreen;
import com.alastar.game.Map;
import com.alastar.game.TargetInfo;
import com.alastar.game.enums.EntityType;
import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.UpdateType;
import com.alastar.game.gui.GUICore;
import com.alastar.game.gui.constructed.ChatGUI;
import com.alastar.game.gui.constructed.StatusGUI;
import com.alastar.game.gui.net.NetGUIAnswer;
import com.alastar.game.gui.net.NetGUICore;
import com.alastar.game.gui.net.NetGUIInfo;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {
    public Kryo kryo;
	public ClientListener(EndPoint e) {
        kryo = e.getKryo();

        kryo.setRegistrationRequired(true);
        kryo.setAsmEnabled(true);
        
        kryo.register(EntityType.class);
        kryo.register(Hashtable.class);
        kryo.register(ArrayList.class);
        kryo.register(Entity.class);
        kryo.register(String.class);
        kryo.register(Integer.class);
        kryo.register(String[].class);
        kryo.register(UpdateType.class);
        kryo.register(Vector2.class);
        kryo.register(ItemType.class);

        kryo.register(LoginResponse.class);
        kryo.register(AddEntityResponse.class);
        kryo.register(SetData.class);
        kryo.register(ChatSendResponse.class);
        kryo.register(RemoveEntityResponse.class);
        kryo.register(RegisterResponse.class);
        kryo.register(AddStatResponse.class);
        kryo.register(AddSkillResponse.class);
        kryo.register(MessageResponse.class);
        kryo.register(AuthPacketRequest.class);
        kryo.register(CharacterChooseRequest.class);
        kryo.register(CreateCharacterRequest.class);
        kryo.register(InputRequest.class);
        kryo.register(MessagePacketRequest.class);
        kryo.register(RegistrationPacketRequest.class);
        kryo.register(AddCharacterResponse.class);
        kryo.register(LoadWorldResponse.class);
        kryo.register(CharacterRemove.class);
        kryo.register(UpdatePlayerResponse.class);
        kryo.register(NetGUIInfo.class);
        kryo.register(NetGUIAnswer.class);
        kryo.register(DropdownMenuRequest.class);
        kryo.register(CloseGUIResponse.class);
        kryo.register(TargetRequest.class);
        kryo.register(SpeechResponse.class);
        kryo.register(CreateContainerResponse.class);
        kryo.register(AddToContainerResponse.class);
        kryo.register(RemoveContainerResponse.class);
        kryo.register(RemoveFromContainerResponse.class);
        kryo.register(AddEquipResponse.class);
        kryo.register(RemoveEquipResponse.class);
        kryo.register(HitResponse.class);
        kryo.register(TargetResponse.class);
        kryo.register(TargetInfoResponse.class);

		//System.out.println("Client Handler have been started!");
	}

	public void received(Connection connection, Object object) {

	    
		 if (object instanceof SetData) {
		     SetData r = (SetData)object;
		     Client.id = r.id;
		 } 
	     else if (object instanceof LoginResponse) {
	         LoginResponse r = (LoginResponse)object;
	         if(r.succesful)
	         {
	           GUICore.enableOne("choose");  
	         }
	         else
	         {
	             
	         }
	     }
		 else if (object instanceof TargetInfoResponse) {
		     TargetInfoResponse r = (TargetInfoResponse)object;
             TargetInfo.setInfo(r.hits, r.mhits);
         }
	     else if (object instanceof TargetResponse) {
	         TargetResponse r = (TargetResponse)object;
	         Client.handleTarget(r.id);
	     }
	     else if (object instanceof AddEquipResponse) {
	         AddEquipResponse r = (AddEquipResponse)object;
	         Client.handleEquip(r);
	     }
	     else if (object instanceof RemoveEquipResponse) {
	         RemoveEquipResponse r = (RemoveEquipResponse)object;
	         Client.handleEquip(r.eid, r.slot);
	     }
	     else if (object instanceof ChatSendResponse) {
	         ChatSendResponse r = (ChatSendResponse)object;
	         ((ChatGUI)GUICore.getConstructedByName("Chat")).addEntry(r.sender, r.msg);
	     }
	     else if (object instanceof AddEntityResponse) {
	          AddEntityResponse r = (AddEntityResponse)object;
	          Entity e = new Entity(r.id, new Vector3(r.x, r.y, r.z), r.caption, r.type, r.warMode);
	          
	          if(e.id == Client.id){
	              Client.controlledEntity = e;
	              MainScreen.camera.position.x = e.position.x * GameManager.textureResolution;
	              MainScreen.camera.position.y = e.position.y * GameManager.textureResolution
	                       + (e.position.z * GameManager.textureResolution);
	          }
	          
	          Map.handleEntity(e);
	          
	     }
	     else if (object instanceof AddStatResponse) {
	          AddStatResponse r = (AddStatResponse)object;
              Client.handleStat(r.name, r.sValue, r.mValue);
         }
	      else if (object instanceof RemoveEntityResponse) {
	          RemoveEntityResponse r = (RemoveEntityResponse)object;
              Client.handleEntityRemove(r.id);
         }
	     else if (object instanceof AddSkillResponse) {
	          AddSkillResponse r = (AddSkillResponse)object;
              Client.handleSkill(r.name, r.sValue, r.mValue);
         }
	     else if (object instanceof CreateContainerResponse) {
	         CreateContainerResponse r = (CreateContainerResponse)object;
	         ContainersInfo.addContainer(r.name, new ContainerInfo(r.name, r.max));
	     } 
	     else if (object instanceof AddToContainerResponse) {
	         AddToContainerResponse r = (AddToContainerResponse)object;
             ContainersInfo.addToContainer(r.name, new Item(r.id, new Vector3(), r.captiion, r.type, r.amount, r.attrs));
         }
	     else if (object instanceof RemoveContainerResponse) {
	          RemoveContainerResponse r = (RemoveContainerResponse)object;
	          ContainersInfo.removeContainer(r.name);
	     }
	     else if (object instanceof RemoveFromContainerResponse) {
	           RemoveFromContainerResponse r = (RemoveFromContainerResponse)object;
	                 ContainersInfo.removeFromContainer(r.name, r.id);
	     }
	     else if (object instanceof AddCharacterResponse) {
	         AddCharacterResponse r = (AddCharacterResponse)object;
	         Client.handleChar(r.name, r.type);
	     }
	     else if (object instanceof RemoveEntityResponse) {
	         RemoveEntityResponse r = (RemoveEntityResponse)object;
	         Map.removeEntity(r.id); 
	     }
	     else if (object instanceof LoadWorldResponse) {
	         LoadWorldResponse r = (LoadWorldResponse)object;
	         MainScreen.currentStage = MainScreen.gui;
	         GUICore.addConstructedGUI(new StatusGUI(MainScreen.gui, "Status")); 
	         GUICore.addConstructedGUI(new ChatGUI(MainScreen.gui)); 
	         Client.LoadWorld(r.name);
	     }
	     else if (object instanceof UpdatePlayerResponse) {
	          UpdatePlayerResponse r = (UpdatePlayerResponse)object;
	          switch(r.updType){
	              case Position:
	                  Map.handleUpdate(r.id, new Vector3(r.x, r.y, r.z));
	              break;
                case All:
                    break;
                case Health:
                    break;
                case Name:
                    break;
                case Mode:
                    Map.handleUpdate(r.id, r.val);
                    break;
                default:
                    break;
	          }
	     } 
	     else if (object instanceof NetGUIInfo) {
	         NetGUIInfo r = (NetGUIInfo)object;
             NetGUICore.createGUIElement(r);
         }
	     else if (object instanceof CloseGUIResponse) {
	         CloseGUIResponse r = (CloseGUIResponse)object;
             GUICore.remove(r.name);
         }
	     else if (object instanceof MessageResponse) {
	         MessageResponse r = (MessageResponse)object;
	         System.out.println("Message! Contents: " + r.msg + " ID: " + r.type);
	         switch(r.type)
	         {
	             case 0:
	                 ((ChatGUI)GUICore.getConstructedByName("Chat")).addEntry("I", r.msg);
	                 break;
	             case 1: 
	                 if(Client.controlledEntity != null)
	                 Client.controlledEntity.drawMessageOverhead(r.msg);
	                 break;
	             case 2:
	                 if(Client.controlledEntity != null)
	                 Client.controlledEntity.drawMessageOverhead(r.msg);
	                 break;
	         }
         }
	     else if (object instanceof SpeechResponse) {
	         SpeechResponse r = (SpeechResponse)object;
             Client.handleSpeech(r.id, r.msg);
         }
	}

	@Override
	public void connected(Connection connection) {

	}

	@Override
	public void disconnected(Connection connection) {
		connection.close();
	}

}
