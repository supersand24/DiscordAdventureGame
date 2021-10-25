package Bot;

import Game.Game;
import Game.MapManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
    private static void addSlashCommands(Guild guild) {
        System.out.println("Running Slash Commands, be sure to disable this on next run.");
        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                //Battle
                new CommandData("battle","All commands related to battling.").addSubcommands(
                        //Attack
                        new SubcommandData("attack","Attacks an enemy.").addOptions(
                                new OptionData(OptionType.INTEGER,"target", "Target you would like to hit.").setRequired(true)
                        ),
                        //Block
                        new SubcommandData("block","Blocks next attack.")
                ),
                //Party
                new CommandData("party","Party related commands.").addSubcommands(
                        //Create
                        new SubcommandData("create","Creates a party."),
                        new SubcommandData("join", "Joins an existing party.").addOptions(
                                new OptionData(OptionType.USER,"member", "A member of the party you would like to join.").setRequired(true)
                        )
                ),
                //Vote
                new CommandData("vote","Starts a vote.")

        ).queue();

        //Adventure Command
        List<Command.Choice> data = new ArrayList<>();
        for (MapManager.Direction dir : MapManager.Direction.values()) {
            data.add(new Command.Choice(dir.getName(),dir.getName()));
        }
        commands.addCommands(
                new CommandData("adventure","Gets ready to go on an adventure.").addOptions(
                        new OptionData(OptionType.STRING,"direction","Which direction you would like to go.")
                                .addChoices(data)
                )
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

                Guild guild = jda.getGuildById(899410801906044991L);

                if (guild != null) {
                    addSlashCommands(guild);

                    //Sets up the Game, if there is an error, app will exit.
                    if (!Game.setUp(guild)) {
                        System.out.println("An error occurred, app will stop running.");
                        System.exit(404);
                    }
                } else {
                    System.out.println("Guild was null, app will stop running.");
                    System.out.println(420);
                }

            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
