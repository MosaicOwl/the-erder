package ru.alastar.entities;

import com.alastar.game.enums.EntityType;
import com.badlogic.gdx.math.Vector3;

import ru.alastar.main.Server;
import ru.alastar.world.ServerWorld;

@SuppressWarnings("serial")
public class Player extends Entity {

	public int accountId;

	public Player(int id, ServerWorld w, String n, Vector3 pos,
			EntityType type, int aI) {
		super(id, w, n, pos, type);
		accountId = aI;
	}

	public void removeYourself() {
		try {
			Server.saveEntity(this);
			this.world.RemoveEntity(this);
			// this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
