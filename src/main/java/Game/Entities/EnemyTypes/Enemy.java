package Game.Entities.EnemyTypes;

import Game.Entities.Entity;
import Game.Items.Useable.Usable;

/**
 * Parent class for all enemies
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Enemy extends Entity {
    protected int dmg;

    /**
     * Constructor for enemy
     * @author Harrison Brown
     */
    public Enemy() {
        super();
    }

    /**
     * attack method for enemy, takes in an entity
     * @author Harrison Brown
     * @param entity the entity to attack
     */
    @Override
    public void attack(Entity entity) {
        if (entity.isBlocking()) {
            this.setLastAction(entity.ifBlock());
        } else {
            String msg = "Attacked " + entity.getName() + " for " + dmg + " damage";
            System.out.println(msg);
            entity.setHealth(entity.getHealth() - dmg);
            this.setLastAction(msg);
        }
    }

    /**
     * block method for enemy
     * @author Harrison Brown
     */
    @Override
    public void block() {
        this.switchBlock();
        String msg = "braced for an attack";
        System.out.println(msg);
        this.setLastAction(msg);
    }

    @Override
    public void move() {

    }

    @Override
    public void useItem(Usable item) {

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
