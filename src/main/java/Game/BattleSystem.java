package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;
import net.dv8tion.jda.api.entities.Member;

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
                makeChoice(actions.ATTACK, e, players[0]);
                if (!players[0].getIsAlive()) {
                    //System.out.println(players[0].getName() + " You died");
                }
            } else {
                int p;
                do {
                    p = randomVal(0, players.length - 1);
                } while(!players[p].getIsAlive());

                makeChoice(actions.ATTACK, e, players[p]);
                if (!players[p].getIsAlive()) {
                    //System.out.println(players[p].getName() + "You died");
                }
            }
            //System.out.println();

        } else {
            makeChoice(actions.BLOCK, e);
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
    public static void makeTurnOrder(Party p, Entity... e) {
        Entity[] arr1;
        Entity[] arr2;
        Entity[] order;

        if (e != null) {
            arr1 = p.getTurnOrder();
            arr2 = e;
        } else {
            arr1 = p.getPlayers(Game.guild).toArray(new Entity[0]);
            arr2 = p.enemies.toArray(new Entity[0]);
        }

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
     * sets the decisions for enemies and players
     * @author Harrison Brown
     * @param choice a string representation of the choice the entity wants to make
     * @param e a nullable array of entities, the first one is the calling entity, the second should be a target if applicable to the action
     */
    public static void makeChoice(BattleSystem.actions choice, Entity... e) {
        //e[0] is player
        //e[1] is target
        switch (choice) {
            case ATTACK -> {
                e[0].attack(e[1]);
                e[1].checkHealth();
            }
            case BLOCK -> e[0].block();
            case CHECK_HEALTH -> System.out.println("Your health is " + e[0].getHealth() + "/" + e[0].getMaxHealth());
            case TURN_ORDER -> {
                System.out.println("The turn order is:");
                for (Entity entity : e) {
                    if (entity.getIsAlive()) {
                        System.out.println(entity.getName());
                    }
                }
            }
            //System.out.println();
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
