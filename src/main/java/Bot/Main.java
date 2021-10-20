package Bot;

import Game.Game;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Everything we should need to have the bot up and running.
 *
 * @author Justin Sandman
 * @version 1.2
 *
 */
public class Main {

    private static String token;
    public final static String COMMAND_SIGN = "!";

    /**
     * Grabs the token from the BOT.token file.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
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

    /**
     * Sends all slash command information to Discord.
     * Only want to do this once, per edit.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    private static void addSlashCommands(JDA jda) {
        System.out.println("Running Slash Commands, be sure to disable this on next run.");
        CommandListUpdateAction commands = jda.updateCommands();

        //Adventure Command
        commands.addCommands(
                new CommandData("adventure","Gets ready to go on an adventure.")
        ).queue();

        //Attack Command
        commands.addCommands(
                new CommandData("attack","Attacks an enemy.").addOptions(
                        new OptionData(OptionType.INTEGER,"target", "Target you would like to hit.").setRequired(true)
                )
        ).queue();

        //Block Command
        commands.addCommands(
                new CommandData("block","Blocks next attack.")
        ).queue();

        //Use Item Command
        commands.addCommands(
                new CommandData("use_item","Uses an item from your inventory.")
        ).queue();

    }

    /**
     * Starts the bot and the game.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
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
