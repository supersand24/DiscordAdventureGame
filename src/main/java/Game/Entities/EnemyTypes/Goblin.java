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
        this.name = name;
        this.spd = BattleSystem.randomVal(3, 9);
    }

    @Override
    public void move() {

    }

    @Override
    public void block() {
        block = true;
        System.out.println(name + "braced for an attack!");
    }

    @Override
    public void useItem() {

    }
}
