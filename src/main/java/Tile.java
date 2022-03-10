public class Tile {

    private final int type;
    private boolean clicked;

    private int mines;

    public static final int EMPTY = 0;
    public static final int MINE = 1;

    public void setMines(int mines) {
        this.mines = mines;
    }

    public Integer getMineCount() {
        return this.mines;
    }

    public Tile(int type) {
        this.type = type;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void click() {
        if (this.isClicked()) {
            throw new RuntimeException();
        }
        clicked = true;
    }

    public int getType() {
        return type;
    }

}
