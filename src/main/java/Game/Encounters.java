package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.EnemyTypes.Grunts.Goblin;
import Game.Entities.Player;

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
        NONE(4, true);

        int weight;
        boolean canRepeat;

        EncounterType(int weight, boolean canRepeat) {
            this.weight = weight;
            this.canRepeat = canRepeat;
        }

    }

    public enum dungeonEncounters {
        BATTLE,
        LOOT,
        BRANCH,
        DUMBNPC,
        NONE
    }

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

        if (p.getCurrentEncounter() == null || p.getCurrentEncounter().canRepeat) {
            types.addAll(List.of(EncounterType.values()));
        } else {
            for (EncounterType type : EncounterType.values()) {
                if (!type.equals(p.getCurrentEncounter())) {
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
        p.setCurrentEncounter(en);
    }

    /**
     * method to generate the encounter from the currentEvent in the party
     * @author Harrison Brown
     */
    public static void generateEncounter(Party party) {

        switch (party.getCurrentEncounter()) {
            case BATTLE -> battle(party);
            case SETTLEMENT -> settlement(party);
            case MERCHANT -> merchant(party);
            case DUNGEON -> dungeon(party);
            case BRANCH_PATH -> branchPath(party);
            case NONE -> none(party);
        }
    }

    /**
     * creates battle encounter
     * @author Harrison Brown
     */
    private static void generateEnemies(Party party) {
       int avgLvl = averageLevel(party);
       int maxCnt = 25 - party.getPlayers(Game.guild).size();
       int cnt = rand.nextInt(1, maxCnt);
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

        for (Player player : party.getPlayers(Game.guild)) {
            sum += player.getLevel();
        }
        avg = sum / party.getPlayers(Game.guild).size();

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
        Game.guild.getTextChannelById(party.channelId).sendMessage("You found a settlement").queue();
    }

    /**
     * method for dealing with merchant event
     * @author Harrison Brown
     * @param party current party
     */
    private static void merchant(Party party) {
        Game.guild.getTextChannelById(party.channelId).sendMessage("You found a merchant").queue();
    }

    /**
     * method for dealing with dungeon event
     * @author Harrison Brown
     * @param party current party
     */
    private static void dungeon(Party party) {
        Game.guild.getTextChannelById(party.channelId).sendMessage("There would be a notice about the dungeon and some desicion structure here").queue();
    }

    /**
     * method for dealing with none event
     * @author Harrison Brown
     * @param party current party
     */
    private static void none(Party party) {
        int r = rand.nextInt(1);
        if (r == 0) {
            Game.guild.getTextChannelById(party.channelId).sendMessage("Your party has been walking a while, do you want to rest?").queue();
        } else {
            encounter(party);
        }
    }

    /**
     * method for dealing with branching path event
     * @author Harrison Brown
     * @param party current party
     */
    private static void branchPath(Party party) {
        Game.guild.getTextChannelById(party.channelId).sendMessage("Theres a branch in the path, do you go left or right?").queue();
    }


}