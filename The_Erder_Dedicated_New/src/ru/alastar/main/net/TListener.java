package ru.alastar.main.net;

import java.util.ArrayList;
import java.util.Date;
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

        kryo.register(EntityType.class);
        kryo.register(Hashtable.class);
        kryo.register(ArrayList.class);
        kryo.register(Entity.class);
        kryo.register(String.class);
        kryo.register(Integer.class);
        kryo.register(String[].class);
        kryo.register(ModeType.class);
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
        kryo.register(CommandRequest.class);
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

        // Main.Log("[LISTENER]", "All packets registered!");
    }

    public void received(Connection connection, Object object)
    {
        try
        {
            ConnectedClient c = Server.getClient(connection);
            if (c != null)
            {
                // Main.Log("[INFO]", "CTM - " + System.currentTimeMillis() +
                // " lastPacket - " + c.lastPacket);
                if ((new Date().getTime() - c.lastPacket.getTime()) > packetDelay)
                {
                    if (object instanceof CommandRequest)
                    {
                        Server.HandleCommand(((CommandRequest) object),
                                connection);
                    } else if (object instanceof AuthPacketRequest)
                    {
                        AuthPacketRequest r = (AuthPacketRequest) object;
                        Server.Login(r.login, r.pass, connection);
                    } else if (object instanceof CharacterChooseRequest)
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
                        Server.HandleTarget(r.id, Server.getClient(connection).controlledEntity);
                    }
                    c.lastPacket = new Date();
                }
            } else
                Main.Log("[ERROR]", "Connected client is null");
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
        connection.close();
        Server.removeClient(connection);
    }
}
