package Game.Entities.EnemyTypes.Tanks;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

/**
 * parent class for all tank type enemies
 * @author Harrison Brown
 */
public abstract class Tank extends Enemy {

    /**
     * default constructor for tanks
     * @author Harrison Brown
     */
    public Tank() {
        super();
        this.dmg = 15;
        this.maxHealth = 150;
        this.health = this.maxHealth;
        this.spd = BattleSystem.randomVal(5,7);
    }
}
