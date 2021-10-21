package Game.Items;

import Game.Items.Useable.Potion;

public class Bottle extends Item {

    private boolean empty;

    private Potion potion = null;

    public Bottle() {
        empty = true;
    }

    public Bottle(Potion potion) {
        empty = false;
        this.potion = potion;
    }

    public void useBottle() {
            potion.use();
            if (potion.checkUses() == 0) {
                potion = null;
            }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void fill(Potion potion) {
        if (empty) {
            this.potion = potion;
        }
    }

    public void makeEmpty() {
        potion = null;
        empty = true;

    }

    public Potion getContents() {
        return potion;
    }

    public String getContentsType() {
        return potion.getClass().getSimpleName();
    }
}
