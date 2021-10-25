package Bot;

import Game.Game;
import Game.BattleSystem;
import Game.MapManager;
import Game.Party;
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

    /**
     * Runs as soon as the Listener is ready to go.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     */
    @Override
    public void onReady(@NotNull ReadyEvent e) {
        roleDeveloper = e.getJDA().getGuilds().get(0).getRoleById(899416257537908777L);
    }

    /**
     * This method is run whenever a message is sent in a channel.
     * It checks the message if there is a command keyword, and starts with Main.COMMAND_SIGN.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
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
                                    case "map"   -> {
                                        MapManager.printMap();
                                        System.out.println(Game.parties.get(0));
                                    }
                                    case "nuke"   -> BattleSystem.endBattle(Game.parties.get(0));
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

    /**
     * This method is run whenever a slash command is sent.
     * It checks the message if there is a command keyword.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent slashCommand) {
        String subCommand = slashCommand.getSubcommandName();
        switch (slashCommand.getName()) {
            case "party" -> {
                if (subCommand != null) {
                    switch (subCommand) {
                        case "create"   -> Game.startParty(slashCommand);
                        case "join"     -> {
                            slashCommand.deferReply(true).queue();
                            Party party = Game.players.get(slashCommand.getOptionsByName("member").get(0).getAsMember()).getParty();
                            if (party.joinParty(Game.players.get(slashCommand.getMember())) ) {
                                slashCommand.getHook().sendMessage("You joined the " + party.getChannel().getAsMention() + ".").queue();
                            } else {
                                slashCommand.getHook().sendMessage("There was an issue joining the party.").queue();
                            }
                        }
                        case "leave"    -> System.out.println("Leaving Party.");
                    }
                }
            }
            case "battle" -> {
                if (subCommand != null) {
                    switch (subCommand) {
                        case "attack"   -> BattleSystem.makeChoice(BattleSystem.actions.ATTACK,slashCommand);
                        case "block"    -> BattleSystem.makeChoice(BattleSystem.actions.BLOCK, slashCommand);
                    }
                }
            }
            case "adventure"        -> Game.leaveTown(slashCommand);
            case "vote"             -> Game.castVote(slashCommand);
        }
    }

    /**
     * Runs whenever a button is pressed in discord
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent e) {
        if (e.getButton().getId().startsWith("vote_")) {
            Game.processVote(e);
        } else {
            switch (e.getButton().getId()) {
                case "joinParty" -> {
                    e.deferReply(true).queue();
                    Party party = Game.players.get(e.getMessage().getMentionedMembers().get(0)).getParty();
                    if (party.joinParty(Game.players.get(e.getMember())) ) {
                        e.getHook().sendMessage("You joined the " + party.getChannel().getAsMention() + ".").queue();
                    } else {
                        e.getHook().sendMessage("There was an issue joining the party.").queue();
                    }
                }
            }
        }
    }
}
