package ru.alastar.main.net.responses;

import java.util.Hashtable;

import com.alastar.game.enums.ItemType;

public class AddEquipResponse
{
  public int eid;
  public String slot;
  public int                        id, amount;
  public String                     captiion;
  public Hashtable<String, Integer> attrs;
  public ItemType type;
}
