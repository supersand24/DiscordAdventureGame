package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.EnemyTypes.Grunts.Goblin;
import Game.Entities.Player;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * controls the encounter creation of the game
 * @author Harrison Brown
 * @version 0.1
 */
public class Encounters {

    /**
     * types of encounters
     */
    public enum EncounterType {
        BATTLE(5, true),
        SETTLEMENT(2, false),
        MERCHANT(1, false),
        DUNGEON(1, false),
        BRANCH_PATH(3, true),
        NONE(4, true),
        RETURN_TO_SETTLEMENT(10,false);

        int weight;
        boolean canRepeat;

        /**
         * constructor for EncounterType states
         * @author Harrison Brown
         * @param weight the likelihood the encounter type will happen
         * @param canRepeat if the EncounterType can be successively called
         */
        EncounterType(int weight, boolean canRepeat) {
            this.weight = weight;
            this.canRepeat = canRepeat;
        }

    }

    /**
     * enum for possible encounters in a dungeon
     */
    public enum dungeonEncounters {
        BATTLE,
        LOOT,
        BRANCH,
        DUMB_NPC,
        DEAD_ADVENTURER,
        NONE
    }

    /**
     * single method call to set the encounter type for the party and handle it
     * @author Harrison Brown
     * @param party party to put in encounters
     */
    public static void encounter(Party party) {
        setEncounter(party);
        generateEncounter(party);
    }
    /**
     * static object of Random for use in the class
     */
    final private static Random rand = new Random();

    /**
     * sets the next encounter for the party
     * @author Harrison Brown
     * @param p the party to set the encounter for
     */
    public static void setEncounter(Party p) {
        EncounterType en = null;
        ArrayList<EncounterType> types = new ArrayList<>();

        if (p.getCurrentEvent() == null || p.getCurrentEvent().canRepeat) {
            types.addAll(List.of(EncounterType.values()));
        } else {
            for (EncounterType type : EncounterType.values()) {
                if (!type.equals(p.getCurrentEvent())) {
                    types.add(type);
                }
            }
        }

        System.out.println(types);

        int max = types.size();
        while (en == null) {
            int sum = 0;
            for (int i = 0; i < max; i++) {
                sum += types.get(i).weight;
            }

            int x = rand.nextInt(sum);
            if (x > (sum - (types.get(max - 1).weight))) {
                en = types.get(max - 1);
            } else {
                if ((max-1) == 0) {
                    max = types.size();
                } else {
                    max--;
                }
            }
        }

        System.out.println(en);
        p.setCurrentEvent(en);
    }

    /**
     * method to generate the encounter from the currentEvent in the party
     * @author Harrison Brown
     */
    public static void generateEncounter(Party party) {
        switch (party.getCurrentEvent()) {
            case BATTLE -> battle(party);
            case SETTLEMENT -> settlement(party);
            case MERCHANT -> merchant(party);
            case DUNGEON -> dungeon(party);
            case BRANCH_PATH -> branchPath(party);
            case NONE -> none(party);
            case RETURN_TO_SETTLEMENT -> returnToSettlement(party);
        }
    }

    /**
     * creates battle encounter
     * @author Harrison Brown
     */
    private static void generateEnemies(Party party) {
       int avgLvl = averageLevel(party);
       int maxCnt = 24 - party.getPlayers().size();
       int cnt = rand.nextInt(1, 4);
       ArrayList<Enemy> badguys = new ArrayList<>();
       for (int i = 0; i <= cnt; i++) {
           badguys.add(new Goblin());
       }

       party.enemies.addAll(badguys);
    }

    /**
     * gets the average level of all players in a party
     * @author Harrison Brown
     * @param party a party
     * @return average level
     */
    public static int averageLevel(Party party) {
        int sum = 0;
        int avg;

        for (Player player : party.getPlayers()) {
            sum += player.getLevel();
        }
        avg = sum / party.getPlayers().size();

        return avg;
    }

    /**
     * method for dealing with battle event
     * @author Harrison Brown
     * @param party current party
     */
    private static void battle(Party party) {
        generateEnemies(party);
        BattleSystem.startBattle(party);
        party.sendBattleMessage();
    }

    /**
     * method for dealing with settlement event
     * @author Harrison Brown
     * @param party current party
     */
    private static void settlement(Party party) {
        party.getChannel().sendMessage("You found a settlement").queue();
    }

    /**
     * method for dealing with merchant event
     * @author Harrison Brown
     * @param party current party
     */
    private static void merchant(Party party) {
        party.getChannel().sendMessage("You found a merchant").queue();
    }

    /**
     * method for dealing with dungeon event
     * @author Harrison Brown
     * @param party current party
     */
    private static void dungeon(Party party) {
        party.getChannel().sendMessage("There would be a notice about the dungeon and some desicion structure here").queue();
    }

    /**
     * method for dealing with none event
     * @author Harrison Brown
     * @param party current party
     */
    private static void none(Party party) {
        int r = rand.nextInt(2);
        if (r == 0) {
            party.getChannel().sendMessage("Your party has been walking a while, do you want to rest?").queue();
        } else {
            encounter(party);
        }
    }

    private static void returnToSettlement(Party party) {
        TextChannel settlementChannel = party.getLocation().getChannel();
        if (settlementChannel != null) {
            for (Member member : party.getChannel().getMembers()) {
                settlementChannel.createPermissionOverride(member).setAllow(
                        Permission.VIEW_CHANNEL
                ).queue();
            }
            party.getChannel().sendMessage("You returned back to " + party.getLocation().getName() + ".").queue();
        }
    }

    /**
     * method for dealing with branching path event
     * @author Harrison Brown
     * @param party current party
     */
    private static void branchPath(Party party) {
        party.getChannel().sendMessage("Theres a branch in the path, do you go left or right?").queue();
    }


}