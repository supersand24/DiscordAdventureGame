package Game.Entities.EnemyTypes.Tanks;

import Game.BattleSystem;
import Game.Entities.EnemyTypes.Enemy;

public abstract class Tank extends Enemy {
    public Tank() {
        super();
        this.dmg = 15;
        this.maxHealth = 150;
        this.health = this.maxHealth;
        this.spd = BattleSystem.randomVal(5,7);
    }
}
