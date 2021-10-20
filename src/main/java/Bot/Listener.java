package Bot;

import Game.Game;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**Class: Listener
 * @author Justin Sandman
 * @version 1.3
 *
 * Where messages come in and are handled.
 *
 */
public class Listener extends ListenerAdapter {

    Role roleDeveloper;

    /**Method: onReady
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Runs as soon as the Listener is ready to go.
     * Gets the Developer Role ready, so Listener can check members for it.
     * Sends the Guild to the Game, so Game can send messages as needed.
     */
    @Override
    public void onReady(@NotNull ReadyEvent e) {
        roleDeveloper = e.getJDA().getGuilds().get(0).getRoleById(899416257537908777L);
    }

    /**Method: onMessageReceived
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * This method is run whenever a message is sent in a channel.
     * It checks the message if there is a command keyword, and starts with Main.COMMAND_SIGN
     * Then it runs the proper command.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        //Ignore messages sent by bots, even though there should not be any bots.
        if (!e.getAuthor().isBot()) {
            //Member is not null.
            if (e.getMember() != null) {
                //Ignore empty messages.
                if (!e.getMessage().getContentRaw().isEmpty()) {
                    //If in the correct channel, later will be changed to not in #meta-gaming channel, and #dev-general channels.
                    //if (e.getChannel().getName().equalsIgnoreCase("testing")) {
                        String messageRaw = e.getMessage().getContentRaw().trim();
                        if (messageRaw.startsWith(Main.COMMAND_SIGN)) {
                            String command = messageRaw.split("//s+")[0].substring(Main.COMMAND_SIGN.length());

                            //Generic Commands
                            switch (command) {
                                //case "test" -> Game.inSettlement(e.getMember());
                                //case "adventure" -> Game.startAdventure(sl);
                            }

                            //Developer Only Commands
                            if (e.getMember().getRoles().contains(roleDeveloper)) {
                                switch (command) {
                                    case "startgame" -> Game.startGame();
                                    case "save"      -> Game.save();
                                }
                            }
                        } else {
                            if (e.getMessage().getReferencedMessage() != null) {
                                if (e.getMessage().getReferencedMessage().getAuthor().isBot()) {
                                    System.out.println("I received a reply");
                                    if (e.getMessage().getReferencedMessage().getContentRaw().startsWith("Game Starting!")) {
                                        Game.joinGame(e.getMember(), e.getMessage());
                                    }
                                }
                            }
                        }
                    //}
                }
            }
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent slashCommand) {
        switch (slashCommand.getName()) {
            case "adventure"        -> Game.startAdventure(slashCommand);
            case "taco"             -> System.out.println("Taco");
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent e) {
        switch (e.getButton().getId()) {
            case "joinAdventure"    -> Game.joinAdventure(e);
            case "leaveTown"        -> Game.leaveTown(e);
        }
    }
}
