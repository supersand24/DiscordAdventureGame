package Game;

public class Area {

    private String name;

    private int[] coords;

    public Area(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }

    public void setCoords(int x, int y) {
        this.coords = new int[]{x, y};
    }
}
