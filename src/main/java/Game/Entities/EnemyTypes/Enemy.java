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
     * @author Harrison Brown
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
     * @author Harrison Brown
     * @param type the enemy type
     * @param dmg the damage value of the enemy
     */
    Enemy(String type, int dmg) {
        super(10,10);
        this.dmg = dmg;
    }

    /**
     * Constructor for enemy
     * @author Harrison Brown
     * @param type the string of the type of enemy
     */
    Enemy(String type) {
        super(10,10);
        this.dmg = 5;
    }

    /**
     * attack method for enemy, takes in an entity
     * @author Harrison Brown
     * @param entity the entity to attack
     */
    @Override
    public String attack(Entity entity) {
        if (entity.isBlocking()) {
            return entity.ifBlock();
        } else {
            String msg = name + " attacked " + entity.getName() + " for " + dmg + " damage!";
            System.out.println(msg);
            entity.setHealth(entity.getHealth() - dmg);
            return msg;
        }
    }

    /**
     * block method for enemy
     * @author Harrison Brown
     */
    @Override
    public String block() {
        this.switchBlock();
        String msg = name + "braced for an attack!";
        System.out.println(msg);
        return msg;
    }

    /**
     * toString for Enemy
     * @author Harrison Brown
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("TYPE: ").append(getClass().getSimpleName()).append("\n");
        msg.append("CALLED: ").append(name).append("\n");
        msg.append("SPEED: ").append(spd).append("\n");
        return msg.toString();

    }
}
