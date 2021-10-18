package Game.Items.Weapons;

import Game.Items.Item;

/***
 * generic weapon class
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Weapon extends Item {
    /***
     * damage value of weapon
     */
    protected int dmg;

    /***
     * sets the name, cost, weight, and damage of the weapon
     * @param name weapon name
     * @param cost weapon cost
     * @param weight weapon weight
     * @param dmg weapon damage
     */
    Weapon(String name, int cost, int weight, int dmg) {
        super(name, cost, weight);
        this.dmg = dmg;
    }

}
