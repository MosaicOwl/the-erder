package com.alastar.game;

import com.badlogic.gdx.math.Vector3;

public class Interpolation
{
  public Vector3 to;
  public Transform transfrom;
  public float step = 11F;
  public boolean finished = false;
  
  public Interpolation(Transform t, Vector3 to)
  {
      this.to = to;
      this.transfrom = t;
      this.finished = false;
  }
  
  public void Step()
  {
      if(!finished){
      step -= 1F;
      this.transfrom.position.lerp(to, 1 / step);
      if(step <= 1)
          Finish();
      }
  }
  
  public void Finish()
  {
      finished = true;
  }
}
