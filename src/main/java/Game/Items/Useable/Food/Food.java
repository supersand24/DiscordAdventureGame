package Game.Items.Useable.Food;

import Game.Entities.Entity;
import Game.Items.Useable.Usable;

/**
 * a basic food class
 * @author Harrison Brown
 * @version 0.1
 *
 * will probably be changed to abstract as more food is added
 */
public abstract class Food extends Usable {

    /**
     * default constructor for food
     * @author Harrison Brown
     */
    public Food() {
        super("Food", 1,1);
    }

}
