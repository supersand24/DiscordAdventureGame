package Game.Items.Weapons;

import Game.Entities.Entity;

/**
 * Sword weapon class
 * @author Harrison Brown
 * @version 0.1
 */
public class Sword extends Weapon {
    /**
     * constructor to create a sword with the given name
     * @author Harrison Brown
     * @param name The name of the Sword
     */
    public Sword(String name, String owner) {
        super(name,20, 15, 10, owner);
    }

    /**
     * default constructor for sword
     * @author Harrison Brown
     */
    public Sword() {
        super("temp", 20, 15, 10);
    }

    //any special methods for sword go here
    /*
    public void attack(Entity e) {
        e.setHealth(e.getHealth()-dmg);

        System.out.println("You attacked with " + name +" for " + dmg + " damage.");
    }
    */

}
