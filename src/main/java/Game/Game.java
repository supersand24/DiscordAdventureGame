package Game;

import net.dv8tion.jda.api.entities.Guild;

/**Class: Game
 * @author Harrison Brown
 * @version 0.1
 *
 * The game.
 *
 */
public class Game {

    public static Guild guild;

    enum gameStates {
        NO_GAME,
        AWAITING_START,
        STARTED
    }

    static gameStates gameState = gameStates.NO_GAME;

    /**Method: startGame
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Tries to start the game, if one is not in progress.
     * Is called by a Developer in the server.
     */
    public static void startGame() {

        if (gameState == gameStates.NO_GAME) {
            System.out.println("Starting Game");
            gameState = gameStates.AWAITING_START;
            sendMessage("Game Starting!");
        } else {
            System.out.println("Game Already Started");
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
     * Temporary method, to test the game portion without connecting to Discord.
     */
    public static void main(String args[]) {

    }
}
