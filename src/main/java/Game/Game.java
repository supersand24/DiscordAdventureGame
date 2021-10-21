package Game;

import Game.Entities.EnemyTypes.*;
import Game.Entities.EnemyTypes.Grunts.Goblin;
import Game.Entities.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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

    public static Category categoryAdventure;
    public static Category categorySettlement;

    static boolean gameStarted;

    public static List<Player> players = new ArrayList<>();
    public static List<Party> parties = new ArrayList<>();

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

    /**
     * Opens an invitation for other players to join an adventure.
     * Is called by a Member in the server, only while in a settlement, and not in another party
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
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
                        parties.add(new Party(textChannel.getIdLong()));

                        System.out.println(parties);
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

    /**
     * Joins an existing party, if they have not left yet.
     * Is called by a Member that presses a button.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
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

    /**
     * The party leaves town and goes on an adventure.
     * Is called by a Member that presses a button, makes sure the party leader pressed the button.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
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
                adventureEvent(findPartyChannel(partyLeader));
            }
        } else {
            e.reply("You are not the party leader.").queue();
        }

    }

    /**
     * Basic processing of an adventure event.
     * This case is a battle.
     *
     * @author Justin Sandman
     * Written : October 19, 2021
     *
     */
    private static void adventureEvent(TextChannel textChannel) {
        textChannel.sendMessage("Everyone walked down the long road.").queue();

        Party party = parties.get(0);

        party.enemies.add(new Goblin());
        party.enemies.add(new Goblin());
        party.enemies.add(new Goblin());
        //textChannel.sendMessage("A battle occurs, the enemies died.").queue();
        //PLACE BattleHandler here.
        BattleSystem.startBattle(party);

        party.sendBattleMessage();

        //Potentially get a list of dead people from BattleHandler
        /*
        for (Enemy en : parties.get(0).enemies) {
            Collections.addAll(parties.get(0).loot, en.getInventory());
        }
        */
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
            case CONTINUE -> party.voteMessage.editMessage("The party voted, to continue on!").queue();
            case HEAD_BACK -> party.voteMessage.editMessage("The party voted, go back to town.").queue();
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

    private static Party findParty(TextChannel channel) {
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
        guild.getTextChannelById(900221982514245652L).sendMessage(msg).queue();
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
        players.add(new Player(20,20,guild.getMemberById(262982533157879810L).getNickname(),"male"));
        players.add(new Player(20,20,guild.getMemberById(286307112072511490L).getNickname(),"male"));
        for (TextChannel channel : categoryAdventure.getTextChannels()) {
            channel.delete().queue();
        }
        for (TextChannel channel : categorySettlement.getTextChannels()) {
            for (PermissionOverride perm : channel.getMemberPermissionOverrides()) {
                perm.delete().queue();
            }
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

        Scanner scan = new Scanner(System.in);

        int playerNum;
        int enemyNum;

        System.out.print("How many people are going to play? ");
        playerNum = scan.nextInt();
        scan.reset();
        System.out.println();

        System.out.print("How many goblins do you want to fight? ");
        enemyNum = scan.nextInt();
        scan.reset();
        System.out.println();

        Player[] players = new Player[playerNum];
        Enemy[] enemies = new Enemy[enemyNum];


        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("PLAYER" + i);
        }

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Goblin();
        }

        //System.out.println(BattleSystem.getTurnOrder(enemies, players));

        //BattleSystem.activeCombat(players, enemies);

    }
}
