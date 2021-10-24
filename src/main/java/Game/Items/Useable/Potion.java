package Game.Items.Useable;

import Game.Entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * class of all potion/liquid based items
 * @author Harrison Brown
 * @version 0.3
 */
public class Potion extends Usable {

    /**
     * enum that contains the fundamental states of the class
     */
     public enum Liquid {
        WATER("Water", 0, 0, null, 0, Status.DYSENTERY, Cleanliness.DIRTY),
        HP("Healing potion", 10, 0, null, 0, null, Cleanliness.CLEAN),
        STAT("Stat up", 0, 3, modifiedStats.values(), 0, null, Cleanliness.OK),
        LVL("Level up potion", 0, 0, null, 0, null, Cleanliness.OK);

        /**
         * constructor for enum states
         */
        private String name; private int heal, raiseStat, lvlUp; private ArrayList<modifiedStats> stats = new ArrayList<>(5); private Status status; private Cleanliness howClean;
         Liquid(String name, int heal, int raiseStat, modifiedStats[] stats, int lvlUp, Status status, Cleanliness howClean) {
            this.name = name;
            this.heal = heal;
            this.raiseStat = raiseStat;
            this.stats.addAll(List.of(stats));
            this.lvlUp = lvlUp;
            this.status = status;
            this.howClean = howClean;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHeal() {
            return heal;
        }

        public void setHeal(int heal) {
            this.heal = heal;
        }

        public int getLvlUp() {
            return lvlUp;
        }

        public void setLvlUp(int lvlUp) {
            this.lvlUp = lvlUp;
        }

        public int getRaiseStat() {
            return raiseStat;
        }

        public void setRaiseStat(int raiseStat) {
            this.raiseStat = raiseStat;
        }

        public ArrayList<modifiedStats> getStats() {
            return stats;
        }

        public void setStats(modifiedStats[] stats) {
            this.stats.addAll(List.of(stats));
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Cleanliness getHowClean() {
            return howClean;
        }

        public void setHowClean(Cleanliness howClean) {
            this.howClean = howClean;
        }
    }

    /**
     * enum of the stats that can be modified by potions
     */
    public enum modifiedStats {
        DEF,
        STR,
        DEX,
        SPD,
        WIS,
    }

    /**
     * enum for possible status potions can have
     */
    public enum Status {
        POISON,
        PAIN,
        HERNIA,
        DYSENTERY,
        PARALYZE,
        SLEEPY,
        HORNY,
        STOP
    }

    /**
     * enum for how clean potions could be
     */
    public enum Cleanliness {
        KRISP,
        CLEAN,
        OK,
        DIRTY
    }


    /**
     * variable to store current state of potion
     */
    private final Liquid liquid;

    /**
     * default constructor for potions, defaults to a healing potion
     * @author Harrison Brown
     */
    public Potion() {
        super(Liquid.HP.getName(), 2, 10);
        liquid = Liquid.HP;
        liquid.setHeal(15);
    }

    /**
     * constructor that takes a default Potion.Liquid state and uses it for the potion
     * @author Harrison Brown
     * @param liquid the liquid state to use
     */
    public Potion(Potion.Liquid liquid) {
        super (liquid.name, 2, 10);
        this.liquid = liquid;
    }

    /**
     * the big kahuna of potion constructors, it sets all values of the potion for a fully customizable experience
     * @author Harrison Brown
     * @param liquid (Potion.Liquid,State) the base state to use
     * @param name (string) the name for the state and the potion, assigned to the state as well as given to the super constructor
     * @param heal (int) the value to use for the how much the potion can heal for
     * @param raiseStat (int) the amount to raise stats by
     * @param lvlUp (int) the amount of levels to rasie the players level by
     * @param stats (array of modifiedStats) the stats for the potion to raise
     * @param status (Potion.Status) the staus for the potion to apply
     * @param howClean (Potion.Cleanliness) how clean the potion is
     * @param cost (int) the cost of the potion, how much it can be bought or sold for
     * @param weight (int) how heavy the potion is
     * @param uses (int) how times the potion can be used before its all gone
     */
    public Potion(Potion.Liquid liquid, String name, int heal, int raiseStat, int lvlUp, modifiedStats[] stats, Potion.Status status, Potion.Cleanliness howClean, int cost, int weight, int uses) {
        super(name, cost, weight);
        this.uses = uses;
        this.liquid = liquid;
        liquid.setName(name);
        liquid.setHeal(heal);
        liquid.setRaiseStat(raiseStat);
        liquid.setLvlUp(lvlUp);
        liquid.setStats(stats);
        liquid.setStatus(status);
        liquid.setHowClean(howClean);
    }


    /**
     * the use method for potions, applies all affects the potion has
     * @author Harrison Brown
     * @param e the entity that used the potion
     */
    @Override
    public void use(Entity e) {
        e.setHealth(e.getHealth() + liquid.heal);
        e.setLevel(e.getLevel() + liquid.lvlUp);
        if (liquid.getStats().contains(modifiedStats.DEF)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.WIS)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.DEX)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.SPD)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.STR)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        uses--;
    }


    /**
     * a test use for potions, doesn't need any parameters
     * @author Harrison Brown
     */
    @Override
    public void use() {
        System.out.println("used potion " + liquid.name);
        uses--;
    }

    /**
     * returns the current state of the potion
     * @author Harrison Brown
     * @return Potion.liquid.State
     */
    public Liquid getLiquid() {
        return liquid;
    }
}
