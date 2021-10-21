package Game;

/**
 * This class will do everything we need for the map.
 * @author Justin Sandman
 */
public class MapManager {

    /**
     * The map size.
     */
    private static final int MAP_SIZE = 25;

    /**
     * Simple cardinal directions.
     */
    enum Direction {
        NORTH_WEST,
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST
    }


    /**
     * Creates a grid.
     */
    private static Area[][] map = new Area[MAP_SIZE][MAP_SIZE];

    /**
     * Temporary method testing method.
     */
    public static void main(String[] args) {

        setArea(3,3,new Area("Noctori"));
        setArea(2,3,new Area("Route"));

        printMap();

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
    public static void setArea(int x, int y, Area area) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) && area != null) {
            map[x - 1][y - 1] = area;
        }
    }

    /**
     * Testing method, prints to the console.
     * May use for drawing maps for players.
     */
    private static void printMap() {
        for (int i = 0; i < map.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < map.length; j++) {

                if (map[j][i] != null) {
                    stringBuilder.append("[X]");
                } else {
                    stringBuilder.append("[ ]");
                }

            }
            System.out.println(stringBuilder);
        }
    }
}
