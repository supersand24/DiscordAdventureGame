package Bot;

import Game.Game;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**Class: Main
 * @author Justin Sandman
 * @version 1.2
 *
 * Everything we should need to have the bot up and running.
 *
 */
public class Main {

    private static String token;
    public final static String COMMAND_SIGN = "!";

    /**Method: setToken
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Grabs the token from the BOT.token file.
     */
    private static void setToken() {

        try {
            File tokenLocation = new File("BOT.token");
            Scanner tokenIn = new Scanner(tokenLocation);
            token = tokenIn.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find BOT.token file.");
        }

    }

    /**Method: addSlashCommands
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     * Sends all slash command information to Discord.
     * Only want to do this once, per edit.
     */
    private static void addSlashCommands(JDA jda) {
        System.out.println("Running Slash Commands, be sure to disable this on next run.");
        CommandListUpdateAction commands = jda.updateCommands();

        //Adventure Command
        commands.addCommands(
                new CommandData("adventure","Gets ready to go on an adventure.")
        ).queue();

    }

    /**Method: main
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Starts the bot.
     */
    public static void main(String[] args) {

        //Pulls the token from the BOT.token file.
        setToken();

        //Start the Bot.
        if (token != null) {
            JDABuilder jdaBuilder = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_MESSAGES
            ).setMemberCachePolicy(MemberCachePolicy.ONLINE);

            jdaBuilder.addEventListeners(new Listener());

            try {
                JDA jda = jdaBuilder.build();
                jda.awaitReady();

                //addSlashCommands(jda);

                //Sets up the Game, if there is an error, app will exit.
                if ( !Game.setUp(jda.getGuildById(899410801906044991L)) ) {
                    System.out.println("An error occurred, app will stop running.");
                    System.exit(404);
                }

            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
