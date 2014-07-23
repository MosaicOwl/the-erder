package ru.alastar.game.systems;

import ru.alastar.game.Entity;

public class BattleSystem
{
    public static int   baseDamage = 1;
    public static float baseSpeed  = 3F; // in seconds
    public static float baseRange  = 2;

    public static int calculateDamage(Entity from, Entity to)
    {
        int d = baseDamage;
        /*
         * Calculating attack damage over here
         */

        d += from.stats.get("Strength").value / 25;
        return d;
    }

    public static float getWeaponSpeed(Entity from)
    {
        float s = baseSpeed;
        /*
         * Calculating attack speed over here
         */
        s += from.stats.get("Dexterity").value / 25;
        return s;
    }

    public static float getWeaponRange(Entity e)
    {
        return baseRange;
    }

}
