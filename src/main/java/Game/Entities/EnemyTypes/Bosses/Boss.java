package Game.Entities.EnemyTypes.Bosses;

import Game.Entities.EnemyTypes.Enemy;

public abstract class Boss extends Enemy {

    public Boss() {
        super();
        this.maxHealth = 300;
        this.health = 300;
        this.spd = 300;
        this.dmg = 300;
    }

}
