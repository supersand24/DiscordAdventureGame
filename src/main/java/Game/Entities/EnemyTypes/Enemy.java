package Game.Entities.EnemyTypes;

import Game.Entities.Entity;

/**
 * Parent class for all enemies
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Enemy extends Entity {

    /**
     * constructor for basic enemy
     * @param gold current gold amount
     * @param maxHealth enemy maxHealth
     */
    Enemy(int gold, int maxHealth) {
        super(gold, maxHealth);
    }

    @Override
    public void attack() {

    }

}
