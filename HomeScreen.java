import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.Random;

public class HomeScreen {
    private JFrame frame; 
    private JPanel mainPanel, topPanel, bottomPanel, bottomLeft, bottomMiddle, bottomRight;
    private JLabel welcome;
    private JButton standard, random, custom;

    private final int W = 300, H = 200;

    public HomeScreen() {
        frame = new JFrame("International Defense Korp");
        frame.setPreferredSize(new Dimension(W, H));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(W, H));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(W, H/2));
        topPanel.setBackground(Color.white);

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(W, H/2));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        bottomLeft = new JPanel();
        bottomLeft.setPreferredSize(new Dimension(W/3, H/2));
        bottomLeft.setBackground(new Color(0, 0, 128));

        bottomMiddle = new JPanel();
        bottomMiddle.setPreferredSize(new Dimension(W/3, H/2));
        bottomMiddle.setBackground(new Color(0, 0, 128));

        bottomRight = new JPanel();
        bottomRight.setPreferredSize(new Dimension(W/3, H/2));
        bottomRight.setBackground(new Color(0, 0, 128));

        welcome = new JLabel("I.D.K");
        welcome.setForeground(Color.red);

        standard = new JButton("Standard");
        standard.addActionListener(new StandardListener());

        random = new JButton("Random");
        random.addActionListener(new RandomListener());

        custom = new JButton("Custom");
        custom.addActionListener(new CustomListener());

        topPanel.add(welcome);
        bottomLeft.add(standard);
        bottomMiddle.add(random);
        bottomRight.add(custom);
        bottomPanel.add(bottomLeft);
        bottomPanel.add(bottomMiddle);
        bottomPanel.add(bottomRight);
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        frame.getContentPane().add(mainPanel);
    }

    public void display() {
        frame.pack();
        frame.setVisible(true);
    }

    public class StandardListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            frame.setVisible(false);

            Grid grid = new Grid(15, 15, 0.8, 0.025, false);
            Player p1 = new Player(grid, State.P2, State.P2OWNED, State.P1, 
                                State.P1OWNED, grid.getPlayerStartPosition());
            Player p2 = new Player(grid, State.P1, State.P1OWNED, State.P2, 
                                State.P2OWNED, grid.getAIStartPosition());
            RandomAI ai = new RandomAI(p2);

            GameGUI gameGUI = new GameGUI(grid, p1, ai, GameMode.STANDARD, false, 60);
            gameGUI.display();
        }
    }
    
    public class RandomListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            frame.setVisible(false);
            
            Random rand = new Random();
            int randNum = rand.nextInt(46) + 5;
            double randDouble = rand.nextDouble() + 0.15;
            if (randDouble > 1.0) {
                randDouble = 0.5;
            }
            Grid grid = new Grid(randNum, randNum, randDouble, 0.025, true);
            Player p1 = new Player(grid, State.P2, State.P2OWNED, State.P1,
                                 State.P1OWNED, grid.getPlayerStartPosition());
            Player p2 = new Player(grid, State.P1, State.P1OWNED, State.P2, 
                                 State.P2OWNED, grid.getAIStartPosition());
            RandomAI ai = new RandomAI(p2);

            GridSquare[][] internalGrid = grid.getInternalGrid();
            int x = (int) Math.round(internalGrid.length * internalGrid[0].length * grid.getDensity());
            int turnsLeft = x - 5 * ((int) Math.round(Math.sqrt(x)));
            GameGUI gameGUI = new GameGUI(grid, p1, ai, GameMode.RANDOM, true, turnsLeft);
            gameGUI.display();
        }
    }
    public class CustomListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            frame.setVisible(false);
            CustomGUI cust = new CustomGUI();
            cust.display();
        }
    }
}
