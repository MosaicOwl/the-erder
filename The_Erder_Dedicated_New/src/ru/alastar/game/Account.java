package ru.alastar.game;

public class Account
{
    public int    id;
    public String login;
    public String pass;
    public String mail;

    public Account(int id, String l, String p, String m)
    {
        this.id = id;
        this.login = l;
        this.pass = p;
        this.mail = m;
    }
}
