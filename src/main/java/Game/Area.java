package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Area implements Serializable {

    private String name;

    private long channelId;

    private int[] coords;

    private List<Encounters.EncounterType> possibleEncounters = new ArrayList<>();

    private final int connectionAmount;
    private Area[] connections = new Area[MapManager.Direction.values().length];

    public Area(String name, int allowedConnections) {
        this.name = name;
        this.connectionAmount = allowedConnections;
        possibleEncounters.add(Encounters.EncounterType.NONE);
        possibleEncounters.add(Encounters.EncounterType.BATTLE);
    }

    public String getName() {
        return name;
    }

    public long getChannelId() {
        return channelId;
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

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", coords=" + Arrays.toString(coords) +
                ", connectionAmount=" + connectionAmount +
                '}';
    }
}
