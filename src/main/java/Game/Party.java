package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Items.Item;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Party implements Serializable {

    long channelId;

    List<Enemy> enemies = new ArrayList<>();
    List<Item> loot = new ArrayList<>();

    public Party(long id) {
        this.channelId = id;
    }

    public List<Member> getMembers(Guild guild) {
        return guild.getTextChannelById(channelId).getMembers();
    }

    @Override
    public String toString() {
        return "Party{" +
                "channelId=" + channelId +
                '}';
    }
}
