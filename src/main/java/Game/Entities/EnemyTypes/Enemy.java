package Game.Entities.EnemyTypes;

import Game.Entities.Entity;

/**
 * Parent class for all enemies
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Enemy extends Entity {
    String type;
    /**
     * constructor for basic enemy
     * @param gold current gold amount
     * @param maxHealth enemy maxHealth
     * @see Game.Entities.Entity
     */
    Enemy(int gold, int maxHealth, String type) {
        super(gold, maxHealth);
        this.type = type;
    }

    Enemy(String type) {
        super(10,10);
        this.type = type;
    }

    @Override
    public void attack() {

    }

    @Override
    public void checkHealthStatus() {
        if (health <= 0) {
            System.out.println(this.type + " is dead");
        }
    }

}
