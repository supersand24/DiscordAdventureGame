package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;

import java.util.*;

/**
 * All Battle System methods for the game (includes some static utilities as well)
 * @author Harrison Brown
 * @version 0.2
 */
public class BattleSystem {

    /**
     * returns an entity array sorted based on speed to be used as the turn order in battle
     */
    public static Entity[] makeTurnOrder(Entity[] arr1, Entity[] arr2) {
        Entity[] order = new Entity[arr1.length + arr2.length];

        int i = 0;

        for (Entity e : arr1) {
                order[i] = e;
                i++;
        }

        for (Entity p : arr2) {
            order[i] = p;
            i++;
        }

        Arrays.sort(order);

        return order;
    }

    /**
     * sets the decisions for enemies and players
     * @param choice a string representation of the choice the entity wants to make
     * @param e a nullable array of entities, the first one is the calling entity, the second should be a target if applicable to the action
     */
    public static void makeChoice(String choice, Entity... e) {
        switch (choice) {
            case "attack" :
                e[0].attack(e[1]);
                e[1].checkHealth();
                break;

            case "block" :
                e[0].block();
                break;

            case "checkHealth" :
                System.out.println("Your health is " + e[0].getHealth() + "/" + e[0].getMaxHealth());
                break;

            case "turnOrder" :
                System.out.println("The turn order is:");
                for (Entity entity : e) {
                    if (entity.getIsAlive()) {
                        System.out.println(entity.getName());
                    }
                }
                System.out.println();
                break;
        }
    }

    /**
     * @author Harrison Brown
     * @param e an array of enemies
     * @return returns true if an entity in the array is alive
     */
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
     */
    public static void activeCombat(Player[] players, Enemy[] enemies) {
        Scanner scan = new Scanner(System.in);

        Entity[] order = BattleSystem.makeTurnOrder(players, enemies);

        System.out.println();
        System.out.println("The turn order is:");
        for (Entity e : order) {
            System.out.println(e.getName());
        }
        System.out.println();
        int act;

        while (BattleSystem.entitiesLive(enemies) && BattleSystem.entitiesLive(players)) {
            for (Entity e : order) {
                if (e.isBlocking()) {
                    System.out.println(e.getName() + " got tired from bracing for an attack that never came");
                    e.switchBlock();
                    System.out.println();
                }
                if (e instanceof Enemy && e.getIsAlive()) {
                    act = BattleSystem.randomVal(0, 1);
                    if (act == 0) {
                        if (players.length == 1) {
                            BattleSystem.makeChoice("attack", e, players[0]);
                            if (!players[0].getIsAlive()) {
                                System.out.println(players[0].getName() + " You died");
                                if (!BattleSystem.entitiesLive(players)) {
                                    break;
                                }
                            }
                        } else {
                            int p = BattleSystem.randomVal(0, players.length - 1);
                            BattleSystem.makeChoice("attack", e, players[p]);
                            if (!players[p].getIsAlive()) {
                                System.out.println(players[p].getName() + "You died");
                            }
                        }
                        System.out.println();

                    } else {
                        BattleSystem.makeChoice("block", e);
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
                            BattleSystem.makeChoice("attack", e, enemies[choice]);
                        } else if (act == 1) {
                            BattleSystem.makeChoice("block", e);
                        } else if (act == 2) {
                            BattleSystem.makeChoice("checkHealth", e);
                        } else if (act == 3) {
                            BattleSystem.makeChoice("turnOrder", order);
                        }
                    } while (act > 1);
                    System.out.println();

                }
            }
        }

        if (!BattleSystem.entitiesLive(enemies)) {
            System.out.println("Players win!!!");
        } else {
            System.out.println("Players lose!!!");
        }
    }




    /**
     * utility to return a random value
     * @param min minimum bound
     * @param max maximum bound
     * @return the generated random value
     */
    public static int randomVal(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }
}
