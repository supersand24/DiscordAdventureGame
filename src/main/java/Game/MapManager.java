package Game;

/**
 * This class will do everything we need for the map.
 * @author Justin Sandman
 */
public class MapManager {

    /**
     * The map size.
     */
    public static final int MAP_SIZE = 7;

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
    }

    /**
     * Creates a grid.
     */
    private static Area[][] map = new Area[MAP_SIZE][MAP_SIZE];

    /**
     * Temporary method testing method.
     */
    public static void main(String[] args) {

        //Game Starts
        Area a = new Area("Noctori");
        addArea(MAP_SIZE/2+1,MAP_SIZE/2+1,a);

        Area b = new Area("Route");
        addAdjacentArea(a,Direction.WEST,b);

        printMap();

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

    public static Area getArea(long channelid) {

        for (Area[] inner : map) {
            for (Area area : inner) {
                if (area != null) {
                    if (area.getChannelId() == channelid)
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
                    switch (map[j][i].getName()) {
                        case "Route" -> stringBuilder.append("[R]");
                        case "Noctori" -> stringBuilder.append("[N]");
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
