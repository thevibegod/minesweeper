import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minesweeper extends PApplet {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int TILE_SIZE = 40;
    public static final int BOARD_START_X = WIDTH / 2 - (TILE_SIZE * ROWS / 2);
    public static final int BOARD_START_Y = HEIGHT / 2 - (TILE_SIZE * COLS / 2);
    public static final int RGB_WHITE = 255;
    public static final int RGB_BLACK = 0;
    public static boolean gameRunning;
    public static int totalEmptySpaces;
    public static int foundEmptySpaces;
    public static final double THRESHOLD = 0.8;
    List<List<Tile>> tiles = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main("Minesweeper", args);
    }

    @Override
    public void settings() {
        super.settings();
        size(WIDTH, HEIGHT);
    }


    @Override
    public void mouseClicked(MouseEvent event) {
        if (!gameRunning) {
            exit();
        }
        int posX = event.getX(), posY = event.getY();
        int j = (posX - BOARD_START_X) / TILE_SIZE;
        int i = (posY - BOARD_START_Y) / TILE_SIZE;
        try {
            Tile tile = this.tiles.get(i).get(j);
            if (tile.getType() == Tile.MINE) {
                endGame();
            } else {
                if (!tile.isClicked()) {
                    flipTile(j, i, tile);
                    tile.click();
                    foundEmptySpaces++;
                    if (foundEmptySpaces == totalEmptySpaces) {
                        displayWinMessage();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    private void flipTile(int j, int i, Tile tile) {
        fill(RGB_BLACK);
        int textXPos = BOARD_START_X + (TILE_SIZE * j) + (TILE_SIZE / 2);
        int textYPos = BOARD_START_Y + (TILE_SIZE * i) + (TILE_SIZE / 2);
        text(tile.getMineCount().toString(), textXPos, textYPos);
    }

    private void displayWinMessage() {
        clear();
        fill(RGB_WHITE);
        text("You won", WIDTH / 2, HEIGHT / 2);
        gameRunning = false;
    }


    private void endGame() {
        clear();
        fill(RGB_WHITE);
        text("You stepped on a mine.Game Over", WIDTH / 3, HEIGHT / 2);
        gameRunning = false;
    }

    public void setMineCountForTiles(List<List<Tile>> tiles) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Tile tile = tiles.get(i).get(j);
                setMineCount(tiles, i, j, tile);
            }
        }
    }

    private void setMineCount(List<List<Tile>> tiles, int i, int j, Tile tile) {
        if (tile.getType() == Tile.EMPTY) {
            int mines = 0;
            if (checkTopLeftTile(tiles, i, j))
                mines++;
            if (checkTopMiddleTile(tiles, i, j))
                mines++;
            if (checkTopRightTile(tiles, i, j))
                mines++;
            if (checkMiddleLeftTile(tiles, i, j))
                mines++;
            if (checkMiddleRightTile(tiles, i, j))
                mines++;
            if (checkBottomLeftTile(tiles, i, j))
                mines++;
            if (checkBottomMiddleTile(tiles, i, j))
                mines++;
            if (checkBottomRightTile(tiles, i, j))
                mines++;
            tile.setMines(mines);
            return;
        }
        tile.setMines(-1);
    }

    private boolean checkBottomRightTile(List<List<Tile>> tiles, int i, int j) {
        return i + 1 < ROWS && j + 1 < COLS && tiles.get(i + 1).get(j + 1).getType() == Tile.MINE;
    }

    private boolean checkBottomMiddleTile(List<List<Tile>> tiles, int i, int j) {
        return i + 1 < ROWS && tiles.get(i + 1).get(j).getType() == Tile.MINE;
    }

    private boolean checkBottomLeftTile(List<List<Tile>> tiles, int i, int j) {
        return i + 1 < ROWS && j - 1 >= 0 && tiles.get(i + 1).get(j - 1).getType() == Tile.MINE;
    }

    private boolean checkMiddleRightTile(List<List<Tile>> tiles, int i, int j) {
        return j + 1 < COLS && tiles.get(i).get(j + 1).getType() == Tile.MINE;
    }

    private boolean checkMiddleLeftTile(List<List<Tile>> tiles, int i, int j) {
        return j - 1 >= 0 && tiles.get(i).get(j - 1).getType() == Tile.MINE;
    }

    private boolean checkTopRightTile(List<List<Tile>> tiles, int i, int j) {
        return i - 1 >= 0 && j + 1 < COLS && tiles.get(i - 1).get(j + 1).getType() == Tile.MINE;
    }

    private boolean checkTopMiddleTile(List<List<Tile>> tiles, int i, int j) {
        return i - 1 >= 0 && tiles.get(i - 1).get(j).getType() == Tile.MINE;
    }

    private boolean checkTopLeftTile(List<List<Tile>> tiles, int i, int j) {
        return i - 1 >= 0 && j - 1 >= 0 && tiles.get(i - 1).get(j - 1).getType() == Tile.MINE;
    }

    @Override
    public void setup() {
        super.setup();
        Random randomNumberGenerator = new Random();
        int x = BOARD_START_X, y = BOARD_START_Y;
        for (int i = 0; i < ROWS; i++) {
            List<Tile> tileList = new ArrayList<>();
            for (int j = 0; j < COLS; j++) {
                if (randomNumberGenerator.nextInt(COLS * ROWS) > (int) (ROWS * COLS * THRESHOLD))
                    tileList.add(new Tile(Tile.MINE));
                else {
                    tileList.add(new Tile(Tile.EMPTY));
                    totalEmptySpaces++;
                }
                rect(x, y, TILE_SIZE, TILE_SIZE);
                x += TILE_SIZE;
            }
            tiles.add(tileList);
            y += TILE_SIZE;
            x = BOARD_START_X;
        }
        setMineCountForTiles(tiles);
        gameRunning = true;
    }

    @Override
    public void draw() {

    }
}
