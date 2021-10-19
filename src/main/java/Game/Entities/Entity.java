package Game.Entities;

import Game.Entities.EnemyTypes.Enemy;
import Game.Items.Item;
import Game.Items.Weapons.Weapon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/***
 * parent class to all Entities
 * @author Harrison Brown
 * @version 0.1
 */

public abstract class Entity implements Comparable<Entity> {
    //properties

    /**
     * name
     */
    protected String name;

    /**
     * if the entity is alive
     */
    protected boolean isAlive = true;

    /**
     * true if it is the entities turn
     */
    protected boolean myTurn = false;
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

    protected ArrayList<Weapon> holding = new ArrayList<>(2);

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

    protected Entity() {
        this.gold = 10;
        this.maxHealth = 50;
        this.health = maxHealth;
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


    public String getName() {
        return name;
    }

    public boolean getMyTurn() {return myTurn; }

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

    public void setName(String name) {
        this.name = name;
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

    /**
     * sorts based on speed, Arrays.sort(Entity[] array)
     * @param compareEntity the entity to compare
     * @return return value
     */
    @Override
    public int compareTo(Entity compareEntity) {

        int compareSpd = (compareEntity).getSpd();

        //ascending order
        //return this.quantity - compareQuantity;

        //descending order
        return compareSpd - this.spd;

    }

    //a  method skeleton base static methods on to compare entities based on different properties
    /*
    public static Comparator<Entity> EntityNameComparator
            = new Comparator<Entity>() {

        public int compare(Entity fruit1, Entity fruit2) {

            String fruitName1 = fruit1.getEnityName().toUpperCase();
            String fruitName2 = fruit2.getEntityName().toUpperCase();

            //ascending order
            return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
    */


}
