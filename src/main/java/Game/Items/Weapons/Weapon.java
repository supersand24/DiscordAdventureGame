package Game.Items.Weapons;

import Game.Items.Item;

/***
 * generic weapon class
 * @author Harrison Brown
 * @version 0.1
 */
public abstract class Weapon extends Item {
    /**
     * actions specific to each weapon type
     */
    protected String[] moves = null;
    /***
     * damage value of weapon
     */
    protected int dmg;

    /**
     * original owner of the weapon
     */
    protected final String ogOwner;

    /**
     * how many entities the weapon has killed
     */
    protected int killCnt;

    /**
     * if the weapon was found in a dungeon or from a boss the player's name would go here
     */
    protected String foundBy;

    /***
     * sets the name, cost, weight, and damage values of the weapon
     * @author Harrison Brown
     * @param name weapon name
     * @param cost weapon cost
     * @param weight weapon weight
     * @param dmg weapon damage
     * @param ogOwner original owner of weapon
     * @see Game.Items.Item
     */
    Weapon(String name, int cost, int weight, int dmg, String ogOwner) {
        super(name, cost, weight);
        this.dmg = dmg;
        this.ogOwner = ogOwner;
    }

    /**
     * weapon constructor with predefined
     * @author Harrison Brown
     * @param name weapon name
     * @param cost weapon cost
     * @param weight weapon weight
     * @param dmg weapon damage
     */
    Weapon(String name, int cost, int weight, int dmg) {

        super(name, cost, weight);
        this.dmg = dmg;
        this.ogOwner = "Harrison"; //it is me by default cause im awesome
    }

    /**
     * getter for weapon damage
     * @author Harrison Brown
     * @return int damage
     */
    public int getDmg() {
        return dmg;
    }

    /**
     * getter for the weapons original owner
     * @author Harrison Brown
     * @return String
     */
    public String getOgOwner() {
        return ogOwner;
    }

    /**
     * getter for the weapons kill count
     * @author Harrison Brown
     * @return int
     */
    public int getKillCnt() {
        return killCnt;
    }

    /**
     * returns the adventurer that found the weapon
     * @author Harrison Brown
     * @return string player name
     */
    public String getFoundBy() {
        return foundBy;
    }

    /**
     * raises the weapons kill count by one
     * @author Harrison Brown
     */
    public void upKillCnt() {
        this.killCnt++;
    }

    /**
     * set the adventurer that found the weapon
     * @author Harrison Brown
     * @param foundBy string of the adventurers name
     */
    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }


}
