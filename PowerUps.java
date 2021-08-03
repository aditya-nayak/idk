import java.util.Random;


public class PowerUps {
    private Grid grid;
    private Player player;

    public PowerUps(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
    }

    public void checkPowerUp(int xPos, int yPos) {
        if (grid.getSquare(xPos, yPos).state == State.POWERUP) {
            randomPowerUp(xPos, yPos);
        }
    }

    public void randomPowerUp(int xPos, int yPos) {
        Random rand = new Random();
        int randInt = rand.nextInt(6);
        switch(randInt) {
            case 0:
                captureRandom();
                break;
            case 1:
                captureDirection(xPos, 0, yPos, -1);
                break;
            case 2:
                captureDirection(xPos, 0, yPos, 1);
                break;
            case 3:
                captureDirection(xPos, -1, yPos, 0);
                break;
            case 4:
                captureDirection(xPos, 1, yPos, 0);
                break;
            case 5:
                captureCross(xPos, yPos);
                break;
            default:
                
        }
    }

    public void captureRandom() {
        Random rand = new Random();
        int randX = rand.nextInt(grid.getInternalGrid().length + 1);
        int randY = rand.nextInt(grid.getInternalGrid().length + 1);
        if (checkSquare(randX, randY)) {
            //chains powerups
            //checkPowerUp(randX, randY);
            grid.setSquareState(randX, randY, player.getControlledState());
        }
    }

    private void captureDirection(int xPos, int xChange, int yPos, int yChange) {
        if (checkSquare(xPos, yPos)) {
            //chains powerups
            //checkPowerUp(xPos, yPos);
            grid.setSquareState(xPos, yPos, player.getControlledState());
            /*
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            */

            //player.gui.display();
            captureDirection(xPos + xChange, xChange, yPos + yChange, yChange);
        }
    }

    public void captureCross(int xPos, int yPos) {
        captureDirection(xPos, 0, yPos, -1);
        captureDirection(xPos, 0, yPos, 1);
        captureDirection(xPos, -1, yPos, 0);
        captureDirection(xPos, 1, yPos, 0);
    }

    public boolean checkSquare(int xPos, int yPos) {
        return grid.getSquare(xPos, yPos) != null
                        && grid.getSquare(xPos, yPos).active 
                        && grid.getSquare(xPos, yPos).state != State.P1
                        && grid.getSquare(xPos, yPos).state != State.P2;
    }
}
