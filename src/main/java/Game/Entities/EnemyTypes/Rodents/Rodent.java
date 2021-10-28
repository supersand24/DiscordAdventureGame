package Game.Entities.EnemyTypes.Rodents;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

/**
 * Parent class for all rodent type enemies
 * @author Harrison Brown
 */
public abstract class Rodent extends Enemy {

    /**
     * deafault constructor for rodents
     */
    public Rodent() {
        this.dmg = 2;
        this.maxHealth = 5;
        this.health = this.maxHealth;
        this.setSpd(BattleSystem.randomVal(5,9));
    }
}
