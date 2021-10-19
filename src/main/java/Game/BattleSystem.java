package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * All Battle System methods for the game (includes some static utilities as well)
 * @author Harrison Brown
 * @version 0.2
 */
public class BattleSystem {

    /**
     * returns an entity array sorted based on speed to be used as the turn order in battle
     */
    public static Entity[] makeTurnOrder(Enemy[] arr1, Player[] arr2) {
        Entity[] order = new Entity[arr1.length + arr2.length];

        int i = 0;

        for (Enemy e : arr1) {
                order[i] = e;
                i++;
        }

        for (Player p : arr2) {
            order[i] = p;
            i++;
        }

        Arrays.sort(order);

        return order;
    }

    /**
     * sets the decisions for enemies and players
     */
    public static void makeChoice(String choice, Entity self, Entity target) {
        switch (choice) {
            case "attack" :
                self.attack(target);
                break;
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
