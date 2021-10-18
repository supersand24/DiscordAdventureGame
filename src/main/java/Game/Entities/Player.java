package Game.Entities;

import Game.Items.Item;
import Game.Items.Weapons.Sword;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Player class
 * @author Harrison Brown
 * @version 0.2
 */
public class Player extends Entity {
    /**
     * The name of the character
     */
    protected final String name;

    /**
     * Gender of the character
     */

    protected String gender;

    /**
     * player constructor
     * @param gold initial gold
     * @param maxHealth initial max health
     * @see Game.Entities.Entity
     */
    public Player(int gold, int maxHealth, String weaponName, String weaponOwner, String playerName, String gender) {
        super(gold, maxHealth);
        holding[0] = new Sword(weaponName, weaponOwner);
        this.name = playerName;
        this.gender = gender;
        genStats();

    }

    /**
     * sets the stats of the player
     * @version 0.4
     */
    private void genStats() {
        Random rand = new Random();
        int pool = 35;
        int[] stats = new int[5];

        int cnt = 4;

        //sets the lower bound of the rand
        int min = 1;
        //sets the initial max
        int max = 15;

        while (pool > 0) {

            for (int i = 0; i < stats.length; i++) {
                if (pool < 0) {
                    break;
                }
                /*
                if (pool <= 3) {
                    min = 0;
                }
                */
                if (stats[i] >= 15) {
                    i++;
                    continue;
                }

                if (pool < 15) {
                    max = pool - cnt;
                }

                int add = rand.nextInt(min, max);
                stats[i] += add;
                pool -= add;
                pool += 1;
                cnt--;
            }
        }

        shuffleArray(stats);
        checkMax(stats);

        this.def = stats[0];
        this.spd = stats[1];
        this.dex = stats[2];
        this.wis = stats[3];
        this.str = stats[4];

    }

    /**
     * a method for a Fisher-Yates shuffle
     * @param ar the array to shuffle
     * @version 1.0
     */
    private void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /***
     * if the value of the sats is greater than 15, sets then to 15
     * @param ar the array of stats to check
     * @version 1.0
     */
    private void checkMax(int[] ar) {
        for (int i = 0; i < ar.length; i++) {
            if (ar[i] > 15) {
                ar[i] = 15;
            }
        }
    }

    @Override
    public void attack(Entity entity) {
        entity.setHealth(entity.getHealth()-holding[0].getDmg());
    }

    @Override
    public void move() {
    }

    @Override
    public void block() {
    }

    @Override
    public void useItem() {
    }

    /**
     * toString for player
     * <p>
     *    gives a string that is a vertical representation of all player attributes
     * </p>
     * @return String
     */
    @Override
    public String toString() {
        String out = "";
        out += "Character: " + name + "\n";
        out += "Gender: " + gender + "\n";
        out += "Level: " + level + "\n";
        out += "Health: " + health + " Max Health: " + maxHealth + "\n";
        out += "Gold: " + gold + "\n";
        for (Item x : holding) {
            if (x != null) {
                out += x.getClass() + ": " + x.getName() + "\n";
            }
        }
        out += "Def: " + def +"\n";
        out += "Spd: " + spd +"\n";
        out += "Dex: " + dex +"\n";
        out += "Wis: " + wis +"\n";
        out += "Str: " + str +"\n";

        return out;
    }
}
