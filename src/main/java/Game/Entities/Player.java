package Game.Entities;

import Game.Items.Item;
import Game.Items.Useable.Usable;
import Game.Items.Weapons.Sword;
import Game.Items.Weapons.Weapon;
import Game.Party;
import net.dv8tion.jda.api.entities.Member;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Player class
 * @author Harrison Brown
 * @version 0.3
 */
public class Player extends Entity {

    /**
     * Gender of the character
     */
    protected String gender = "male";

    protected Party party;

    protected Member member;

    /**
     * player constructor
     * @author Harrison Brown
     * @param gold initial gold
     * @param maxHealth initial max health
     * @see Game.Entities.Entity
     */
    public Player(int gold, int maxHealth, String playerName, String gender) {
        super(gold, maxHealth);
        this.name = playerName;
        this.gender = gender;
        genStats();
        Sword s = new Sword();
        holding.add(s);

    }

    /**
     * a default constructor for quickly creating a default character
     * @author Harrison Brown
     * @param playerName the name of the player
     */
    public Player(String playerName, Member member) {
        super();
        this.name = playerName;
        this.maxHealth = 100;
        this.health = this.maxHealth;
        genStats();
        Sword s = new Sword();
        holding.add(s);
        this.member = member;
        System.out.println("New player created\n" + this);
    }

    /**
     * sets the stats of the player
     * @author Harrison Brown
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
     * @author Harrison Brown
     * @param ar the array to shuffle
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
     * @author Harrison Brown
     * @param ar the array of stats to check
     */
    private void checkMax(int[] ar) {
        for (int i = 0; i < ar.length; i++) {
            if (ar[i] > 15) {
                ar[i] = 15;
            }
        }
    }

    public Party getParty() {
        return party;
    }

    public Member getMember() {
        return member;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * a method to add a weapon to the players equips
     * @author Harrison Brown
     * @param weapon the weapon to add
     */
    public void addWeapon(Weapon weapon) {
        holding.add(weapon);
    }

    /**
     * a method to switch the active weapon the secondary one
     * @author Harrison Brown
     */
    public void switchActiveWeapon() {
        Weapon temp = holding.get(0);
        holding.remove(0);
        holding.add(temp);
    }

    /**
     * override for attack method
     * @author Harrison Brown
     * <p>
     *     checks if the target is blocking and acts accordingly
     *     if the target dies the equipped weapons kill count is incremented
     * </p>
     * @param entity the entity to target
     */
    @Override
    public void attack(Entity entity) {
        if (entity.isBlocking()) {
            this.setLastAction(entity.ifBlock());
        } else {
            System.out.println(this.getName() + " Attacked");
            entity.setHealth(entity.getHealth() - holding.get(0).getDmg());
            entity.checkHealth();
            if (!entity.getIsAlive()) {
                holding.get(0).upKillCnt();
            }
            this.setLastAction("Attacked " + entity.getName() + " for " + holding.get(0).getDmg() + " damage");
        }
    }

    /**
     * override for the move method
     * @author Harrison Brown
     */
    @Override
    public void move() {
    }

    /**
     * override for the block method
     * @author Harrison Brown
     */
    @Override
    public void block() {
        String msg;
        if (this.isBlocking()) {
            msg = "You already braced for an attack";
        } else {
            msg = "braced for an attack";
            this.switchBlock();
        }
        System.out.println(msg);
        this.setLastAction(msg);
    }

    /**
     * override for the useItem method
     * @author Harrison Brown
     */
    @Override
    public void useItem(Usable item) {
        item.use(this);
    }

    public String saveString() {
        return "ID:" + getMember().getId() + "\n" +
                "Player Name:" + getName() + "\n" +
                "Level:" + getLevel() + "\n" +
                "Gold:" + getGold() + "\n" +
                "Health:" + getHealth() + "\n" +
                "Max Health:" + getMaxHealth() + "\n" +
                "Defense:" + getDef() + "\n" +
                "Speed:" + getSpd() + "\n" +
                "Dexterity:" + getDex() + "\n" +
                "Wisdom:" + getWis() + "\n" +
                "Strength:" + getStr();
    }

    /**
     * toString for player
     * @author Harrison Brown
     * <p>
     *    gives a string that is a vertical representation of all player attributes
     * </p>
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Character: ");
        out.append(name);
        out.append("\n");
        out.append("Gender: ");
        out.append(gender);
        out.append("\n");
        out.append("Level: ").append(level).append("\n");
        out.append("Health: ").append(health).append(" Max Health: ").append(maxHealth).append("\n");
        out.append("Gold: ").append(gold).append("\n");

        for (Item x : holding) {
            out.append(x.getClass().getSimpleName()).append(": ").append(x.getName()).append("\n");
        }
        out.append("Def: ").append(def).append("\n").append("Spd: ").append(spd).append("\n").append("Dex: ").append(dex).append("\n").append("Wis: ").append(wis).append("\n").append("Str: ").append(str).append("\n");

        return out.toString();
    }
}
