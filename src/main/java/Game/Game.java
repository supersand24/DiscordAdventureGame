package Game;

import Game.Entities.EnemyTypes.*;
import Game.Entities.Player;
import Game.Items.Item;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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

    static List<Player> players = new ArrayList<>();
    static List<Party> parties = new ArrayList<>();

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
     * Opens an invite for other players to join an adventure.
     * Is called by a Member in the server, only while in a settlement, and not in another party.
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
        parties.get(0).enemies.add(new Dragon());
        parties.get(0).enemies.add(new Goblin());
        parties.get(0).enemies.add(new Rat());
        textChannel.sendMessage("A battle occurs, the enemies died.").queue();
        //PLACE BattleHandler here.
        //Potentially get a list of dead people from BattleHandler
        for (Enemy en : parties.get(0).enemies) {
            Collections.addAll(parties.get(0).loot, en.getInventory());
        }
        parties.get(0).enemies.clear();
        textChannel.sendMessage("Take time to heal up, when ready cast a group vote on what to do next.").queue();
    }

    /**
     * Goes through all the channels under the adventure category,
     * and finds the one channel that the member is present in.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     * @param member The member to check for.
     * @return The text channel that the member is in.
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

    /**
     * Simple check to make sure the game is active, and the player is picked up by the Game.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
     *
     */
    private static boolean canPlayGame(Member member) {
        return (member.getRoles().contains(roleAdventurer) && gameStarted);
    }

    /**
     * Sends a message to the testing channel.
     *
     * @author Justin Sandman
     * Written : October 17, 2021
     *
     */
    private static void sendMessage(String msg) {
        //Send a message to the test channel.
        guild.getTextChannelById(899417703486455848L).sendMessage(msg).queue();
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
            System.out.println("File not found...");
            e.printStackTrace();
            return false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //CHECK IF THE PARTY CHANNEL STILL EXISTS, JUST IN CASE ITS OLD DATA



        gameStarted = true;

        //No errors occurred, game is set up properly.
        return true;

    }

    /**
     * Saves all the lists needed for the game to run properly.
     *
     * @author Justin Sandman
     * Written : October 18, 2021
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
     * A simple check to see if there are any enemies alive.
     *
     * @author Harrison Brown
     * @param enemies an array of enemies
     * @return returns true if an enemy in the array is alive
     */
    private static boolean enemiesLive (Enemy[] enemies)
    {
        int alive = enemies.length;
        for (int i = 0; i < enemies.length; i++)
        {
            if (!enemies[i].getIsAlive())
            {
                alive -= 1;
            }
        }
        if (alive == 0)
        {
            return false;
        }
        else
        {
            return true;
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
        /*
        Scanner scan = new Scanner(System.in);

        Player harrison = new Player(100, 50, "Slayer of Thots", "Harrison", "Harrison", "Male");
        Enemy[] enemies = new Enemy[2];
        enemies[0] = new Goblin();
        enemies[1] = new Goblin();

        do {

            for (Enemy x : enemies) {
                System.out.print(x.getHealth() + " | ");
                x.attack(harrison);
            }
            System.out.println();
            System.out.println("Which Goblin do you want to attack? (0 or 1) ");
            int choice = scan.nextInt();
            scan.reset();
            harrison.attack(enemies[choice]);
            for (Enemy x : enemies) {
                x.checkHealthStatus();
            }
            harrison.checkHealthStatus();

        } while (enemiesLive(enemies) && harrison.getIsAlive());
        if (harrison.getIsAlive()) {
            System.out.println("You Win!");
        } else {
            System.out.println("Enemies Win!");
        }
         */

        Player harrison = new Player(100, 50, "Slayer of Thots", "Harrison", "Harrison", "Male");
        System.out.print(harrison);
    }
}
