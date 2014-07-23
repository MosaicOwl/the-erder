package com.alastar.game.gui.constructed;

import java.util.ArrayList;

import com.alastar.game.gui.GUIElement;

public interface ConstructedGUI
{
  public void Destroy();
  public String getName();
  public ArrayList<GUIElement> getElements();
  public void notifyAllElements(String s, String val);
}
