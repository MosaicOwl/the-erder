package ru.alastar.main.net;

import java.util.ArrayList;
import java.util.Hashtable;

import ru.alastar.enums.EntityType;
import ru.alastar.game.Entity;
import ru.alastar.game.systems.gui.NetGUIAnswer;
import ru.alastar.game.systems.gui.NetGUIInfo;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.main.Main;
import ru.alastar.main.net.requests.*;
import ru.alastar.main.net.responses.*;

import com.alastar.game.enums.*;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public class TListener extends Listener
{

    public static Kryo  kryo;
    public static float packetDelay = 100F;

    public TListener(EndPoint e)
    {
        kryo = e.getKryo();
        

        // –егистраци€ пакетов. *ƒолжна выполн€тьс€ в такой же
        // последовательности, что и на сервере!*
        kryo.setRegistrationRequired(true);
        kryo.setAsmEnabled(true);

        registerPacket(EntityType.class, true);
        registerPacket(Hashtable.class, true);
        registerPacket(ArrayList.class, true);
        registerPacket(Entity.class, true);
        registerPacket(String.class, true);
        registerPacket(Integer.class, true);
        registerPacket(String[].class, true);
        registerPacket(UpdateType.class, true);
        registerPacket(Vector2.class, true);
        registerPacket(ItemType.class, true);

        registerPacket(LoginResponse.class, true);
        registerPacket(AddEntityResponse.class, true);
        registerPacket(SetData.class, true);
        registerPacket(ChatSendResponse.class, true);
        registerPacket(RemoveEntityResponse.class, true);
        registerPacket(RegisterResponse.class, true);
        registerPacket(AddStatResponse.class, true);
        registerPacket(AddSkillResponse.class, true);
        registerPacket(MessageResponse.class, true);
        registerPacket(AuthPacketRequest.class, false);
        registerPacket(CharacterChooseRequest.class, false);
        registerPacket(CreateCharacterRequest.class, false);
        registerPacket(InputRequest.class, false);
        registerPacket(MessagePacketRequest.class, true);
        registerPacket(AddCharacterResponse.class, true);
        registerPacket(LoadWorldResponse.class, true);
        registerPacket(CharacterRemove.class, false);
        registerPacket(UpdatePlayerResponse.class, true);
        registerPacket(NetGUIInfo.class, true);
        registerPacket(NetGUIAnswer.class, true);
        registerPacket(DropdownMenuRequest.class, true);
        registerPacket(CloseGUIResponse.class, true);
        registerPacket(TargetRequest.class, true);
        registerPacket(SpeechResponse.class, true);
        registerPacket(CreateContainerResponse.class, true);
        registerPacket(AddToContainerResponse.class, true);
        registerPacket(RemoveContainerResponse.class, true);
        registerPacket(RemoveFromContainerResponse.class, true);
        registerPacket(AddEquipResponse.class, true);
        registerPacket(RemoveEquipResponse.class, true);
        registerPacket(HitResponse.class, true);
        registerPacket(TargetResponse.class, true);
        registerPacket(TargetInfoResponse.class, true);
    }

    public void registerPacket(@SuppressWarnings("rawtypes") Class c, boolean filtering)
    {
        kryo.register(c);
        PacketFiltering.addFilterFor(c, filtering);
    }

    public void received(Connection connection, Object object)
    {
        try
        {
            if(PacketFiltering.checkFilter(object.getClass(), connection)){
            if (object instanceof AuthPacketRequest)
            {                
                AuthPacketRequest r = (AuthPacketRequest) object;
                Server.Login(r.login, r.pass, connection);
            }
            if (object instanceof CharacterChooseRequest)
            {
                CharacterChooseRequest r = (CharacterChooseRequest) object;
                Server.HandleCharacterChoose(r.nick, connection);
            } else if (object instanceof CreateCharacterRequest)
            {
                CreateCharacterRequest r = (CreateCharacterRequest) object;
                Server.HandleCharacterCreate(r.nick, r.type, connection);
            } else if (object instanceof CharacterRemove)
            {
                CharacterRemove r = (CharacterRemove) object;
                Server.HandleCharacterRemove(r.nick, connection);
            } else if (object instanceof InputRequest)
            {
                InputRequest r = (InputRequest) object;
                Server.HandleMove(r.x, r.y, connection);
            } else if (object instanceof NetGUIAnswer)
            {
                NetGUIAnswer r = (NetGUIAnswer) object;
                NetGUISystem.handleAnswer(r, connection);
            } else if (object instanceof DropdownMenuRequest)
            {
                DropdownMenuRequest r = (DropdownMenuRequest) object;
                NetGUISystem.handleDropRequest(r, connection);
            } else if (object instanceof ChatSendResponse)
            {
                ChatSendResponse r = (ChatSendResponse) object;
                Server.HandleChat(r.msg, connection);
            } else if (object instanceof TargetRequest)
            {
                TargetRequest r = (TargetRequest) object;
                Server.HandleTarget(r.id,
                        Server.getClient(connection).controlledEntity);
            }
         }
        } catch (Exception e)
        {
            Main.Log("[SERVER]", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void connected(Connection connection)
    {
        if (!Server.hasClient(connection))
        {
            Server.addClient(connection);
        }
    }

    @Override
    public void disconnected(Connection connection)
    {
        Server.removeClient(connection);
        connection.close();
    }
}
