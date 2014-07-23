package ru.alastar.entities;

import com.alastar.game.Tile;
import com.alastar.game.Transform;
import com.alastar.game.enums.EntityType;

import ru.alastar.main.Server;
import ru.alastar.world.ServerWorld;
import com.badlogic.gdx.math.Vector3;

public class Entity extends Transform {

	private static final long serialVersionUID = 1L;
	public int id = 0;
	public long lastMoveTime = System.currentTimeMillis();
	public boolean inBattle = false;
	public String caption = "Generic Entity";
	public ServerWorld world;
	public EntityType type;
	public int height = 2;

	public Entity(int id, ServerWorld w, String c, Vector3 pos, EntityType ty) {
		super(pos);
		this.id = id;
		this.world = w;
		this.caption = c;
		this.type = ty;
	}

	public void setPosition(Vector3 pos) {
		this.position = pos;
	}

	public boolean Move(int x, int y) {
		if ((System.currentTimeMillis() - lastMoveTime) > 250) {
			int obstacleHeight = 0;
			for (int i = 0; i < height; ++i) {
				if (world.GetTile(((int) this.position.x + x),
						((int) this.position.y + y), (int) this.position.z + i) != null)
					++obstacleHeight;
			}
			// Server.Log("[INPUT]: obstacle height: " + obstacleHeight);
			if (obstacleHeight < height) {
				this.position.x += x;
				this.position.y += y;
				this.position.z += obstacleHeight;
				CheckIfInAir();
				Server.UpdateEntityPosition(this);
				lastMoveTime = System.currentTimeMillis();
				// Server.Log("[INPUT]: player moved");
				return true;
			} else {
				Tile t = world.GetTile(((int) this.position.x + x),
						((int) this.position.y + y), (int) this.position.z);
				// Server.Log("[INPUT]: Tile is not null");
				if (t != null) {
					if (t.passable) {
						// Server.Log("[INPUT]: Tile is passable!");

						this.position.x += x;
						this.position.y += y;
						this.position.z += 1;
						CheckIfInAir();

						Server.UpdateEntityPosition(this);
						lastMoveTime = System.currentTimeMillis();
						// Server.Log("[INPUT]: player moved");
						return true;
					} else {
						// Server.Log("[INPUT]: Tile is not passable!");

						return false;
					}
				} else {
					// Server.Log("[INPUT]: path is passable!");

					this.position.x += x;
					this.position.y += y;
					CheckIfInAir();

					Server.UpdateEntityPosition(this);
					lastMoveTime = System.currentTimeMillis();
					// Server.Log("[INPUT]: player moved");
					return true;
				}

			}
		}
		// else
		// {
		// this.x += x;
		// this.y += y;
		// Server.UpdateEntityPosition(this);
		// lastMoveTime = DateTime.Now;
		// Server.AddConsoleEntry("[INPUT]: Staff move");

		// }
		// }
		else {
			// Server.Log("[INPUT]: Too early");
			return false;

		}

	}

	private void CheckIfInAir() {
		Tile t = world.GetTile(new Vector3(position.x, position.y,
				position.z - 1));
		for (int z = (int) position.z; z > world.zMin; --z) {
			t = world.GetTile(new Vector3(position.x, position.y, z));
			if (t == null) {
				position.z = z;
			} else
				break;
		}
	}
}
