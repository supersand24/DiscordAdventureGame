package Game.Entities;

import Game.Items.Item;
import Game.Items.Useable.Usable;
import Game.Items.Weapons.Weapon;

import java.util.ArrayList;

/***
 * parent class to all Entities
 * @author Harrison Brown
 * @version 0.1
 */

public abstract class Entity implements Comparable<Entity> {
    //properties

    /**
     * last action of the entity
     */
    protected String lastAction;

    /**
     * name
     */
    protected String name;

    /**
     * holds if the entity is blocking or not
     */
    private boolean block;

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
    protected ArrayList<Item> Inventory = new ArrayList<>();

    /**
     * arraylist of all the weapons the player is currently holding
     */
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
     * @author Harrison Brown
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

    /**
     * default constructor for Entity
     * @author Harrison Brown
     */
    protected Entity() {
        this.level = 1;
    }

    protected Entity(int lvl) {
        this.level = lvl;
    }

    //abstract methods

    /**
     * Generic attack for all entities to implement
     * @author Harrison Brown
     */
    abstract public void attack(Entity entity);

    /**
     * Generic move for all entities to implement
     * @author Harrison Brown
     */
    abstract public void move();

    /**
     * Generic block for all entities to implement
     * @author Harrison Brown
     */
    abstract public void block();

    /**
     * Generic useItem for all entities to implement
     * @author Harrison Brown
     */
    abstract public void useItem(Usable item);

    /**
     * if the entities' health is 0 or less, isAlive is set to false
     * @author Harrison Brown
     */
    public void checkHealth() {
        if (health <= 0) {
            isAlive = false;
        }
    }

    //gets and sets

    /**
     * get for entity name
     * @author Harrison Brown
     * @return string
     */
    public String getName() {
        return name;
    }

    /**
     * getter for if the entity is alive
     * @author Harrison Brown
     * @return boolean
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * returns current gold entity has
     * @author Harrison Brown
     * @return int
     */
    public int getGold() {
        return gold;
    }

    /**
     * getter for the entity's current health
     * @author Harrison Brown
     * @return int
     */
    public int getHealth() {
        return health;
    }

    /**
     * getter for the entity's max health
     * @author Harrison Brown
     * @return int
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * getter for the entity's current level
     * @author Harrison Brown
     * @return int
     */
    public int getLevel() {
        return level;
    }

    /**
     * returns entities current inventory
     * @author Harrison Brown
     * @return Item[]
     */
    public ArrayList<Item> getInventory() {
        return Inventory;
    }

    /**
     * a getter for the entities def stat
     * @author Harrison Brown
     * @return int
     */
    public int getDef() {
        return def;
    }

    /**
     * getter for entity's speed stat
     * @author Harrison Brown
     * @return int
     */
    public int getSpd() {
        return spd;
    }

    /**
     * getter for entities dex
     * @author Harrison Brown
     * @return int
     */
    public int getDex() {
        return dex;
    }

    /**
     * getter for entities wis
     * @author Harrison Brown
     * @return int
     */
    public int getWis() {
        return wis;
    }

    public int getStr() {
        return str;
    }

    /**
     * setter for entity's name
     * @author Harrison Brown
     * @param name string to set name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for entity's current gold
     * @author Harrison Brown
     * @param gold amt to set gold to
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * setter for entity's current health
     * @author Harrison Brown
     * @param health val to set health to
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * setter for entity's max health
     * @author Harrison Brown
     * @param maxHealth val to set max health to
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * setter for entity's level
     * @author Harrison Brown
     * @param level val to set current level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * setter for entity's inventory
     * @author Harrison Brown
     * @param inventory array to replace current inventory
     */
    public void setInventory(ArrayList<Item> inventory) {
        Inventory = inventory;
    }

    /**
     * setter for entity's def stat
     * @author Harrison Brown
     * @param def val to set def
     */
    public void setDef(int def) {
        this.def = def;
    }

    /**
     * setter for entity's speed stat
     * @author Harrison Brown
     * @param spd val to set def
     */
    public void setSpd(int spd) {
        this.spd = spd;
    }

    /**
     * setter for entity's dex stat
     * @author Harrison Brown
     * @param dex val to set dex
     */
    public void setDex(int dex) {
        this.dex = dex;
    }

    /**
     * setter for entity's wis stat
     * @author Harrison Brown
     * @param wis val to set wis
     */
    public void setWis(int wis) {
        this.wis = wis;
    }

    public void setStr(int str) {
        this.str = str;
    }

    /**
     * checks the current state of blocking
     * @author Harrison Brown
     * @return boolean
     */
    public boolean isBlocking() {
        return block;
    }

    /**
     * switches the current val of blocking
     * @author Harrison Brown
     */
    public void switchBlock() {
        block = !block;
    }

    /**
     * checks if entity is blocking, swaps the bool, and returns a string
     * @return string
     */
    public String ifBlock() {
        String msg = "Attacked " + this.getName() + ", it was blocked";
            System.out.println(msg);
            this.switchBlock();
            return (msg);
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public void addToInventory(Item item) {
        Inventory.add(item);
    }


    /**
     * sorts based on speed, Arrays.sort(Entity[] array)
     * @author Harrison Brown
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

            String fruitName1 = fruit1.getEntityName().toUpperCase();
            String fruitName2 = fruit2.getEntityName().toUpperCase();

            //ascending order
            return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
    */


}
