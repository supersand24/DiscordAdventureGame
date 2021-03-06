package Game;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * This class will do everything we need for the map.
 * @author Justin Sandman
 */
public class MapManager {

    /**
     * The map size.
     */
    public static final int MAP_SIZE = 15;

    /**
     * Creates a grid.
     */
    private static Area[][] map = new Area[MAP_SIZE][MAP_SIZE];

    public static List<Area> areas = new ArrayList<>();

    public static Hashtable<TextChannel,Area> areas2 = new Hashtable<>();

    /**
     * Simple cardinal directions.
     */
    public enum Direction {
        NORTH_WEST("Northwest"),
        NORTH("North"),
        NORTH_EAST("Northeast"),
        EAST("East"),
        SOUTH_EAST("Southeast"),
        SOUTH("South"),
        SOUTH_WEST("Southwest"),
        WEST("West");

        String name;

        Direction(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            for (int i = 0; i < Direction.values().length; i++) {
                if (Direction.values()[i].equals(this)) {
                    return i;
                }
            }
            return 0;
        }

        Direction getOpposite() {
            Direction dir = EAST;
            switch (this) {
                case NORTH_WEST -> dir = SOUTH_EAST;
                case NORTH -> dir = SOUTH;
                case NORTH_EAST -> dir = SOUTH_WEST;
                case EAST -> dir = WEST;
                case SOUTH_EAST -> dir = NORTH_WEST;
                case SOUTH -> dir = NORTH;
                case SOUTH_WEST -> dir = NORTH_EAST;
            }
            return dir;
        }

        List<Direction> getNearbys() {
            List<Direction> directions = null;
            switch (this) {
                case NORTH_WEST -> directions = List.of(new Direction[]{WEST, NORTH});
                case NORTH -> directions = List.of(new Direction[]{NORTH_WEST, NORTH_EAST});
                case NORTH_EAST -> directions = List.of(new Direction[]{NORTH, EAST});
                case EAST -> directions = List.of(new Direction[]{NORTH_EAST, SOUTH_EAST});
                case SOUTH_EAST -> directions = List.of(new Direction[]{EAST, SOUTH});
                case SOUTH -> directions = List.of(new Direction[]{SOUTH_EAST, SOUTH_WEST});
                case SOUTH_WEST -> directions = List.of(new Direction[]{SOUTH, WEST});
                case WEST -> directions = List.of(new Direction[]{NORTH_WEST, SOUTH_WEST});
            }
            return directions;
        }
    }

    public enum AreaType {
        SETTLEMENT(0),
        PATH(10);

        int weight;

        AreaType(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

    }

    public static AreaType setAreaType(Party party) {
        //need to swap from previousAreas.size() to party.getTilesMovedWithNoSpecialEvent()
        if (party.previousAreas.size() >= 5) {
            for (AreaType type : AreaType.values()) {
                if (!type.equals(AreaType.SETTLEMENT)) {
                    type.weight = 0;
                }
            }
        } else if (party.previousAreas.size() > 3) {
            AreaType.SETTLEMENT.weight = (party.previousAreas.size() - 3) * 2;
        }

        Random rand = new Random();
        AreaType typeToReturn = null;
        int max = AreaType.values().length;
        while (typeToReturn == null) {
            int sum = 0;
            for (int i = 0; i < max; i++) {
                sum += AreaType.values()[i].weight;
            }

            int x = rand.nextInt(sum);
            if (x > (sum - (AreaType.values()[(max - 1)].weight))) {
                typeToReturn = AreaType.values()[(max - 1)];
            } else {
                if ((max-1) == 0) {
                    max = AreaType.values().length;
                } else {
                    max--;
                }
            }
        }
        return typeToReturn;
    }

    public static void linkAreas(Area from, Direction dir, Area to) {
        from.setConnection(to,dir);
        to.setConnection(from,dir.getOpposite());
    }

    public static void addAdjacentArea(Area oldArea, Direction dir, Area newArea) {
        int x = oldArea.getXCoord();
        int y = oldArea.getYCoord();
        if (getAdjacentArea(x,y,dir) == null) {
            linkAreas(oldArea,dir,newArea);
            switch (dir) {
                case NORTH_WEST -> addArea(x -1 , y - 1,newArea);
                case NORTH -> addArea(x , y - 1,newArea);
                case NORTH_EAST -> addArea(x +1 , y - 1,newArea);
                case EAST -> addArea(x +1 , y ,newArea);
                case SOUTH_EAST -> addArea(x +1 , y + 1,newArea);
                case SOUTH -> addArea(x , y + 1,newArea);
                case SOUTH_WEST -> addArea(x -1 , y + 1,newArea);
                case WEST -> addArea(x -1 , y ,newArea);
            }
        } else {
            System.out.println("There is already an existing area there.");
        }
    }

    public static List<Direction> getEmptySpaces(Area area) {
        List<Direction> directionList = new ArrayList<>(Direction.values().length);
        for (Direction dir : Direction.values()) {
            if (getAdjacentArea(area, dir) == null) {
                directionList.add(dir);
            }
        }
        return directionList;
    }

    /**
     * Gets nearby area.
     * @param area X&Y coordinates.
     * @param dir The cardinal direction to search in.
     * @return The area next to area.
     */
    public static Area getAdjacentArea(Area area, Direction dir) {
        return getAdjacentArea(area.getXCoord(),area.getYCoord(),dir);
    }

    /**
     * Gets nearby area.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param dir The cardinal direction to search in.
     * @return The area next to coordinate.
     */
    public static Area getAdjacentArea(int x, int y, Direction dir) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) ) {
            switch (dir) {
                case NORTH_WEST -> {return getArea(x -1 , y - 1);}
                case NORTH -> {return getArea(x , y - 1);}
                case NORTH_EAST -> {return getArea(x +1 , y - 1);}
                case EAST -> {return getArea(x +1 , y );}
                case SOUTH_EAST -> {return getArea(x +1 , y + 1);}
                case SOUTH -> {return getArea(x , y + 1);}
                case SOUTH_WEST -> {return getArea(x -1 , y + 1);}
                case WEST -> {return getArea(x -1 , y );}
            }
        }
        System.out.println("getAdjacentArea Tried to grab a map out of bounds!");
        return null;
    }

    public static Area getArea(TextChannel channel) {

        for (Area[] inner : map) {
            for (Area area : inner) {
                if (area != null) {
                    if (area.getChannel().equals(channel))
                        return area;
                }
            }
        }
        return null;

    }

    /**
     * Gets area at coordinates, if there is one.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return The area at the coordinates given.
     */
    public static Area getArea(int x, int y) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) ) {
            return map[x - 1][y - 1];
        }
        System.out.println("getArea Tried to grab a map out of bounds!");
        return null;
    }

    /**
     * Marks a coordinate to be at an area.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param area The new area that needs to be registered.
     */
    public static void addArea(int x, int y, Area area) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) && area != null) {
            map[x - 1][y - 1] = area;
            area.setCoords(x,y);
            areas.add(area);
        }
    }

    /**
     * Testing method, prints to the console.
     * May use for drawing maps for players.
     */
    public static String printMap() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[j][i] != null) {
                    switch (map[j][i].getAreaType()) {
                        case PATH -> stringBuilder.append("[R]");
                        case SETTLEMENT -> stringBuilder.append("[").append(map[j][i].getName().charAt(0)).append("]");
                        default -> stringBuilder.append("[ ]");
                    }
                } else {
                    stringBuilder.append("[ ]");
                }

            }
            stringBuilder.append("\n");

        }
        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }
}
