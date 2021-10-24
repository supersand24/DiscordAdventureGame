package Game.Items;

/***
 * parent class to all items and weapons
 * @author Harriosn Brown
 * @version 0.1
 */
public abstract class Item {

    /**
     * name of the item
     */
    protected String name;

    /**
     * cost of the item
     */
    protected int cost;

    /**
     * weight of the item
     */
    protected int weight;

    /**
     * sets the name, cost, and weight of an item
     * @author Harrison Brown
     * @param name item name
     * @param cost item cost
     * @param weight item weight
     */
    protected Item(String name, int cost, int weight) {
        this.name = name;
        this.cost = cost;
        this.weight = weight;
    }

    /**
     * generic constructor for items
     * @author Harrison Brown
     */
    protected Item() {
        this.name = "generic item";
        this.cost = 5;
        this.weight = 10;
    }

    //gets and sets



    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
