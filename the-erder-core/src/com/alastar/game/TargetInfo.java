package com.alastar.game;

public class TargetInfo
{
  public static int hits = 0, mhits = 0;
  
  public static void setInfo(int h, int m)
  {
      System.out.println("Set target info " + h + " " + m);
      hits = h;
      mhits = m;
  }
}
