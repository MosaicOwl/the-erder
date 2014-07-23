package ru.alastar.main.net.requests;

public class AuthPacketRequest
{
    public String login;
    public String pass;

    /*
     * ResultSet r =
     * DatabaseClient.commandExecute("SELECT * FROM accounts WHERE login = '" +
     * login + "'"); try{ r.next(); if(r.getString("password").equals(pass)) {
     * Server.Log("Auth: Login - " + login + " pass - " + pass +
     * " successful!"); Server.AuthProcess(r, ctx); } else {
     * PacketGenerator.generatePacketTo(PacketID.Auth, ctx, false);
     * Server.Log("Auth: Login - " + login + " pass - " + pass +
     * " unsuccessful!");
     * 
     * } }catch(Exception e) { Server.Log("Ошибка! " +
     * e.getLocalizedMessage()); PacketGenerator.generatePacketTo(PacketID.Auth,
     * ctx, false); } Server.Log("Auth: Login - " + login + " pass - " + pass);
     */
}
