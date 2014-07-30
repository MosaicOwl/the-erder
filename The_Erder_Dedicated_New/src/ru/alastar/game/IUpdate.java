package ru.alastar.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import ru.alastar.main.net.ConnectedClient;
import ru.alastar.world.ServerWorld;

public interface IUpdate
{
    public void UpdateTo(ConnectedClient c);

    public void RemoveTo(ConnectedClient c);

    public void tryRemoveNear(IUpdate i);

    public void tryAddToNear(IUpdate e);

    public int getType();

    public void UpdateAround();

    public ServerWorld getWorld();

    public Vector2 getPosition();

    public ArrayList<IUpdate> getAround();

    public int getId();

}
