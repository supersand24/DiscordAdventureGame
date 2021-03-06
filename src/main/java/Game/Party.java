package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

/**
 * This object keeps track of a party, it is able to keep track of its members, map location, and voting information.
 *
 * @author Justin Sandman
 * @version 1.1
 *
 */
public class Party {

    /**
     * The party text channel, where messages go.
     */
    private final TextChannel channel;

    /**
     * The party leader, has final say, most of the time.
     */
    private final Member leader;

    /**
     * List of players.
     */
    private final List<Player> players = new ArrayList<>();

    /**
     * The current encounter of the party.
     */
    private Encounters.EncounterType currentEvent = null;

    /**
     * List of enemies the party is currently facing.
     */
    public List<Enemy> enemies = new ArrayList<>();

    private int tilesMovedWithNoSpecialEvent = 0;

    /**
     * The party's, current location on the map.
     */
    private Area location;

    /**
     * The party's previous areas, works with heading back.
     */
    public List<Area> previousAreas = new ArrayList<>();

    /**
     * The party's facing direction, the map generation prefers this direction when generating areas.
     */
    private MapManager.Direction goingTo = null;

    /**
     * Lists storing all entities in the battle, sorted by their speed stats.
     */
    private Entity[] turnOrder = null;

    /**
     * Index of the parties current turn.
     */
    private int turnIndex = 0;

    /**
     * The message that was sent, shows the battle.
     */
    private Message battleMessage = null;

    /**
     * The message that was sent, so members can vote.
     */
    private Message voteMessage = null;

    /**
     * Keeps track of all the votes and how many each one has.
     */
    public HashMap<Game.Vote, Integer> vote = new HashMap<>();

    /**
     * List of members that have already voted, prevents double voting.
     */
    public List<Member> hasVoted = new ArrayList<>();

    /**
     * @author Justin Sandman
     */
    public Party(TextChannel channel, Member partyLeader, Area location) {
        this.channel = channel;
        this.leader = partyLeader;
        this.location = location;
        players.add(Game.players.get(partyLeader));
        this.goingTo = MapManager.Direction.NORTH;
    }

    public boolean joinParty(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            Member member = player.getMember();
            channel.sendMessage(member.getAsMention() + " has joined the Party!").queue();
            channel.createPermissionOverride(member)
                    .setAllow(Permission.VIEW_CHANNEL)
                    .queue();
            return true;
        } else {
            return false;
        }
    }

    public boolean leaveParty(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            player.setParty(null);
            Member member = player.getMember();
            channel.sendMessage(member.getAsMention() + "has left the Party.").queue();
            channel.getPermissionOverride(member).delete().queue();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Moves the party forwards, generating new paths or using existing ones.
     * @author Justin Sandman
     */
    public void continueOn() {
        voteMessage.editMessage("The party voted, to continue on!").queue();

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

            //use previousAreas.size number indicates how far party is on path
            //gets larger they've been on path, use to force settlement odds

            //random pick area type to use

            Area area = new Area(MapManager.setAreaType(this),this);

            switch (area.getAreaType()) {
                case SETTLEMENT     -> getChannel().sendMessage("@everyone You found " + area.getName() + "!").queue();
                case PATH           -> getChannel().sendMessage("The path continues on.").queue();
            }
            MapManager.addAdjacentArea(location,possibleDirections.get(headingDirection),area);

        } else {
            System.out.println("Continuing on path.");
            possibleDirections = location.getOtherConnections(goingTo.getOpposite());
            headingDirection = 0;
            //have a vote in here, if multiple paths
        }
        previousAreas.add(getLocation());
        setLocation(MapManager.getAdjacentArea(location,possibleDirections.get(headingDirection)));
        goingTo = possibleDirections.get(headingDirection);
        //CHECK FOR MULTIPLE PATHS!!!
        channel.sendMessage("The party headed " + possibleDirections.get(headingDirection).getName() + ".").queue();
    }

    /**
     * Moves the party backwards to previously visited areas.
     * @author Justin Sandman
     */
    public void headBack() {
        voteMessage.editMessage("The party voted, to go back.").queue();
        setLocation(previousAreas.get(previousAreas.size()-1));
        previousAreas.remove(getLocation());
        System.out.println(location.getXCoord() + "," + location.getYCoord() + "[" + location.getName() + "] ");
    }

    /**
     * Sends/updates the battle message in Discord.
     * @author Justin Sandman
     */
    public void sendBattleMessage() {

        //If the party channel exists.
        if (channel != null) {

            EmbedBuilder embed = new EmbedBuilder();

            //Turn
            embed.setTitle(turnOrder[turnIndex].getName() + "'s Turn!");

            //Fields
            embed.addField("Turn Order", getTurnOrderAsString(), false);
            //Get all player stats
            for (Player player : getPlayers()) {
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

    public TextChannel getChannel() {
        return channel;
    }

    /**
     * @author Justin Sandman
     * @return The party leader.
     */
    public Member getLeader() {
        return leader;
    }

    /**
     * @author Justin Sandman
     * @return The event that the party is participating in.
     */
    public Encounters.EncounterType getCurrentEvent() {
        return currentEvent;
    }

    /**
     * @author Justin Sandman
     * @return The list of enemies in front of the party.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * @author Justin Sandman
     * @return The area the party is at.
     */
    public Area getLocation() {
        return location;
    }

    /**
     * @author Justin Sandman
     * @return The list of previous areas.
     */
    public List<Area> getPreviousAreas() {
        return previousAreas;
    }

    /**
     * @author Justin Sandman
     * @return The direction the party is facing.
     */
    public MapManager.Direction getGoingTo() {
        return goingTo;
    }

    /**
     * @author Harrison Brown
     * @return Turn order as an Array.
     */
    public Entity[] getTurnOrder() {
        return turnOrder;
    }

    /**
     * @author Harrison Brown
     * @author Justin Sandman
     * @return Turn Order as a String.
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
     * getter for turnIndex
     * @author Harrison Brown
     * @return A number used for in the turn.
     */
    public int getTurnIndex() {
        return turnIndex;
    }

    public Message getBattleMessage() {
        return battleMessage;
    }

    public Message getVoteMessage() {
        return voteMessage;
    }

    public HashMap<Game.Vote, Integer> getVote() {
        return vote;
    }

    public List<Member> getHasVoted() {
        return hasVoted;
    }

    /**
     * @author Justin Sandman
     * @return A list of all the players in the party.
     */
    public List<Player> getPlayers() {
        return players;
    }

    public void setCurrentEvent(Encounters.EncounterType currentEvent) {
        this.currentEvent = currentEvent;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setLocation(Area location) {
        this.location = location;
        int random = new Random().nextInt(location.getPossibleEncounters().size());
        setCurrentEvent(location.getPossibleEncounters().get(random));
        Encounters.generateEncounter(this);
    }

    public void setGoingTo(MapManager.Direction goingTo) {
        this.goingTo = goingTo;
    }

    /**
     * @author Harrison Brown
     * @param e The array to set the turn order.
     */
    public void setTurnOrder(Entity[] e) {
        turnOrder = e;
    }

    /**
     * @author Harrison Brown
     * @param turnIndex the new value
     */
    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public void setBattleMessage(Message message) {
        battleMessage = message;
    }

    public void setVoteMessage(Message voteMessage) {
        this.voteMessage = voteMessage;
    }

    public void setVote(HashMap<Game.Vote, Integer> vote) {
        this.vote = vote;
    }

    public void setHasVoted(List<Member> hasVoted) {
        this.hasVoted = hasVoted;
    }

    public String saveString() {
        StringBuilder string = new StringBuilder();
        string.append("ID:").append(getChannel().getId()).append("\n");
        for (Member member : getChannel().getMembers()) {
            if (member.getRoles().contains(Game.roleAdventurer)) {
                if (member.equals(getLeader())) {
                    string.append("Leader:").append(member.getId()).append("\n");
                } else {
                    string.append("Player:").append(member.getId()).append("\n");
                }
            }
        }
        string.append("Location:").append(getLocation().getXCoord()).append(',').append(getLocation().getYCoord()).append("\n");
        string.append("GoingTo:").append(getGoingTo()).append("\n");
        return string.toString();
    }

    public void setTilesMovedWithNoSpecialEvent(int tilesMovedWithNoSpecialEvent) {
        this.tilesMovedWithNoSpecialEvent = tilesMovedWithNoSpecialEvent;
    }

    public int getTilesMovedWithNoSpecialEvent() {
        return tilesMovedWithNoSpecialEvent;
    }

    @Override
    public String toString() {
        return "Party{" +
                "leader=" + leader.getEffectiveName() +
                ", location=" + location +
                ", goingTo=" + goingTo +
                ", currentEvent=" + currentEvent +
                ", previousAreas=" + previousAreas +
                '}';
    }
}
