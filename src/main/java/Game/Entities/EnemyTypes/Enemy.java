package Game.Entities.EnemyTypes;

import Game.Entities.Entity;

/**
 * Parent class for all enemies
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Enemy extends Entity {
    protected int dmg;

    /**
     * constructor for basic enemy
     * @param gold current gold amount
     * @param maxHealth enemy maxHealth
     * @see Game.Entities.Entity
     */
    Enemy(int gold, int maxHealth, String type, int dmg) {
        super(gold, maxHealth);
        this.dmg = dmg;
    }

    /**
     * constructor for enemy
     * @param type the enemy type
     * @param dmg the damage of the enemy
     */
    Enemy(String type, int dmg) {
        super(10,10);
        this.dmg = dmg;
    }

    /**
     * Constructor for enemy
     * @param type the string of the type of enemy
     */
    Enemy(String type) {
        super(10,10);
        this.dmg = 5;
    }

    /**
     * attack method for enemy, takes in an entity
     * @param p the entity to attack
     */
    @Override
    public void attack(Entity p) {
        p.setHealth(p.getHealth()-dmg);
    }

    @Override
    public String toString() {
        String msg = "";
        msg += "TYPE: " + getClass().getSimpleName() + "\n";
        msg += "CALLED: " + name + "\n";
        msg += "SPEED: " + spd + "\n";
        return msg;
    }
}
