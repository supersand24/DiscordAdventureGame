package Game.Entities;

import Game.Items.Weapons.Sword;

/**
 * Player class
 * @author Harrison Brown
 * @version 0.1
 */
public class Player extends Entity {

    /**
     * player constructor
     * @param gold initial gold
     * @param maxHealth initial max health
     * @see Game.Entities.Entity
     */
    public Player(int gold, int maxHealth) {
        super(gold, maxHealth);
        holding[0] = new Sword("Slayer of Thots");
    }

    @Override
    public void attack() {
        ((Sword)holding[0]).attack();
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

    @Override
    public void checkHealthStatus() {
    }
}
