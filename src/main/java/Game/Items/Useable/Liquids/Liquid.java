package Game.Items.Useable.Liquids;

import Game.Entities.Entity;
import Game.Items.Useable.Usable;

public abstract class Liquid extends Usable {

    protected Liquid() {
        super();
    }

    protected Liquid(String name, int a, int b) {
        super(name, a, b);
    }
}
