package Game.Entities.EnemyTypes.Bosses;

import Game.Entities.EnemyTypes.Enemy;

/**
 * parent class for all boss type enemies
 * @author Harrison Brown
 */
public abstract class Boss extends Enemy {

    /**
     * default constructor for bosses
     */
    public Boss() {
        super();
        this.maxHealth = 300;
        this.health = 300;
        this.setSpd(300);
        this.dmg = 300;
    }

}
