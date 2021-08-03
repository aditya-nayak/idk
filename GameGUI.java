import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class GameGUI {
    private JFrame frame;
    private JPanel mainPanel, leftPanel, rightPanel, rightTop, rightMid, rMleft, rMright, rightBot;
    private JLabel mode, NumTriesPrompt, NumTries, NumBlocksPrompt, numBlocks, OppBlocksPrompt, oppBlocks;
    private JButton up, down, left, right, back; 
    private JPanel[][] jGrid;
    private int turnsLeft;
    private Grid grid;
    private Player player;
    private RandomAI ai;
    public GameMode gameMode;
    private boolean canCapture;

    private final int W = 1100, H = 650;

    public GameGUI(Grid grid, Player player1, RandomAI ai, GameMode gameMode, boolean canCapture, int turnsLeft) {
        GridSquare[][] internalGrid = grid.getInternalGrid();
        this.player = player1;
        this.ai = ai;
        this.grid = grid;
        this.gameMode = gameMode;
        this.canCapture = canCapture;
        this.turnsLeft = turnsLeft;
        
        jGrid = new JPanel[internalGrid.length][internalGrid[0].length];

        frame = new JFrame(gameMode + "");
        frame.setPreferredSize(new Dimension(W, H));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(W, H));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((W * 3)/ 5, H));
        leftPanel.setLayout(new GridLayout(internalGrid.length, internalGrid[0].length, 1, 1));
        
        for (int x = 0; x < internalGrid.length; x++) {
            for (int y = 0; y < internalGrid[0].length; y++) {
                JPanel temp = new JPanel();
                temp.setBackground(Color.black);
                temp.setName(x + ", " + y);
                jGrid[x][y] = temp;
                leftPanel.add(temp);
            }
        }
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension((W * 2)/ 5, H));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightTop = new JPanel();
        rightTop.setPreferredSize(new Dimension((W * 2) / 5, 100));
        rightTop.setBackground(new Color(128, 0, 128));

        rightMid = new JPanel();
        rightMid.setPreferredSize(new Dimension(((W * 2) / 5), (H - 100) / 2));
        rightMid.setLayout(new BoxLayout(rightMid, BoxLayout.X_AXIS));

        rMleft = new JPanel();
        rMleft.setPreferredSize(new Dimension(W / 5, (H - 100) / 2));
        rMleft.setLayout(new BoxLayout(rMleft, BoxLayout.Y_AXIS));

        rMright = new JPanel();
        rMright.setPreferredSize(new Dimension(W / 5, (H - 100) / 2));
        rMright.setLayout(new BoxLayout(rMright, BoxLayout.Y_AXIS));

        rightBot = new JPanel();
        rightBot.setPreferredSize(new Dimension(((W * 2) / 5), (H - 100) / 2));
        rightBot.setLayout(new GridLayout(3, 3));

        if (gameMode == GameMode.STANDARD) {
            mode = new JLabel("Standard Mode");
        } else if (gameMode == GameMode.RANDOM) {
            mode = new JLabel("Random Mode");
        } else {
            mode = new JLabel("Custom Mode");
        }
        mode.setFont(new Font("Times New Roman", Font.BOLD, 20));
        mode.setForeground(Color.YELLOW);

        NumTriesPrompt = new JLabel("Turns Left: ");
        NumTriesPrompt.setFont(new Font("Times New Roman", Font.BOLD, 15));
        NumTriesPrompt.setForeground(new Color(128, 0, 128));
        NumTriesPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        NumTries = new JLabel("" + turnsLeft);
        NumTries.setForeground(new Color(128, 0, 128));
        NumTries.setHorizontalAlignment(SwingConstants.CENTER);

        NumBlocksPrompt = new JLabel("          Blocks Captured: ");
        NumBlocksPrompt.setFont(new Font("Times New Roman", Font.BOLD, 15));
        NumBlocksPrompt.setForeground(new Color(128, 0, 128));
        NumBlocksPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        numBlocks = new JLabel("                      1"); // "        " + grid.getP1Blocks();
        numBlocks.setForeground(new Color(128, 0, 128));

        OppBlocksPrompt = new JLabel("          Opponent Blocks: ");
        OppBlocksPrompt.setFont(new Font("Times New Roman", Font.BOLD, 15));
        OppBlocksPrompt.setForeground(new Color(128, 0, 128));
        OppBlocksPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        oppBlocks = new JLabel("                      1"); // "         " + grid.getP2Blocks();
        oppBlocks.setForeground(new Color(128, 0, 128));

        up = new JButton("Up");
        up.addActionListener(new UpListener());
        up.setMnemonic(KeyEvent.VK_KP_UP);


        down = new JButton("Down");
        down.addActionListener(new DownListener());
        down.setMnemonic(KeyEvent.VK_KP_DOWN);

        left = new JButton("Left");
        left.addActionListener(new LeftListener());
        left.setMnemonic(KeyEvent.VK_KP_LEFT);

        right = new JButton("Right");
        right.addActionListener(new RightListener());
        right.setMnemonic(KeyEvent.VK_KP_RIGHT);

        back = new JButton("Back");
        back.addActionListener(new BackListener());

        rightTop.add(back);
        rightTop.add(mode);
        rMleft.add(NumTriesPrompt);
        rMleft.add(NumTries);
        rightMid.add(rMleft);
        rMright.add(NumBlocksPrompt);
        rMright.add(numBlocks);
        rMright.add(OppBlocksPrompt);
        rMright.add(oppBlocks);
        rightMid.add(rMright);
        rightBot.add(new JPanel());
        rightBot.add(up);
        rightBot.add(new JPanel());
        rightBot.add(left);
        rightBot.add(new JButton());
        rightBot.add(right);
        rightBot.add(new JPanel());
        rightBot.add(down);
        rightBot.add(new JPanel());
        rightPanel.add(rightTop);
        rightPanel.add(rightMid);
        rightPanel.add(rightBot);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        frame.getContentPane().add(mainPanel);
    }

    public void display() {
        frame.pack();
        GridSquare[][] iGrid = grid.getInternalGrid();
        for (int i = 0; i < iGrid.length; i++) {
            for (int j = 0; j < iGrid[i].length; j++) {
                if (!iGrid[i][j].active) {
                    jGrid[i][j].setBackground(Color.black);
                } else {
                    switch(iGrid[i][j].state) {
                        case NEUTRAL:
                            jGrid[i][j].setBackground(Color.white);
                            break;
                        case P1:
                            jGrid[i][j].setBackground(Color.blue);
                            break;
                        case P2:
                            jGrid[i][j].setBackground(Color.red);
                            break;
                        case P1OWNED:
                            jGrid[i][j].setBackground(new Color(93, 167, 227));
                            break;
                        case P2OWNED:
                            jGrid[i][j].setBackground(new Color(214, 86, 86));
                            break;
                        case POWERUP:
                            jGrid[i][j].setBackground(Color.green);
                            break;
                        case TRAP:
                            jGrid[i][j].setBackground(Color.white);
                            break;
                    }
                }
                
            }
        }

        frame.setVisible(true);
    }

    private void onMoveButtonClick() {
        turnsLeft--;
        NumTries.setText("" + turnsLeft);
        numBlocks.setText("                      " + grid.getBlockCount(State.P1OWNED));
        int aiValidMoves = 0;
        Player aiPlayer = ai.getAiPlayer();
        if (aiPlayer.checkMove(0, -1, canCapture)) {
            aiValidMoves++;
        }
        if (aiPlayer.checkMove(0, 1, canCapture)) {
            aiValidMoves++;
        }
        if (aiPlayer.checkMove(-1, 0, canCapture)) {
            aiValidMoves++;
        }
        if (aiPlayer.checkMove(1, 0, canCapture)) {
            aiValidMoves++;
        }
        if (aiValidMoves > 0) {
            ai.smarterRandomMove(canCapture);
        }
        int playerValidMoves = 0;
        if (player.checkMove(0, -1, canCapture)) {
            playerValidMoves++;
        }
        if (player.checkMove(0, 1, canCapture)) {
            playerValidMoves++;
        }
        if (player.checkMove(-1, 0, canCapture)) {
            playerValidMoves++;
        }
        if (player.checkMove(1, 0, canCapture)) {
            playerValidMoves++;
        }   
        
        oppBlocks.setText("                      " + grid.getBlockCount(State.P2OWNED));
        display();
        if (turnsLeft == 0 || aiValidMoves == 0 || playerValidMoves == 0) {
            if (grid.getBlockCount(State.P1OWNED) > grid.getBlockCount(State.P2OWNED)) {
                NumTriesPrompt.setText("USER WINS!!!!!!");
                NumTries.setText("😎😎😎😎");
            } else if (grid.getBlockCount(State.P1OWNED) < grid.getBlockCount(State.P2OWNED)) {
                NumTriesPrompt.setText("USER LOSES");
                NumTries.setText("😭😭😭😭");
            } else {
                NumTriesPrompt.setText("ITS A TIE!!!!");
                NumTries.setText("😶😶😶");
            }
            up.setEnabled(false);
            down.setEnabled(false);
            left.setEnabled(false);
            right.setEnabled(false);
        }
    }
    
    public class UpListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (player.move('u', canCapture)) {
                    onMoveButtonClick();
            }
        }
    }
    public class DownListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (player.move('d', canCapture)) {
                    onMoveButtonClick();
            }
        }
    }
    public class LeftListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (player.move('l', canCapture)) {
                    onMoveButtonClick();
            }
        }
    }
    public class RightListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (player.move('r', canCapture)) {
                    onMoveButtonClick();
            } 
        }
    }
    public class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            HomeScreen trial = new HomeScreen();
            trial.display();
            frame.setVisible(false);
        }
    }
}
