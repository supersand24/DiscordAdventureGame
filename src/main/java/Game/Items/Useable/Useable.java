package Game.Items.Useable;

import Game.Entities.Entity;
import Game.Items.Item;

public abstract class Useable extends Item {

    /**
     * sets the name, cost, and weight of an item
     *
     * @param name   item name
     * @param cost   item cost
     * @param weight item weight
     * @author Harrison Brown
     */
    protected Useable(String name, int cost, int weight) {
        super(name, cost, weight);
    }

    abstract void use(Entity e);

    abstract void use();
}
