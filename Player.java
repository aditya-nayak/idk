public class Player {
    private Grid grid;
    // coordinate origin in top left corner of grid
    private int xPos;
    private int yPos;
    private State enemyState;
    private State enemyControl;
    private State playerState;
    private State controlledState;
    private PowerUps pu;

    public Player(Grid grid, State enemyState, State enemyControl, State playerState,
                  State controlledState, Position playerPosition) {
        this.pu = new PowerUps(grid, this);
        this.grid = grid;
        grid.updateBlockCount(controlledState, 0);
        this.enemyState = enemyState;
        this.enemyControl = enemyControl;
        this.playerState = playerState;
        this.controlledState = controlledState;
        xPos = playerPosition.getY();
        yPos = playerPosition.getX();
        grid.setSquareState(xPos, yPos, playerState);
    }

    public int[] getPosition() {
        return new int[] {xPos, yPos};
    }

    public State getEnemyControl() {
        return enemyControl;
    }

    public State getControlledState() {
        return controlledState;
    }

    public boolean move(char direction) {
        return move(direction, false);
    }

    public boolean move(char direction, boolean canCapture) {
        if (direction == 'u') {
            if (checkMove(0, -1, canCapture)) {
                grid.setSquareState(xPos, yPos, controlledState);
                yPos--;
                pu.checkPowerUp(xPos, yPos);
                grid.setSquareState(xPos, yPos, playerState);
                return true;
            } else {
                return false;
            }
        } else if (direction == 'd') {
            if (checkMove(0, 1, canCapture)) {
                grid.setSquareState(xPos, yPos, controlledState);
                yPos++;
                pu.checkPowerUp(xPos, yPos);
                grid.setSquareState(xPos, yPos, playerState);
                return true;
            } else {
                return false;
            }
        } else if (direction == 'l') {
            if (checkMove(-1, 0, canCapture)) {
                grid.setSquareState(xPos, yPos, controlledState);
                xPos--;
                pu.checkPowerUp(xPos, yPos);
                grid.setSquareState(xPos, yPos, playerState);
                return true;
            } else {
                return false;
            }
        } else if (direction == 'r') {
            if (checkMove(1, 0, canCapture)) {
                grid.setSquareState(xPos, yPos, controlledState);
                xPos++;
                pu.checkPowerUp(xPos, yPos);
                grid.setSquareState(xPos, yPos, playerState);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean checkMove(int xDirection, int yDirection, boolean canCapture) {
        if (canCapture) {
            return grid.getSquare(xPos + xDirection, yPos + yDirection) != null
                    && grid.getSquare(xPos + xDirection, yPos + yDirection).active 
                    && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyState;
        } else {
            return grid.getSquare(xPos + xDirection, yPos + yDirection) != null
                    && grid.getSquare(xPos + xDirection, yPos + yDirection).active 
                    && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyState
                    && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyControl;
        }
    }

    // Higher return value is more valuable. A return value of 0 means invalid move, 1 means
    // valid but onto own territory, 2 means valid and not onto own territory
    public int checkMoveValue(int xDirection, int yDirection, boolean canCapture) {
        if (canCapture) {
            if (grid.getSquare(xPos + xDirection, yPos + yDirection) != null
                        && grid.getSquare(xPos + xDirection, yPos + yDirection).active 
                        && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyState) {
                if (grid.getSquare(xPos + xDirection, yPos + yDirection).state != controlledState) {
                    if (grid.getSquare(xPos + xDirection, yPos + yDirection).state == State.POWERUP) {
                        return 3;
                    } else {
                        return 2;
                    }
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            if (grid.getSquare(xPos + xDirection, yPos + yDirection) != null
                        && grid.getSquare(xPos + xDirection, yPos + yDirection).active 
                        && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyState
                        && grid.getSquare(xPos + xDirection, yPos + yDirection).state != enemyControl) {
                if (grid.getSquare(xPos + xDirection, yPos + yDirection).state != controlledState) {
                    if (grid.getSquare(xPos + xDirection, yPos + yDirection).state == State.POWERUP) {
                        return 3;
                    } else {
                        return 2;
                    }
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        }  
    }
}
