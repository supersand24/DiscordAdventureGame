package Game.Entities.EnemyTypes.Grunts;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

/**
 * Parent class for all grunt type enemies
 * @author Harrison Brown
 */
public abstract class Grunt extends Enemy {

    /**
     * default constructor for grunts
     */
    Grunt() {
        super();
        this.dmg = 5;
        this.setSpd(BattleSystem.randomVal(3, 9));
        this.maxHealth = 10;
        this.health = this.maxHealth;
    }
}
