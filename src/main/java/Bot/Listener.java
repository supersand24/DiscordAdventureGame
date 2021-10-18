package Bot;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**Class: Listener
 * @author Justin Sandman
 * @version 1.0
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
     */
    @Override
    public void onReady(@NotNull ReadyEvent e) {
        System.out.println("Listener is ready, running onReady()");
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
            //Ignore empty messages
            if (!e.getMessage().getContentRaw().isEmpty()) {
                //If in the correct channel, later will be changed to not in #meta-gaming channel, and #dev-general channels.
                if (e.getChannel().getName().equalsIgnoreCase("testing")) {
                    String messageRaw = e.getMessage().getContentRaw().trim();
                    if (messageRaw.startsWith(Main.COMMAND_SIGN)) {
                        String command = messageRaw.split("//s+")[0].substring(Main.COMMAND_SIGN.length());

                        //Generic Commands
                        switch (command) {
                            case "test"     -> e.getMessage().reply("Message Received").queue();
                        }

                        //Developer Only Commands
                        if ( e.getMember().getRoles().contains(roleDeveloper) ) {
                            switch (command) {
                                case "startgame"    -> e.getMessage().reply("Game Starting").queue();
                            }
                        }
                    }
                }
            }
        }
    }
}
