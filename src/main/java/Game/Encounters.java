package Game;

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
        BRANCHPATH(3, true),
        NONE(4, true);

        int weight;
        boolean canRepeat;

        EncounterType(int weight, boolean canRepeat) {
            this.weight = weight;
            this.canRepeat = canRepeat;
        }

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
            types.addAll(Arrays.asList(EncounterType.values()));
        } else {
            for (EncounterType type : EncounterType.values()) {
                if (!type.equals(p.getCurrentEncounter())) {
                    types.add(type);
                }
            }
        }

        //System.out.println(types);


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

        p.setCurrentEncounter(en);
    }

    /**
     * creates battle encounter
     * @author Harrison Brown
     */
    public static void generateEnemies(Party p) {

    }


}