package Game.Entities.EnemyTypes.Rodents;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

public abstract class Rodent extends Enemy {

    public Rodent() {
        this.dmg = 2;
        this.maxHealth = 5;
        this.health = this.maxHealth;
        this.spd = BattleSystem.randomVal(5,9);
    }
}
