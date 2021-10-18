package Game;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

/**Class: Game
 * @author Harrison Brown and Justin Sandman
 * @version 0.3.1
 *
 * Handles everything for the game.
 *
 */
public class Game {

    public static Guild guild;

    public static Role roleAdventurer;

    public static Category categoryAdventure;
    public static Category categorySettlement;

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
            sendMessage("Game Starting!  To join, reply to this message your characters name.");
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
                guild.addRoleToMember(member, roleAdventurer).queue();
                guild.modifyNickname(member,msg.getContentRaw().trim()).queue();
            } else {
                msg.reply("Game isn't active.").queue();
            }
        } else {
            msg.reply("You are already in the game!").queue();
        }

    }

    /**Method: startAdventure
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     * Opens an invite for other players to join an adventure.
     * Is called by a Member in the server, only while in a settlement, and not in another party.
     */
    public static void startAdventure(SlashCommandEvent slashCommand) {

        //Get user Member
        Member member = slashCommand.getMember();

        if (canPlayGame(member)) {
            //If member is in a settlement, and not on an adventure.
            if (categorySettlement.getMembers().contains(member)) {
                if (!categoryAdventure.getMembers().contains(member)) {
                    slashCommand.reply(member.getAsMention() + " is going on an adventure.")
                            .addActionRow(
                                    Button.primary("joinAdventure", "Join Adventure Party"),
                                    Button.danger("leaveTown", "Leave Town")
                            ).queue();
                    guild.createTextChannel("party", categoryAdventure).queue(textChannel -> {
                        textChannel.createPermissionOverride(member)
                                .setAllow(Permission.VIEW_CHANNEL)
                                .queue();
                        textChannel.sendMessage(member.getAsMention() + " This is your party's private text channel.").queue();
                    });
                } else {
                    slashCommand.reply("You are already in a Party!").queue();
                }
            } else {
                slashCommand.reply("You are not in a Settlement.").queue();
            }
        } else {
            slashCommand.reply("You are unable to use this command!").queue();
        }
    }

    /**Method: joinAdventure
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     * Joins an existing party, if they have not left yet.
     * Is called by a Member that presses a button.
     */
    public static void joinAdventure(ButtonClickEvent e) {

        Member partyLeader = e.getMessage().getMentionedMembers().get(0);
        Member member = e.getMember();

        if (canPlayGame(member)) {
            //If you are not already in a party.
            if (!categoryAdventure.getMembers().contains(member)) {
                //If the party hasn't already left yet.
                if (categorySettlement.getMembers().contains(partyLeader)) {

                    //Find the Party Channel, and add the member to it, and notify the party.
                    TextChannel channel = findPartyChannel(partyLeader);
                    if (channel != null) {
                        channel.sendMessage(member.getAsMention() + " has joined the Party!").queue();
                        channel.createPermissionOverride(member)
                                .setAllow(Permission.VIEW_CHANNEL)
                                .queue();
                    } else {
                        System.out.println("Could not find party channel.");
                    }
                } else {
                    e.reply("The party has already left!").queue();
                }
            } else {
                e.reply("You are already in a Party!").queue();
            }
        }
    }

    /**Method: leaveTown
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     * The party leaves town and goes on an adventure.
     * Is called by a Member that presses a button, makes sure the party leader pressed the button.
     */
    public static void leaveTown(ButtonClickEvent e) {

        Member partyLeader = e.getMessage().getMentionedMembers().get(0);
        Member buttonClickingMember = e.getMember();

        //If the party leader clicks the leaveTown button.
        if (buttonClickingMember.getIdLong() == partyLeader.getIdLong()) {
            e.getMessage().editMessage(partyLeader.getAsMention() + " has left on an adventure.").queue();
            TextChannel channel = findPartyChannel(partyLeader);
            if (channel != null) {
                channel.sendMessage("@everyone your party has left the town!").queue();
                for (Member member : channel.getMembers()) {
                    if (member.getRoles().contains(roleAdventurer)) {
                        e.getTextChannel().createPermissionOverride(member)
                                .setDeny(Permission.VIEW_CHANNEL)
                                .queue();
                    }
                }
            }
        } else {
            e.reply("You are not the party leader.").queue();
        }

    }
    
    private static TextChannel findPartyChannel(Member member) {
        for (TextChannel channel : categoryAdventure.getTextChannels()) {
            if (channel.getMembers().contains(member)) {
                return channel;
            }
        }
        return null;
    }

    /**Method: canPlayGame
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     * Simple check to make sure the game is active, and the player is picked up by the Game.
     */
    private static boolean canPlayGame(Member member) {
        return (member.getRoles().contains(roleAdventurer) && gameStarted);
    }

    /**Method: sendMessage
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     * Sends a message to the testing channel.
     */
    private static void sendMessage(String msg) {
        //Send a message to the test channel.
        guild.getTextChannelById(899417703486455848L).sendMessage(msg).queue();
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
