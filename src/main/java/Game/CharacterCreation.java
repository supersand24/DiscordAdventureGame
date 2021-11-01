package Game;

import Game.Entities.Player;
import Game.Items.Weapons.Sword;
import Game.Items.Weapons.Weapon;
import net.dv8tion.jda.api.entities.Member;

import java.util.Scanner;

/**
 * This class controls character creation
 *
 * @author harrison Brown
 * @version 0.1
 */
public class CharacterCreation {
    private final static Scanner scan = new Scanner(System.in);

    /**
     * starts character creation
     * @param member the member to make the character for
     * @return the new player object
     */
    public static Player makePlayer(Member member) {
        Player player;
        String name;
        Player.PlayerRace race;
        Player.PlayerGender gender;
        Weapon weapon;

        race = getPlayerRace();
        scan.reset();
        name = getPlayerName();
        scan.reset();
        gender = getPlayerGender();
        scan.reset();
        weapon = getPlayerWeapon();

        player = createPlayer(race, name, gender, weapon, member);

        return player;
    }

    public static Player makePlayer() {
        Player player;
        String name;
        Player.PlayerRace race;
        Player.PlayerGender gender;
        Weapon weapon;

        race = getPlayerRace();
        scan.reset();
        name = getPlayerName();
        scan.reset();
        gender = getPlayerGender();
        scan.reset();
        weapon = getPlayerWeapon();

        player = createPlayer(race, name, gender, weapon);
        System.out.println("New player created\n" + player);

        return player;
    }


    private static Player.PlayerRace getPlayerRace() {
        System.out.println("Pick a race from these options:");
        for (Player.PlayerRace r : Player.PlayerRace.values()) {
            System.out.print(r + ", ");
        }
        System.out.println();
        System.out.print("Enter number from 0 to " + (Player.PlayerRace.values().length-1)+ ": ");

        return Player.PlayerRace.values()[scan.nextInt()];
    }

    private static String getPlayerName() {
        System.out.print("Enter a name: ");
        return scan.next();
    }

    private static Player.PlayerGender getPlayerGender() {
        System.out.println("Pick a gender from these options:");
        for (Player.PlayerGender g : Player.PlayerGender.values()) {
            System.out.print(g + ", ");
        }
        System.out.println();
        System.out.print("Enter number from 0 to " + (Player.PlayerGender.values().length-1) + ": ");


        return Player.PlayerGender.values()[scan.nextInt()];
    }

    private static Weapon getPlayerWeapon() {
        return new Sword();
    }

    private static Player createPlayer(Player.PlayerRace race, String name, Player.PlayerGender gender, Weapon weapon, Member member) {
        Player p = new Player(name, member, race);
        p.setGender(gender);
        p.setRace(race);
        p.addWeapon(weapon);
        return p;
    }

    private static Player createPlayer(Player.PlayerRace race, String name, Player.PlayerGender gender, Weapon weapon) {
        Player p = new Player(name, race);
        p.setGender(gender);
        p.addWeapon(weapon);
        return p;
    }

}
