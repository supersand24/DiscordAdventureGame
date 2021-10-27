package Game;

import Game.Entities.Player;
import Game.Items.Weapons.Sword;
import Game.Items.Weapons.Weapon;
import net.dv8tion.jda.api.entities.Member;

import java.util.Scanner;

public class CharacterCreation {
    private static Scanner scan = new Scanner(System.in);

    public static Player makePlayer(Member member) {
        Player player;
        String name;
        Player.PlayerRace race;
        Player.PlayerGender gender;
        Weapon weapon;

        race = getPlayerRace();
        name = getPlayerName();
        gender = getPlayerGender();
        weapon = getPlayerWeapon();

        player = createPlayer(race, name, gender, weapon, member);

        return player;
    }

    private static Player.PlayerRace getPlayerRace() {
        System.out.println("Pick a race from these options:");
        for (Player.PlayerRace r : Player.PlayerRace.values()) {
            System.out.print(r + ", ");
        }
        System.out.println();
        System.out.print("Enter number fro 0 to " + Player.PlayerRace.values().length + ": ");


        return Player.PlayerRace.values()[scan.nextInt()];
    }

    private static String getPlayerName() {
        System.out.println("Enter a name: ");
        return scan.next();
    }

    private static Player.PlayerGender getPlayerGender() {
        System.out.println("Pick a gender from these options:");
        for (Player.PlayerGender g : Player.PlayerGender.values()) {
            System.out.print(g + ", ");
        }
        System.out.println();
        System.out.print("Enter number fro 0 to " + Player.PlayerRace.values().length + ": ");


        return Player.PlayerGender.values()[scan.nextInt()];
    }

    private static Weapon getPlayerWeapon() {
        return new Sword();
    }

    private static Player createPlayer(Player.PlayerRace race, String name, Player.PlayerGender gender, Weapon weapon, Member member) {
        Player p = new Player(name, member);
        p.setGender(gender);
        p.setRace(race);
        p.addWeapon(weapon);
        return p;
    }

}
