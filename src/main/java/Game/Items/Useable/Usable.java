package Game.Items.Useable;

import Game.Entities.Entity;
import Game.Items.Item;

/**
 * abstract parent class for all items that the player can use
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Usable extends Item {

    /**
     * the max amount of times that an itm can be used
     */
    protected int maxUses;

    /**
     * the amount of times that an item can be used
     */
    protected int uses;

    /**
     * sets the name, cost, and weight of an item
     * @author Harrison Brown
     * @param name   item name
     * @param cost   item cost
     * @param weight item weight
     * @author Harrison Brown
     */
    protected Usable(String name, int cost, int weight) {
        super(name, cost, weight);
    }

    /**
     * default constructor for Usable
     */
    protected Usable() {
        super();
    }

    /**
     * set the abstract method for Usable items
     * @author Harrison Brown
     * @param e the entity that used the potion
     */
    public abstract void use(Entity e);

    /**
     * a use method without parameters
     * @author Harrison Brown
     */
    public abstract void use();

    /**
     * returns the amount of times that an item has left to be used
     * @author Harrison Brown
     * @return int
     */
    public int checkUses() {
        return uses;
    }
}
