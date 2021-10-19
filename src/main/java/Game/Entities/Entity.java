package Game.Entities;

import Game.Entities.EnemyTypes.Enemy;
import Game.Items.Item;
import Game.Items.Weapons.Weapon;

import java.util.HashMap;

/***
 * parent class to all Entities
 * @author Harrison Brown
 * @version 0.1
 */

public abstract class Entity {
    //properties

    /**
     * if the entity is alive
     */
    protected boolean isAlive = true;

    /**
     * amount of gold the entity has
     */
    protected int gold;

    /**
     * current health of the entity
     */
    protected int health;

    /**
     * max health of the entity
     */
    protected int maxHealth;

    /**
     * current level of the entity
     */
    protected int level;

    /**
     * current inventory of the entity
     */
    protected Item[] Inventory;

    protected Weapon[] holding = new Weapon[2];

    //stats, will probably be changed later
    /**
     * defense stat
     */
    protected int def; //defense

    /**
     * speed stat
     */
    protected int spd; //speed

    /**
     * dexterity stat
     */
    protected int dex; //dexterity, accuracy, spryness

    /**
     * wisdom stat
     */
    protected int wis; //wisdom, spell casting etc.

    /**
     * strength stat
     */
    protected int str; //strength


    //constructor

    /**
     * Constructor for entity
     * <p>
     *     Sets the initial gold and health values for an entity
     *     current health will be set to match the maxHealth
     * </p>
     * @param gold initial gold amount
     * @param maxHealth initial max health
     */

    protected Entity(int gold, int maxHealth) {
        this.gold = gold;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.level = 1;

    }

    //abstract methods

    /**
     * Generic attack for all entities to implement
     */
    abstract protected void attack(Entity entity);

    /**
     * Generic move for all entities to implement
     */
    abstract protected void move();

    /**
     * Generic block for all entities to implement
     */
    abstract protected void block();

    /**
     * Generic useItem for all entities to implement
     */
    abstract protected void useItem();

    /**
     * if the entities' health is 0 or less, isAlive is set to false
     */
    public void checkHealthStatus() {
        if (health <= 0) {
            isAlive = false;
        }
    }

    //gets and sets

    public boolean getIsAlive() {
        return isAlive;
    }

    public int getGold() {
        return gold;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getLevel() {
        return level;
    }

    public Item[] getInventory() {
        return Inventory;
    }

    public int getDef() {
        return def;
    }

    public int getSpd() {
        return spd;
    }

    public int getDex() {
        return dex;
    }

    public int getWis() {
        return wis;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setInventory(Item[] inventory) {
        Inventory = inventory;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public void setWis(int wis) {
        this.wis = wis;
    }

    public abstract String getName();
}
