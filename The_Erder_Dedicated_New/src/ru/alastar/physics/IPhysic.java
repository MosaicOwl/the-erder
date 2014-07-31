package ru.alastar.physics;

import ru.alastar.game.IUpdate;

public interface IPhysic extends IUpdate
{
  public PhysicalData getData();
  public void UpdatePhysicalData( int z, boolean b );
  @Override
  public int getType();
  @Override
  public int getId();
}
