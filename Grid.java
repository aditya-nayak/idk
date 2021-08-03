import java.io.*;
import java.util.*;

public class Grid {
    private GridSquare[][] grid;
    private int toActivate;
    private Stack<Position> leafStack;
    private Map<State, Integer> blockCounts;
    private double gridDensity, powerupDensity;
    private Random powerupRandom;
    private Position playerStartPosition, aiStartPosition;

    public Grid(int gridHeight, int gridWidth, double gridDensity, double powerupDensity, boolean randomMode) {
        this.gridDensity = gridDensity;
        this.powerupDensity = powerupDensity;
        grid = new GridSquare[gridHeight][gridWidth];
        blockCounts = new HashMap<State, Integer>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new GridSquare(false, State.NEUTRAL);          
            }
        }

        powerupRandom = new Random();

        if (!randomMode) {
            double lengthFraction = Math.sqrt(gridDensity);
            int activatedGridHeight = (int) Math.round(gridHeight * lengthFraction);
            int activatedGridWidth = (int) Math.round(gridWidth * lengthFraction);

            int gridStartX = (gridHeight - activatedGridHeight) / 2;
            int gridStartY = (gridWidth - activatedGridWidth) / 2;

            int gridEndX = gridStartX + activatedGridHeight - 1;
            int gridEndY = gridStartY + activatedGridWidth - 1;

            for (int i = gridStartX; i <= gridEndX; i++) {
                for (int j = gridStartY; j <= gridEndY; j++) {
                    grid[i][j].active = true;
                    
                    if (biasedPowerupRandom(powerupRandom)) {
                        grid[i][j].state = State.POWERUP;
                    }
                }
            }

            playerStartPosition = new Position(gridStartX, gridStartY);
            aiStartPosition = new Position(gridEndX, gridEndY);
        } else {
            toActivate = (int) Math.round(gridHeight * gridWidth * gridDensity);
            Random random = new Random();
            leafStack = new Stack<>();
            randomGridActivator(random);
        }
    }

    public boolean biasedPowerupRandom(Random random) {
        return (random.nextDouble() <= powerupDensity);
    }

    public void randomGridActivator(Random random) {
        int startRow, startCol;

        if (leafStack.isEmpty()) {
            startRow = random.nextInt(grid.length);
            startCol = random.nextInt(grid[0].length);
        } else {
            Position leafPosition = leafStack.pop();
            startRow = leafPosition.getX();
            startCol = leafPosition.getY();
        }

        randomGridActivator(startRow, startCol, random);

        if (toActivate > 0) {
            randomGridActivator(random);
        }
    }

    private void randomGridActivator(int r, int c, Random random) {
        if (r < grid.length && r >= 0 && c < grid[0].length && c >= 0
                && !grid[r][c].active && toActivate > 0) {
            if (random.nextBoolean()) {
                grid[r][c].active = true;
                toActivate--;

                if (biasedPowerupRandom(powerupRandom)) {
                    grid[r][c].state = State.POWERUP;
                }

                if ((playerStartPosition == null) || (playerStartPosition.getX() > r)
                        || (playerStartPosition.getX() == r && playerStartPosition.getY() > c)) {
                    playerStartPosition = new Position(r, c);
                }

                if ((aiStartPosition == null) || (aiStartPosition.getX() < r)
                        || (aiStartPosition.getX() == r && aiStartPosition.getY() < c)) {
                    aiStartPosition = new Position(r, c);
                }

                randomGridActivator(r + 1, c, random);
                randomGridActivator(r - 1, c, random);
                randomGridActivator(r, c + 1, random);
                randomGridActivator(r, c - 1, random);
            } else {
                leafStack.push(new Position(r, c));
            }
        }
    }

    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j].active) {
                    System.out.print("[/]");
                } else {
                    switch(grid[i][j].state) {
                        case NEUTRAL:
                            System.out.print("[N]");
                            break;
                        case P1:
                            System.out.print("[*]");
                            break;
                        case P2:
                            System.out.print("[&]");
                            break;
                        case P1OWNED:
                            System.out.print("[1]");
                            break;
                        case P2OWNED:
                            System.out.print("[2]");
                            break;
                        case POWERUP:
                            System.out.print("[P]");
                            break;
                        case TRAP:
                            System.out.print("[T]");
                            break;
                    }
                }
                
            }
            System.out.println();
        } 
    }

    public GridSquare getSquare(int x, int y) {
        if (y < 0 || y >= grid.length || x < 0 || x >= grid[y].length) {
            return null;
        }
        
        return grid[y][x];
    }

    public void setSquareState(int x, int y, State state) {
        State oldState = grid[y][x].state;
        grid[y][x].state = state;

        if (state != oldState) {
            updateBlockCount(state, 1);
            updateBlockCount(oldState, -1);
        }
    }

    public void updateBlockCount(State state, int change) {
        if (!blockCounts.containsKey(state)) {
            blockCounts.put(state, 0);
        } else {
            blockCounts.put(state, blockCounts.get(state) + change);
        }
    }

    public int getBlockCount(State state) {
        return blockCounts.get(state);
    }

    public GridSquare[][] getInternalGrid() {
        return grid;
    }

    public double getDensity() {
        return gridDensity;
    }

    public Position getPlayerStartPosition() {
        return playerStartPosition;
    }

    public Position getAIStartPosition() {
        return aiStartPosition;
    }
}
