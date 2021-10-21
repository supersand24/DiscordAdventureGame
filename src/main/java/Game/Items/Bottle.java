package Game.Items;

import Game.Items.Useable.Potion;

/**
 * container class for potions
 * @author Harrison Brown
 */
public class Bottle extends Item {

    /**
     * state of emptiness of bottle
     */
    private boolean empty;

    /**
     * potion current stored in bottle
     */
    private Potion potion = null;

    /**
     * default constructor for bottle
     * creates an empty
     * @author Harrison Brown
     */
    public Bottle() {
        empty = true;
    }

    /**
     * creates a bottle filled with the given potion
     * @author Harrison Brown
     * @param potion a potion to be put in the bottle
     */
    public Bottle(Potion potion) {
        empty = false;
        this.potion = potion;
    }

    /**
     * calls the stored potions use method
     * @author Harrison Brown
     */
    public void useBottle() {
            potion.use();
            if (potion.checkUses() == 0) {
                potion = null;
            }
    }

    /**
     * checks if the bottle is empty
     * @author harrison Brown
     * @return boolean
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * fills an existing potion wth the given potion
     * @author Harrison Brown
     * @param potion the potion to fill it with
     */
    public void fill(Potion potion) {
        if (empty) {
            this.potion = potion;
        }
    }

    /**
     * empties the bottle
     * @author Harrison Brown
     */
    public void makeEmpty() {
        potion = null;
        empty = true;

    }

    /**
     * returns the current contents of the bottle
     * @author Harrison Brown
     * @return potion
     */
    public Potion getContents() {
        return potion;
    }

    /**
     * returns the contents of the bottle as a string
     * @author Harrison Brown
     * @return string
     */
    public String getContentsType() {
        return potion.getClass().getSimpleName();
    }
}
