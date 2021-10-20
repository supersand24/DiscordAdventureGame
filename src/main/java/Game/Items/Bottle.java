package Game.Items;

import Game.Items.Useable.Liquids.Liquid;

public class Bottle extends Item {

    private boolean empty;

    private Liquid liquid = null;

    public Bottle() {
        empty = true;
    }

    public Bottle(Liquid liquid) {
        empty = false;
        this.liquid = liquid;
    }

    public void useBottle() {
            liquid.use();
            if (liquid.checkUses() == 0) {
                liquid = null;
            }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void fill(Liquid liquid) {
        if (empty) {
            this.liquid = liquid;
        }
    }

    public void makeEmpty() {
        liquid = null;
        empty = true;

    }





}
