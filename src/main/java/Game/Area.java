package Game;

import Game.Entities.Player;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.Serializable;
import java.util.*;

public class Area implements Serializable {

    private String name;

    private long channelId;

    private TextChannel channel;

    public List<Party> parties = new ArrayList<>();

    private int[] coords;

    private MapManager.AreaType areaType;

    private List<Encounters.EncounterType> possibleEncounters = new ArrayList<>();

    private int connectionAmount;
    private Area[] connections = new Area[MapManager.Direction.values().length];

    public Area(MapManager.AreaType areaType, Party party) {
        this.areaType = areaType;
        switch (areaType) {
            case SETTLEMENT -> {
                this.name = generateSettlementName();
                this.connectionAmount = 6;
                possibleEncounters.add(Encounters.EncounterType.RETURN_TO_SETTLEMENT);
                Game.categorySettlement.createTextChannel(name).queue(channel1 -> {
                    setChannel(channel1);
                    for (Member member : party.getChannel().getMembers()) {
                        if (member.getRoles().contains(Game.roleAdventurer)) {
                            channel1.createPermissionOverride(member).setAllow(
                                    Permission.VIEW_CHANNEL
                            ).queue();
                        }
                    }
                });
            }
            case PATH -> {
                this.name = "Route";
                this.connectionAmount = 2;
                possibleEncounters.add(Encounters.EncounterType.NONE);
                possibleEncounters.add(Encounters.EncounterType.BATTLE);
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getChannelId() {
        return channelId;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public int[] getCoords() {
        return coords;
    }

    public int getXCoord() {
        return coords[0];
    }

    public int getYCoord() {
        return coords[1];
    }

    public List<Encounters.EncounterType> getPossibleEncounters() {
        return possibleEncounters;
    }

    public int getConnectionAmount() {
        return connectionAmount;
    }

    public Area[] getConnections() {
        return connections;
    }

    public List<MapManager.Direction> getOtherConnections(MapManager.Direction dir) {
        List<MapManager.Direction> connections = new ArrayList<>(MapManager.Direction.values().length -1);
        for (MapManager.Direction direction : MapManager.Direction.values()) {
            if (!direction.equals(dir)) {
                if (getConnections()[direction.getIndex()] != null)
                    connections.add(direction);
            }
        }
        System.out.println(connections);
        return connections;
    }

    public boolean canGenerateAdjacentPaths() {
        int count = 0;
        for (Area area : connections) {
            if (area != null)
                count++;
        }
        return count < getConnectionAmount();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }

    public void setCoords(int x, int y) {
        this.coords = new int[]{x, y};
    }

    public void setPossibleEncounters(List<Encounters.EncounterType> possibleEncounters) {
        this.possibleEncounters = possibleEncounters;
    }

    public void setConnection(Area area, MapManager.Direction dir) {
        this.connections[dir.getIndex()] = area;
    }

    public String saveString() {
        StringBuilder string = new StringBuilder();
        if (getChannel() != null) {
            string.append("ID:").append(getChannel().getId()).append("\n");
        }
        string.append("Name:").append(getName()).append("\n");
        for (MapManager.Direction dir : MapManager.Direction.values()) {
            Area area = connections[dir.getIndex()];
            if (area != null) {
                string.append("Connection:").append(dir).append("\n");
            }
        }

        return string.toString();
    }

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", coords=" + Arrays.toString(coords) +
                ", connectionAmount=" + connectionAmount +
                '}';
    }

    private String generateSettlementName() {
        Random rand = new Random();
        String name = null;
        int i = 0;
        while (name == null) {
            i = rand.nextInt(possibleNames.length);
            name = possibleNames[i];
        }
        possibleNames[i] = null;
        return name + setSettlementType();
    }

    private String setSettlementType() {
        Random rand = new Random();
        return settlementType[rand.nextInt(settlementType.length)];
    }

    public MapManager.AreaType getAreaType() {
        return areaType;
    }

    private static String[] possibleNames = {
            "Noctori",
            "Harmony",
            "Crotion",
            "Monaphos",
            "Aspen",
            "Klingson",
            "Mevania",
            "Dumarma",
            "Nazgord",
            "Nobrul",
            "Horizon",
            "Wingston",
            "Namros",
            "Redemption",
            "Flameridge"
    };

    private String[] settlementType = {
            " Village",
            " Town",
            " City"
    };
}
