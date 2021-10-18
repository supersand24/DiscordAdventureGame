package Game;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

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

    /**Method: main
     * @author Harrison Brown
     * Written : October 17, 2021
     *
     * Temporary method, to test the game without connecting to Discord.
     */
    public static void main(String[] args) {

    }
}
