package Game.Entities;

import Game.Items.Item;
import Game.Items.Weapons.Sword;

import java.util.Random;

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
        def = genStat();
        spd = genStat();
        dex = genStat();
        wis = genStat();
        str = genStat();
        level = 1;

    }

    /**
     * randomly generate an int for the stats
     * @return returns a random value between 0 and 20
     */
    public int genStat() {
        Random rand = new Random();
        return rand.nextInt(1,16);
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
