package ru.alastar.game.ai;

import ru.alastar.enums.EntityType;
import ru.alastar.game.Entity;

public class BaseHostileAI extends BaseAI
{
    public BaseHostileAI()
    {

    }

    @Override
    public void OnSeeEntity(final Entity y)
    {
        if (y.type == EntityType.Human)
        {
            if (!getEntity().alreadyTargets())
            {
                this.entity.toggleWar(true);
                entity.actToTarget(y);
                StartChasingTarget();
            }
        }
    }

}
