package Game.Entities.EnemyTypes;

import Game.BattleSystem;

import java.util.Random;

/**
 * enemy goblin class
 * @author Harrison Brown
 * @version 0.1
 */
public class Goblin extends Enemy {
    /**
     * constructor for goblin
     */
    public Goblin(String name) {
        super("Goblin");
        this.called = name;
        this.spd = BattleSystem.randomVal(3, 9);
    }

    @Override
    public void move() {

    }

    @Override
    public void block() {

    }

    @Override
    public void useItem() {

    }
}
