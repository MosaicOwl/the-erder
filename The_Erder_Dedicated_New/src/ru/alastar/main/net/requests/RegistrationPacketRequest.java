package ru.alastar.main.net.requests;

public class RegistrationPacketRequest
{

    public String login = "", pass = "", mail = "";
    /*
     * try { int loginL = m.readInt(); byte[] loginB = new byte[loginL]; for(int
     * i = 0; i < loginL; ++i) { loginB[i] = m.readByte(); }
     * 
     * int passL = m.readInt(); byte[] passB= new byte[passL]; for(int i = 0; i
     * < passL; ++i) { passB[i] = m.readByte(); }
     * 
     * int mailL = m.readInt(); byte[] mailB= new byte[mailL]; for(int i = 0; i
     * < mailL; ++i) { mailB[i] = m.readByte(); }
     * 
     * login = new String(loginB, "UTF-8"); pass = new String(passB, "UTF-8");
     * mail = new String(mailB, "UTF-8");
     * 
     * } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
     * 
     * ResultSet r =
     * DatabaseClient.commandExecute("SELECT * FROM accounts WHERE login = '" +
     * login + "'"); try{ boolean val = r.next(); if(val == true) {
     * PacketGenerator.generatePacketTo(PacketID.Reg, ctx, false);
     * Server.Log("Register: Login - " + login + " pass - " + pass + " mail - "
     * + mail + " unsuccessful!"); } else {
     * PacketGenerator.generatePacketTo(PacketID.Reg, ctx, true); r =
     * DatabaseClient
     * .commandExecute("INSERT INTO accounts(login, password, mail) VALUES ('"+
     * login + "','"+pass+"','"+mail+"')"); Server.Log("Register: Login - " +
     * login + " pass - " + pass + " mail - " + mail + " successful!"); }
     * }catch(Exception e) { Server.Log("Ошибка! " +
     * e.getLocalizedMessage()); e.printStackTrace();
     * PacketGenerator.generatePacketTo(PacketID.Reg, ctx, false); } }
     */
}
