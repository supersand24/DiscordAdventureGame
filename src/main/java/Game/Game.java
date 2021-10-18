package Game;

import Game.Entities.EnemyTypes.*;
import Game.Entities.Player;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.Scanner;

/**Class: Game
 * @author Harrison Brown and Justin Sandman
 * @version 0.2
 *
 * The game.
 *
 */
public class Game {

    public static Guild guild;

    public static Role roleAdventurer;
    public static Role roleDead;

    static boolean gameStarted = false;

    /**Method: startGame
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Tries to start the game, if one is not in progress.
     * Is called by a Developer in the server.
     */
    public static void startGame() {

        if (!gameStarted) {
            gameStarted = true;
            System.out.println("Starting Game");
            sendMessage("Game Starting!");
        } else {
            System.out.println("Game Already Started");
        }

    }

    /**Method: joinGame
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Tries to join the game, if one is in progress.
     * Is called by a Member in the server.
     */
    public static void joinGame(Member member, Message msg) {
        if (!member.getRoles().contains(roleAdventurer)) {
            if (gameStarted) {
                System.out.println(member.getEffectiveName() + " has joined the Game.");
                msg.reply("You joined the game!").queue();
                guild.addRoleToMember(member, roleAdventurer).queue();
            } else {
                msg.reply("Game isn't active.").queue();
            }
        } else {
            msg.reply("You are already in the game!").queue();
        }

    }

    /**Method: sendMessage
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Sends a message to the testing channel.
     */
    private static void sendMessage(String msg) {
        //Send a message to the test channel.
        guild.getTextChannelById(899441117781721158L).sendMessage(msg).queue();
    }

    /**
     * @author Harrison Brown
     * @version 1.0
     * @param enemies an array of enemies
     * @return returns true if an enemy in the array is alive
     */
    private static boolean enemiesLive (Enemy[] enemies)
    {
        int alive = enemies.length;
        for (int i = 0; i < enemies.length; i++)
        {
            if (!enemies[i].getIsAlive())
            {
                alive -= 1;
            }
        }
        if (alive == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**Method: main
     * @author Harrison Brown
     * @version 0.2
     * Written : October 17, 2021
     *
     * Temporary method, to test the game without connecting to Discord.
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Player harrison = new Player(100, 50, "Slayer of Thots", "Harrison", "Harrison");
        Enemy[] enemies = new Enemy[2];
        enemies[0] = new Goblin();
        enemies[1] = new Goblin();

        do {

            for (Enemy x : enemies) {
                System.out.print(x.getHealth() + " | ");
                x.attack(harrison);
            }
            System.out.println();
            System.out.println("Which Goblin do you want to attack? (0 or 1) ");
            int choice = scan.nextInt();
            scan.reset();
            harrison.attack(enemies[choice]);
            for (Enemy x : enemies) {
                x.checkHealthStatus();
            }
            harrison.checkHealthStatus();

        } while (enemiesLive(enemies) && harrison.getIsAlive());
        if (harrison.getIsAlive()) {
            System.out.println("You Win!");
        } else {
            System.out.println("Enemies Win!");
        }

    }
}
