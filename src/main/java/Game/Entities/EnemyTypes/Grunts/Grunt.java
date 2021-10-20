package Game.Entities.EnemyTypes.Grunts;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

public abstract class Grunt extends Enemy {

    Grunt() {
        super();
        this.dmg = 5;
        this.spd = BattleSystem.randomVal(3, 9);
        this.maxHealth = 10;
        this.health = this.maxHealth;
    }
}
