package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Player;
import Game.Items.Item;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An object that is stored in a list.  It keeps track of one Party.
 *
 * @author Justin Sandman
 * @version 0.2
 *
 */
public class Party implements Serializable {

    long channelId;

    List<Enemy> enemies = new ArrayList<>();
    List<Item> loot = new ArrayList<>();

    public Party(long id) {
        this.channelId = id;
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
     *
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
            playerList.add( getPlayer(member) );
        }
        return playerList;
    }

    /**
     *
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

}
