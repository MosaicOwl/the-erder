package ru.alastar.main;

import com.esotericsoftware.kryonet.Connection;

public class ServerInfo
{
  public String name;
  public String address;
  public Connection connection;
  public ServerState state;
  
  public ServerInfo(String name, String a, Connection c, ServerState s)
  {
      this.name = name;
      this.address = a;
      this.connection = c;
      this.state = s;
  }
}
