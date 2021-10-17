package Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**Class: Main
 * @author Justin Sandman
 * @version 1.0
 *
 * Everything we should need to have the bot up and running.
 *
 */
public class Main {

    static String token;

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

            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
