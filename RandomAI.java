//import java.util.Random;
import java.util.*; 

public class RandomAI {
    private Random rand;
    private Player ai;

    public RandomAI(Player ai) {
        this.ai = ai;
        rand = new Random();
    }

    public Player getAiPlayer() {
        return ai;
    }

    public void randomMove(boolean canCapture) {
        char direction = 'u';
        int randInt = 0; 

        do {
            randInt = rand.nextInt(4);
            if (randInt == 0) {
                direction = 'u';
            } else if (randInt == 1) {
                direction = 'd';
            } else if (randInt == 2) {
                direction = 'l';
            } else if (randInt == 3) {
                direction = 'r';
            }
        } while (!ai.move(direction, canCapture));      
    }

    public void smarterRandomMove(boolean canCapture) {
        int upValue = ai.checkMoveValue(0, -1, canCapture);
        int downValue = ai.checkMoveValue(0, 1, canCapture);
        int leftValue = ai.checkMoveValue(-1, 0, canCapture);
        int rightValue = ai.checkMoveValue(1, 0, canCapture);
        List<Character> value3List = new LinkedList<Character>();
        if (upValue == 3) {
            value3List.add('u');
        }
        if (downValue == 3) {
            value3List.add('d');
        }
        if (leftValue == 3) {
            value3List.add('l');
        }
        if (rightValue == 3) {
            value3List.add('r');
        }
        if (!value3List.isEmpty()) {
            char direction = 'u';
            int randInt = 0; 
            randInt = rand.nextInt(value3List.size());
            direction = value3List.get(randInt);
            ai.move(direction, canCapture);
        } else {
            List<Character> value2List = new LinkedList<Character>();
            if (upValue == 2) {
                value2List.add('u');
            }
            if (downValue == 2) {
                value2List.add('d');
            }
            if (leftValue == 2) {
                value2List.add('l');
            }
            if (rightValue == 2) {
                value2List.add('r');
            }

            if (!value2List.isEmpty()) { 
                char direction = 'u';
                int randInt = 0; 
                randInt = rand.nextInt(value2List.size());
                direction = value2List.get(randInt);
                ai.move(direction, canCapture);
            } else {
                randomMove(canCapture);
            }
        }
    }
}
