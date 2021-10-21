package Game.Items.Useable;

import Game.Entities.Entity;

/**
 * a basic food class
 * @author Harrison Brown
 * @version 0.1
 *
 * will probably be changed to abstract as more food is added
 */
public class Food extends Usable {

    /**
     * default constructor for food
     * @author Harrison Brown
     */
    public Food() {
        super("Food", 1,1);
    }

    /**
     * override method use from Usable class
     * @author Harrison Brown
     */
    @Override
    public void use() {

    }

    /**
     * override for abstract use with entity parameter
     * @author Harrison Brown
     * @param e the entity that used the potion
     */
    @Override
    public void use(Entity e) {

    }
}
