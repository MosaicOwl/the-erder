package ru.alastar.game.projectiles;

import ru.alastar.game.Entity;

import com.alastar.game.enums.ProjectileType;
import com.badlogic.gdx.math.Vector3;

public class TestProjectile extends ru.alastar.physics.BaseProjectile
{

    public TestProjectile(int id, Vector3 from, Entity shooter, double angle)
    {
        super(id, from, shooter, angle);
        this.speed = 10;
        this.lifeTime = 100;
        this.maxSpeed = 10;
        this.type = ProjectileType.Arrow;
    }

}
