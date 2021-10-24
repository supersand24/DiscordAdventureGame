package Game;

import Game.Entities.EnemyTypes.*;
import Game.Entities.EnemyTypes.Grunts.Goblin;
import Game.Entities.Player;
import Game.Items.Bottle;
import Game.Items.Useable.Potion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.*;
import java.util.*;

/**
 * Handles everything for the game.
 *
 * @author Harrison Brown
 * @author Justin Sandman
 * @version 0.3.1
 *
 */
public class Game {

    public static Guild guild;

    public static Role roleAdventurer;
    public static Role roleDeveloper;

    public static Category categoryAdventure;
    public static Category categorySettlement;

    static boolean gameStarted;

    public static List<Player> players = new ArrayList<>();
    public static List<Party> parties = new ArrayList<>();

    private static Area mainHub = new Area(MapManager.AreaType.SETTLEMENT);

    /**
     * Tries to start the game, if one is not in progress.
     * Is called by a Developer in the server.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     */
    public static void startGame() {

        if (!gameStarted) {
            gameStarted = true;
            System.out.println("Starting Game");
            sendMessage("Game Starting!  To join, reply to this message your characters name.");
            guild.createTextChannel(mainHub.getName(),categorySettlement).queue(channel -> {
                MapManager.addArea(MapManager.MAP_SIZE/2+1,MapManager.MAP_SIZE/2+1,mainHub);
                mainHub.setChannelId(channel.getIdLong());
                MapManager.printMap();
            });
        } else {
            System.out.println("Game Already Started");
        }

    }

    /**
     * Tries to join the game, if one is in progress.
     * Is called by a Member in the server.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     */
    public static void joinGame(Member member, Message msg) {
        if (!member.getRoles().contains(roleAdventurer)) {
            if (gameStarted) {
                guild.addRoleToMember(member, roleAdventurer).queue();
                String playerName = msg.getContentRaw().trim();
                guild.modifyNickname(member,playerName).queue();
                players.add(new Player(playerName));
                guild.getTextChannelById(mainHub.getChannelId())
                        .createPermissionOverride(member).setAllow(
                                Permission.VIEW_CHANNEL
                        ).queue();
                System.out.println(member.getUser().getName()  + " has joined the Game.");
            } else {
                msg.reply("Game isn't active.").queue();
            }
        } else {
            msg.reply("You are already in the game!").queue();
        }

    }

    public static void startParty(SlashCommandEvent slashCommand) {

        //Get user Member
        Member member = slashCommand.getMember();

        if (member != null) {
            if (canPlayGame(member)) {
                //If member is in a settlement, and not on an adventure.
                if (categorySettlement.getMembers().contains(member)) {
                    if (!categoryAdventure.getMembers().contains(member)) {
                        slashCommand.reply(member.getAsMention() + " started a party!")
                                .addActionRow(
                                        Button.primary("joinParty", "Join Party")
                                ).queue();
                        guild.createTextChannel("party", categoryAdventure).queue(textChannel -> {
                            textChannel.createPermissionOverride(member)
                                    .setAllow(Permission.VIEW_CHANNEL)
                                    .queue();
                            textChannel.sendMessage(member.getAsMention() + " This is your party's private text channel.").queue();
                            parties.add(new Party(textChannel.getIdLong(), member, MapManager.getArea(slashCommand.getChannel().getIdLong())));
                            System.out.println(parties.get(0).toString());
                        });
                    } else {
                        slashCommand.reply("You are currently on an adventure, go back to town to start a new party.").setEphemeral(true).queue();
                    }
                } else {
                    slashCommand.reply("You need to be in a Settlement to create a party.").setEphemeral(true).queue();
                }
            } else {
                slashCommand.reply("The game is not started! Ask a " + roleDeveloper.getAsMention() + " to start it.").setEphemeral(true).queue();
            }
        } else {
            slashCommand.reply("ERROR : Member not found!").setEphemeral(true).queue();
            System.out.println("Member could not be found.");
        }
    }

    public static void joinParty(ButtonClickEvent buttonClick) {
        Member partyLeader = buttonClick.getMessage().getMentionedMembers().get(0);
        Member member = buttonClick.getMember();

        buttonClick.deferReply(true).queue();

        if (member != null && partyLeader != null) {
            if (canPlayGame(member)) {
                //If you are not already in a party.
                if (!categoryAdventure.getMembers().contains(member)) {
                    //Find the Party Channel, and add the member to it, and notify the party.
                    TextChannel channel = findPartyChannel(partyLeader);
                    if (channel != null) {
                        channel.sendMessage(member.getAsMention() + " has joined the Party!").queue();
                        channel.createPermissionOverride(member)
                                .setAllow(Permission.VIEW_CHANNEL)
                                .queue();
                        buttonClick.getHook().sendMessage("You joined the " + channel.getAsMention() + ".").queue();
                    } else {
                        buttonClick.getHook().sendMessage("ERROR : Party not found!").queue();
                        System.out.println("Could not find party channel.");
                    }
                } else {
                    buttonClick.getHook().sendMessage("You are already in a party.").queue();
                }
            }
        } else {
            buttonClick.getHook().sendMessage("ERROR : Member not found!").queue();
            System.out.println("A member could not be found.");
        }
    }

    /**
     * The party leaves town and goes on an adventure.
     * Is called by a Member that presses a button, makes sure the party leader pressed the button.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    public static void leaveTown(SlashCommandEvent slashCommand) {

        slashCommand.deferReply(true).queue();

        Member member = slashCommand.getMember();

        if (member != null) {
            Party party = findParty(member);
            if (party != null) {
                if (member.equals(party.getLeader())) {
                    TextChannel partyChannel = Game.guild.getTextChannelById(party.getChannelId());
                    if (partyChannel != null) {
                        partyChannel.sendMessage("@everyone the party has left town!").queue();

                        //Get direction from command
                        MapManager.Direction dir = null;
                        OptionMapping optionMapping = slashCommand.getOption("direction");
                        if (optionMapping != null) {
                            switch (optionMapping.getAsString().toLowerCase()) {
                                case "northwest" -> dir = MapManager.Direction.NORTH_WEST;
                                case "north" -> dir = MapManager.Direction.NORTH;
                                case "northeast" -> dir = MapManager.Direction.NORTH_EAST;
                                case "east" -> dir = MapManager.Direction.EAST;
                                case "southeast" -> dir = MapManager.Direction.SOUTH_EAST;
                                case "south" -> dir = MapManager.Direction.SOUTH;
                                case "southwest" -> dir = MapManager.Direction.SOUTH_WEST;
                                case "west" -> dir = MapManager.Direction.WEST;
                            }
                        } else {
                            dir = MapManager.Direction.NORTH;
                        }

                        //REMOVES ALL PARTY MEMBERS FROM SLASH COMMAND CHANNEL
                        //NEEDS TO BE PARTY LOCATION CHANNEL
                        for (Member m : partyChannel.getMembers()) {
                            if (m.getRoles().contains(roleAdventurer)) {
                                slashCommand.getTextChannel().getPermissionOverride(m).delete().queue();
                            }
                        }

                        slashCommand.getHook().sendMessage("Your party left on an adventure. " + partyChannel.getAsMention()).queue();

                        party.setGoingTo(dir);
                        party.setComingFrom(dir.getOpposite());

                        //Generate new area, if it doesn't exist.
                        //ADD SOMEWHERE IN HERE TO CHECK IF SETTLEMENT CAN GENERATE NEW PATHS
                        if (MapManager.getAdjacentArea(party.location,dir) == null) {
                            MapManager.addAdjacentArea(party.getLocation(),dir,new Area(MapManager.AreaType.PATH));
                        }
                        party.previousAreas.add(party.getLocation());
                        party.setLocation(MapManager.getAdjacentArea(party.getLocation(),dir));

                        partyChannel.sendMessage("```" + MapManager.printMap() + "```").queue();
                        System.out.println(party);

                    } else {
                        slashCommand.getHook().sendMessage("ERROR : Could not find party channel.").queue();
                        System.out.println("Could not find party text channel.");
                    }
                } else {
                    slashCommand.getHook().sendMessage("You are not the party leader.").queue();
                }
            } else {
                //Party not found
                slashCommand.getHook().sendMessage("ERROR : Could not find party.").queue();
                System.out.println("Could not find party.");
            }
        } else {
            slashCommand.getHook().sendMessage("ERROR : Could not find member.").queue();
            System.out.println("Could not find member.");
        }
    }

    enum Vote {
        CONTINUE,
        HEAD_BACK
    }

    /**
     * Sends a message in chat with button to press, this is the voting window.
     *
     * @param slashCommand Where to send the message.
     */
    public static void castVote(SlashCommandEvent slashCommand) {
        if (categoryAdventure.getMembers().contains(slashCommand.getMember())) {
            if (parties.get(0).vote.size() == 0) {
                slashCommand.reply("@everyone time to vote, what do you want to do?")
                        .addActionRow(
                                Button.primary("vote_continue", "Continue On"),
                                Button.primary("vote_headBack", "Go home")
                        ).queue(interactionHook -> interactionHook.retrieveOriginal().queue(message -> parties.get(0).voteMessage = message));
                parties.get(0).vote.put(Vote.CONTINUE,0);
                parties.get(0).vote.put(Vote.HEAD_BACK,0);
                System.out.println(parties.get(0).vote);
            } else {
                slashCommand.reply("There is already an on going vote!").setEphemeral(true).queue();
            }
        } else {
            slashCommand.reply("You are not on an adventure!").setEphemeral(true).queue();
        }

    }

    /**
     * This adds the vote to the party.
     *
     * @param event So we can get which vote was pressed.
     */
    public static void processVote(ButtonClickEvent event) {
        event.deferReply(true).queue();
        if (parties.get(0).vote.size() > 0) {
            if (!parties.get(0).hasVoted.contains(event.getMember())) {
                Vote vote = null;
                switch (event.getButton().getId().split("_")[1]) {
                    case "continue" -> vote = Vote.CONTINUE;
                    case "headBack" -> vote = Vote.HEAD_BACK;
                }

                int count = parties.get(0).vote.getOrDefault(vote, 0);
                parties.get(0).vote.put(vote, count + 1);

                System.out.println(parties.get(0).vote);

                parties.get(0).hasVoted.add(event.getMember());
                event.getHook().sendMessage("Your vote was counted for.").queue();

                if (parties.get(0).hasVoted.size() >= parties.get(0).getPlayers(Game.guild).size()) {
                    endVote(parties.get(0));
                }
            } else {
                event.getHook().sendMessage("You have already voted!").queue();
            }
        } else {
            event.getHook().sendMessage("There isn't a vote active.").queue();
        }
    }

    /**
     * Runs after 100% of votes are tallied in.
     *
     * @param party The party to check votes.
     */
    private static void endVote(Party party) {

        //Run the decision

        Map.Entry<Vote,Integer> maxEntry = null;

        for (Map.Entry<Vote,Integer> entry : party.vote.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        System.out.println(maxEntry.getKey());

        switch (maxEntry.getKey()) {
            case CONTINUE -> party.continueOn();
            case HEAD_BACK -> party.headBack();
        }

        //Reset Vars for next vote.
        party.vote.clear();
        party.hasVoted.clear();
        party.voteMessage = null;
    }

    /**
     * Goes through all the channels under the adventure category,
     * and finds the one channel that the member is present in.
     *
     * @author Justin Sandman
     * Written : October 19, 2021
     *
     */
    private static TextChannel findPartyChannel(Member member) {
        for (TextChannel channel : categoryAdventure.getTextChannels()) {
            if (channel.getMembers().contains(member)) {
                return channel;
            }
        }
        return null;
    }

    private static Party findParty(Member member) {
        TextChannel channel = findPartyChannel(member);
        for (Party party : parties) {
            if (party.channelId == channel.getIdLong())
                return party;
        }
        return null;
    }

    /**
     * Checks to make sure the game is active, and the player is picked up by the Game.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    private static boolean canPlayGame(Member member) {
        return (member.getRoles().contains(roleAdventurer) && gameStarted);
    }

    /**
     * Sends a message to the meta gaming text channel.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     */
    private static void sendMessage(String msg) {
        //Send a message to the test channel.
        guild.getTextChannelById(901660400649658448L).sendMessage(msg).queue();
    }

    /**
     * Runs at app start, will load any needed files.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     * @param guild The guild object of the Discord Server.
     * @return Returns true if no errors are present.
     *
     */
    public static boolean setUp(Guild guild) {

        System.out.println("Starting Game Set Up...");

        Game.guild = guild;

        roleAdventurer      = Game.guild.getRoleById(899464047001468978L);
        roleDeveloper       = Game.guild.getRoleById(899416257537908777L);

        categoryAdventure   = Game.guild.getCategoryById(899663492175511612L);
        categorySettlement  = Game.guild.getCategoryById(899464535180718090L);

        //Load any saved files
        //If save file is found, load all that and then set gameStarted true.
        //If no save file is found, keep default and leave gameStarted false.

        gameStarted = true;

        //Read from Party File
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("parties.dat"));
            do {
                parties.add((Party) ois.readObject());
            } while (true);
        } catch (EOFException e) {
            System.out.println(parties);
            try {
                assert ois != null;
                ois.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("parties.dat not found...");
            gameStarted = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //CHECK IF THE PARTY CHANNEL STILL EXISTS, JUST IN CASE ITS OLD DATA

        //TESTING BEGIN
        //guild.removeRoleFromMember(guild.getMemberById(262982533157879810L),roleAdventurer).queue();
        for (Member member : guild.getMembers()) {
            if (member.getRoles().contains(roleAdventurer)) {
                guild.removeRoleFromMember(member,roleAdventurer).queue();
            }
        }
        for (TextChannel channel : categoryAdventure.getTextChannels()) {
            channel.delete().queue();
        }
        for (TextChannel channel : categorySettlement.getTextChannels()) {
            channel.delete().queue();
        }
        //TESTING END

        //No errors occurred, game is set up properly.
        return true;

    }

    /**
     * Saves all the lists needed for the game to run properly.
     *
     * @author Justin Sandman
     *
     */
    public static void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("parties.dat"));
            for (Party party : parties) {
                oos.writeObject(party);
            }
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Temporary method, to test the game without connecting to Discord.
     *
     * @author Harrison Brown
     * Written : October 17, 2021
     *
     */
    public static void main(String[] args) {
        Bottle bottle = new Bottle();
        bottle.fill(new Potion(Potion.Liquid.WATER));
        bottle.getContents().getLiquid().setHowClean(Potion.Cleanliness.KRISP);
        //Party p1 = new Party();
        //p1.setCurrentEncounter(Encounters.EncounterType.MERCHANT);
        //Encounters.setEncounter(p1);
    }
}
