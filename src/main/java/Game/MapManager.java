package Game;

public class MapManager {

    private static final int MAP_SIZE = 25;

    //Creates a grid
    private static Area[][] map = new Area[MAP_SIZE][MAP_SIZE];

    public static void main(String[] args) {

        setArea(3,3,new Area("Noctori"));
        setArea(2,3,new Area("Route"));

        printMap();

    }

    public static Area getArea(int x, int y) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) ) {
            return map[x - 1][y - 1];
        }
        return null;
    }

    public static void setArea(int x, int y, Area area) {
        if ( (x > 0 || y > 0) && (x < MAP_SIZE || y < MAP_SIZE) && area != null) {
            map[x - 1][y - 1] = area;
        }
    }

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
