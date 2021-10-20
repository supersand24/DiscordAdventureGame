package Game.Items.Useable;

import Game.Entities.Entity;
import Game.Items.Item;

public abstract class Usable extends Item {

    protected int maxUses;

    protected int uses;
    /**
     * sets the name, cost, and weight of an item
     *
     * @param name   item name
     * @param cost   item cost
     * @param weight item weight
     * @author Harrison Brown
     */
    protected Usable(String name, int cost, int weight) {
        super(name, cost, weight);
    }

    protected Usable() {
        super();
    }

    public abstract void use(Entity e);

    public abstract void use();

    public int checkUses() {
        return uses;
    }
}
