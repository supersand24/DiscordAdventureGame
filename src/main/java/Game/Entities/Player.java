package Game.Entities;

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
    }

    @Override
    public void attack() {
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
