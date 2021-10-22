package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable {

    private String name;

    private long channelId;

    private int[] coords;

    private List<Encounters.EncounterType> possibleEncounters = new ArrayList<>();
    private Area[] connections = new Area[MapManager.Direction.values().length];

    public Area(String name) {
        this.name = name;
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

    public Area[] getConnections() {
        return connections;
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
}
