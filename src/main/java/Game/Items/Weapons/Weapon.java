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
     * sets the name, cost, weight, and damage of the weapon
     * @param name weapon name
     * @param cost weapon cost
     * @param weight weapon weight
     * @param dmg weapon damage
     * @see Game.Items.Item
     */
    Weapon(String name, int cost, int weight, int dmg) {
        super(name, cost, weight);
        this.dmg = dmg;
        this.ogOwner = "Harrison"; //it is me by default cause im awesome
    }

    Weapon(String name, int cost, int weight, int dmg, String ogOwner) {
        super(name, cost, weight);
        this.dmg = dmg;
        this.ogOwner = ogOwner;
    }

    public int getDmg() {
        return dmg;
    }

    public String getOgOwner() {
        return ogOwner;
    }

    public int getKillCnt() {
        return killCnt;
    }

    public String getFoundBy() {
        return foundBy;
    }


    public void setKillCnt(int killCnt) {
        this.killCnt = killCnt;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }


}
