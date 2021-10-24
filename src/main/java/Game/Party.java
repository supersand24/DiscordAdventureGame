package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;
import Game.Items.Item;
import Game.MapManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.Serializable;
import java.util.*;

/**
 * An object that is stored in a list.  It keeps track of one Party.
 *
 * @author Justin Sandman
 * @version 0.2
 *
 */
public class Party implements Serializable {

    long channelId;

    private final Member leader;

    List<Enemy> enemies = new ArrayList<>();
    List<Item> loot = new ArrayList<>();

    Area location = null;
    @Deprecated
    MapManager.Direction comingFrom = null;
    List<Area> previousAreas = new ArrayList<Area>();
    MapManager.Direction goingTo = null;

    private Entity[] turnOrder = null;
    private int turnIndex = 0;
    public Message battleMessage = null;

    public Message voteMessage = null;
    public HashMap<Game.Vote, Integer> vote = new HashMap<>();
    public List<Member> hasVoted = new ArrayList<>();

    public Party(long id, Member member, Area location) {
        this.channelId = id;
        this.leader = member;
        this.location = location;
    }

    private Encounters.EncounterType crntEvent = null;

    public void continueOn() {
        voteMessage.editMessage("The party voted, to continue on!").queue();
        System.out.println(location.getXCoord() + "," + location.getYCoord() + "[" + location.getName() + "] ");

        List<MapManager.Direction> possibleDirections;
        int headingDirection = 0;
        if (location.canGenerateAdjacentPaths()) {
            System.out.println("Generating new path.");
            possibleDirections = MapManager.getEmptySpaces(location);

            //If the direction the party is heading, is empty increase its chances.
            if (possibleDirections.contains(goingTo)) {
                possibleDirections.add(goingTo);
                possibleDirections.add(goingTo);
                possibleDirections.add(goingTo);
            }
            for (MapManager.Direction dir : goingTo.getNearbys()) {
                if (possibleDirections.contains(dir)) {
                    possibleDirections.add(dir);
                    possibleDirections.add(dir);
                }
            }

            headingDirection = new Random().nextInt(possibleDirections.size());
            //THIS IS WHERE SETTLEMENTS AND DUNGEONS ARE GENERATED.
            MapManager.addAdjacentArea(location,possibleDirections.get(headingDirection),new Area(MapManager.AreaType.PATH));

        } else {
            System.out.println("Continuing on path.");
            possibleDirections = location.getOtherConnections(comingFrom);
            headingDirection = 0;
            //have a vote in here, if multiple paths
        }
        //System.out.println(possibleDirections);
        previousAreas.add(getLocation());
        setLocation(MapManager.getAdjacentArea(location,possibleDirections.get(headingDirection)));
        goingTo = possibleDirections.get(headingDirection);
        //CHECK FOR MULTIPLE PATHS!!!
        comingFrom = location.getOtherConnections(goingTo).get(0);
        System.out.println(location.getOtherConnections(goingTo));
        //setLocation(MapManager.getAdjacentArea(location,possibleDirections.get(headingDirection)));
        Game.guild.getTextChannelById(channelId).sendMessage("The party headed " + possibleDirections.get(headingDirection).getName() + ".").queue();
    }

    public void headBack() {
        voteMessage.editMessage("The party voted, to go back.").queue();
        //setLocation(location.getConnections()[comingFrom.getIndex()]);
        setLocation(previousAreas.get(previousAreas.size()-1));
        previousAreas.remove(getLocation());
        System.out.println(location.getXCoord() + "," + location.getYCoord() + "[" + location.getName() + "] ");
    }

    public void sendBattleMessage() {

        TextChannel channel = Game.guild.getTextChannelById(channelId);

        //If the party channel exists.
        if (channel != null) {

            EmbedBuilder embed = new EmbedBuilder();

            //Turn
            embed.setTitle(turnOrder[turnIndex].getName() + "'s Turn!");

            //Fields
            embed.addField("Turn Order", getTurnOrderAsString(), false);
            //Get all player stats
            for (Player player : getPlayers(Game.guild)) {
                embed.addField(
                        "PLAYER\n" + player.getName(),
                        player.getHealth() + "/" + player.getMaxHealth() + " HP",
                        true);
            }

            //Get all enemy stats
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                embed.addField(
                        "Target #" + (i + 1) + "\n" + enemy.getName(),
                        enemy.getHealth() + "/" + enemy.getMaxHealth() + " HP",
                        true
                );
            }

            //If there is an existing message, edit, if not send a new one.
            if (battleMessage == null) {
                channel.sendMessageEmbeds(embed.build()).queue(this::setBattleMessage);
            } else {
                battleMessage.editMessageEmbeds(embed.build()).queue();
            }
        }
    }

    public long getChannelId() {
        return channelId;
    }

    public Member getLeader() {
        return leader;
    }

    public Area getLocation() {
        return location;
    }

    public MapManager.Direction getComingFrom() {
        return comingFrom;
    }

    public MapManager.Direction getGoingTo() {
        return goingTo;
    }

    /**
     * Goes through the whole player list and finds a match, using member nickname.
     *
     * @author Justin Sandman
     * Written : October 19, 2021
     * @param member The member we need to compare with players.
     *
     */
    public Player getPlayer(Member member) {
        if (member.getNickname() != null) {
            for (Player player : Game.players) {
                if (member.getNickname().equals(player.getName())) {
                    return player;
                }
            }
        }
        return null;
    }

    /**
     * Makes a whole list of Players, that are in the party.
     * Works side by side with getPlayer method.
     *
     * @author Justin Sandman
     * Written : October 19, 2021
     * @param guild The guild of the Discord Server.
     *
     */
    public List<Player> getPlayers(Guild guild) {
        List<Player> playerList = new ArrayList<>();
        for (Member member : getMembers(guild)) {
            if (member.getNickname() != null && member.getRoles().contains(Game.roleAdventurer)) {
                for (Player player : Game.players) {
                    if (member.getNickname().equals(player.getName())) {
                        playerList.add(player);
                        break;
                    }
                }
            }
        }
        return playerList;
    }

    /**
     * Obtains the member objects from everyone that can view the party channel.
     *
     * @author Justin Sandman
     * Written : October 19, 2021
     * @param guild The guild of the Discord Server.
     *
     */
    public List<Member> getMembers(Guild guild) {
        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel != null) {
            return channel.getMembers();
        } else {
            return null;
        }
    }

    /**
     * returns the encounter turn order as a string
     * @author Harrison Brown
     * @return string of turn order
     */
    public String getTurnOrderAsString() {
        if (turnOrder == null) {
            return "There is no turn order";
        } else {
            int cnt = 1;
            StringBuilder turns = new StringBuilder();
            for (Entity e : turnOrder) {
                if (e.getIsAlive()) {

                    if (turnOrder[turnIndex].equals(e))
                        turns.append(" > ");

                    switch (cnt) {
                        case 1 -> turns.append("**1st**");
                        case 2 -> turns.append("**2nd**");
                        case 3 -> turns.append("**3rd**");
                        default -> turns.append("**").append(cnt).append("th**");
                    }

                    turns.append(" ").append(e.getName());

                    if (e.getLastAction() != null) {
                        turns.append(" *").append(e.getLastAction()).append('*');
                    }

                    turns.append("\n");
                    cnt++;
                }
            }

            return turns.toString();
        }


    }

    /**
     * @author Harrison Brown
     * @return returns the turn order
     */
    public Entity[] getTurnOrder() {
        return turnOrder;
    }

    /**
     * sets the turn order
     * @author Harrison Brown
     * @param e the array to set the turn order
     */
    public void setTurnOrder(Entity[] e) {
        turnOrder = e;
    }

    /**
     * getter for turnIndex
     * @author Harrison Brown
     * @return returns turnIndex
     */
    public int getTurnIndex() {
        return turnIndex;
    }

    /**
     * setter for turnIndex
     * @author Harrison Brown
     * @param turnIndex the new value
     */
    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    private void setBattleMessage(Message message) {
        battleMessage = message;
    }

    /**
     * setter for nextEncounter
     * @author Harrison Brown
     * @param currentEvent the next encounter for the party
     */
    public void setCurrentEncounter(Encounters.EncounterType currentEvent) {
        this.crntEvent = currentEvent;
    }

    /**
     * getter for the party's next encounter
     * @author Harrison Brown
     * @return returns the nextEncounter
     */
    public Encounters.EncounterType getCurrentEncounter() {
        return crntEvent;
    }

    public void setLocation(Area location) {
        this.location = location;
        int random = new Random().nextInt(location.getPossibleEncounters().size());
        setCurrentEncounter(location.getPossibleEncounters().get(random));
        Encounters.generateEncounter(this);
    }

    public void setComingFrom(MapManager.Direction comingFrom) {
        this.comingFrom = comingFrom;
    }

    public void setGoingTo(MapManager.Direction goingTo) {
        this.goingTo = goingTo;
    }

    @Override
    public String toString() {
        return "Party{" +
                "leader=" + leader.getEffectiveName() +
                ", location=" + location +
                ", comingFrom=" + comingFrom +
                ", goingTo=" + goingTo +
                ", crntEvent=" + crntEvent +
                ", previousAreas=" + previousAreas +
                '}';
    }
}
