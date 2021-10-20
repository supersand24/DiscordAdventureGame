package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.*;

/**
 * All Battle System methods for the game (includes some static utilities as well)
 * @author Harrison Brown
 * @version 0.6
 */
public class BattleSystem {

    /**
     * enum for the actions that can be taken in a turn
     */
    public enum actions {
        ATTACK,
        BLOCK,
        CHECK_HEALTH,
        TURN_ORDER
    }

    /**
     * list of all parties engaged in battle
     */
    private static List<Party> activeBattles = new ArrayList<>();

    /**
     * starts battle
     * @author Harriosn Brown
     */
    public static void startBattle(Party p) {
        //adds the party to the list pof active battles
        activeBattles.add(p);

        //temp array of players
        Player[] players = p.getPlayers(Game.guild).toArray(new Player[0]);

        //sets the turn order in the party
        makeTurnOrder(p);

        //iterate through turn order if enemy do turn, if player set party.turn = first player in turn order list
        processTurn(p,0);
    }

    /**
     * handles the current set of turns
     * @author Harrison Brown
     * @param p a party
     * @param cnt the starting index in the turn order
     */
    public static void processTurn(Party p, int cnt) {

        for (int i = cnt; i < p.getTurnOrder().length; i++) {
            if (p.getTurnOrder()[i] instanceof Enemy && p.getTurnOrder()[i].getIsAlive()) {
                enemyTurn(p, (Enemy)p.getTurnOrder()[i]);
            }

            if (p.getTurnOrder()[i] instanceof Player) {
                p.setTurnIndex(i);
                break;
            }

        }
    }

    /**
     * computer takes turn
     * @author Harrison Brown
     */
    private static void enemyTurn(Party party, Enemy e) {
        Player[] players = party.getPlayers(Game.guild).toArray(new Player[0]);
        int act;
        act = randomVal(0, 1);
        //act = 1;
        if (act == 0) {
            if (players.length == 1) {
                attack(e, players[0]);
                if (!players[0].getIsAlive()) {
                    //System.out.println(players[0].getName() + " You died");
                }
            } else {
                int p;
                do {
                    p = randomVal(0, players.length - 1);
                } while(!players[p].getIsAlive());

                attack(e, players[p]);
                if (!players[p].getIsAlive()) {
                    //System.out.println(players[p].getName() + "You died");
                }
            }
            //System.out.println();

        } else {
            block(e);
            //System.out.println();
        }
    }

    /**
     * checks if the given member's turn is now
     * @author Harrison Brown
     * @param p the party the member is from
     * @param m the member to know if it's their turn
     * @return boolean
     */
    public static boolean isTurn(Party p, Member m) {
        if (p.getTurnOrder() != null) {
            return p.getTurnOrder()[p.getTurnIndex()].equals(p.getPlayer(m));
        }
        return false;
    }

    /**
     * sets the turnOrder array in the party class based on the speed of the entities in the encounter
     * @author Harrison Brown
     */
    private static void makeTurnOrder(Party p) {
        Entity[] arr1;
        Entity[] arr2;
        Entity[] order;

        arr1 = p.getPlayers(Game.guild).toArray(new Entity[0]);
        arr2 = p.enemies.toArray(new Entity[0]);

        order = new Entity[arr1.length + arr2.length];

        int i = 0;

        for (Entity entity : arr1) {
                order[i] = entity;
                i++;
        }

        for (Entity entity : arr2) {
            order[i] = entity;
            i++;
        }

        Arrays.sort(order);
        p.setTurnOrder(order);

    }

    /**
     * Converts decision from discord to action.
     *
     * @author Justin Sandman
     * @param choice a string representation of the choice the entity wants to make
     * @param slashCommand the command info from discord, can get player, party, and member
     */
    public static void makeChoice(BattleSystem.actions choice, SlashCommandEvent slashCommand) {

        Member member = slashCommand.getMember();

        Party party = null;
        for (Party p : activeBattles) {
            if (p.getMembers(Game.guild).contains(member)) {
                party = p;
            }
        }

        if (party != null && member != null) {

            if (isTurn(party, member)) {

                slashCommand.deferReply(true).queue();

                Player player = party.getPlayer(member);

                switch (choice) {
                    case ATTACK -> attack(
                            player,
                            party.enemies.get(
                                    (int) slashCommand.getOption("target").getAsLong() - 1
                            ));
                    case BLOCK -> block(player);
                    case CHECK_HEALTH -> checkHealth(player);
                    case TURN_ORDER -> turnOrder(party.getTurnOrder());
                }
                
                slashCommand
                        .getHook()
                        .sendMessage(player.getLastAction())
                        .queue();

                processTurn(party, party.getTurnIndex() + 1);

                party.updateBattle();

            } else {
                slashCommand
                        .getHook()
                        .sendMessage("It's not your turn.")
                        .queue();
            }
        }
    }

    public static void attack(Entity self, Entity target) {
        self.attack(target);
        target.checkHealth();
    }

    public static void block(Entity self) {
        self.block();
    }

    //PROB NOT NEEDED, SINCE HEALTH WILL BE ON SCREEN
    //CAN BE REPLACED WITH JUST CHECK SELF COMMAND
    //DOES NOT NEED TO BE A PART OF BATTLE SYSTEM
    public static void checkHealth(Entity self) {
        System.out.println("Your health is " + self.getHealth() + "/" + self.getMaxHealth());
    }

    public static void turnOrder(Entity[] entities) {
        System.out.println("The turn order is:");
        for (Entity entity : entities) {
            if (entity.getIsAlive()) {
                System.out.println(entity.getName());
            }
        }
    }


    /*
    /**
     * @author Harrison Brown
     * @param e an array of enemies
     * @return returns true if an entity in the array is alive

    public static boolean entitiesLive (Entity[] e) {
        int alive = e.length;
        for (int i = 0; i < e.length; i++) {
            if (!e[i].getIsAlive()) {
                alive -= 1;
            }
        }
        if (alive == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * method to control the flow of combat
     * @param players array of players
     * @param enemies array of enemies

    public static void activeCombat(Player[] players, Enemy[] enemies) {

        int act;

        while (entitiesLive(enemies) && entitiesLive(players)) {
            for (Entity e : order) {
                if (!entitiesLive(enemies)) {
                    break;
                }
                if (e.isBlocking()) {
                    System.out.println(e.getName() + " got tired from bracing for an attack that never came");
                    e.switchBlock();
                    System.out.println();
                }
                if (e instanceof Enemy && e.getIsAlive()) {
                    act = randomVal(0, 1);
                    //act = 1;
                    if (act == 0) {
                        if (players.length == 1) {
                            makeChoice("attack", e, players[0]);
                            if (!players[0].getIsAlive()) {
                                System.out.println(players[0].getName() + " You died");
                                if (!entitiesLive(players)) {
                                    break;
                                }
                            }
                        } else {
                            int p = randomVal(0, players.length - 1);
                            makeChoice("attack", e, players[p]);
                            if (!players[p].getIsAlive()) {
                                System.out.println(players[p].getName() + "You died");
                            }
                        }
                        System.out.println();

                    } else {
                        makeChoice("block", e);
                        System.out.println();
                    }
                } else if (e instanceof Player && e.getIsAlive()) {
                    System.out.println(e.getName() + ", its your turn!");
                    do {
                        System.out.print("Do you want to attack, block, check your health, or check turn order? (0 - 3): ");
                        act = scan.nextInt();
                        scan.reset();
                        if (act == 0) {
                            System.out.println("This is the current health of the enemies:");
                            for (Enemy x : enemies) {
                                System.out.print(x.getHealth() + " | ");
                            }
                            System.out.println();
                            System.out.println("Which do you want to attack? (0 - (enemy count-1)");
                            int choice = scan.nextInt();
                            scan.reset();
                            makeChoice("attack", e, enemies[choice]);
                        } else if (act == 1) {
                            makeChoice("block", e);
                        } else if (act == 2) {
                            makeChoice("checkHealth", e);
                        } else if (act == 3) {
                            makeChoice("turnOrder", order);
                        }
                    } while (act > 1);
                    System.out.println();

                }
            }
        }

        if (!entitiesLive(enemies)) {
            System.out.println("Players win!!!");
        } else {
            System.out.println("Players lose!!!");
        }
    }

*/


    /**
     * utility to return a random value
     * @author Harrison Brown
     * @param min minimum bound
     * @param max maximum bound
     * @return the generated random value
     */
    public static int randomVal(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }
}
