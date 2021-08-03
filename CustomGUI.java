import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.Random;

public class CustomGUI {
    private JFrame frame;
    private JPanel mainPanel, panel1, panel2, panel3, panel4, panel4Left, panel4Right, 
                   panel5, panel6, panel6Left, panel6Right; 
    private JLabel title, hostileTakeoverPrompt, gridTypePrompt, turnPrompt, powerupPrompt, 
                   heightPrompt, widthPrompt, powerDensity;
    private JRadioButton hostileYes, hostileNo, gridTypeSt, gridTypeRd, powerY, powerN; 
    private JTextField turns, height, width, powerUpDensity; 
    private JButton play, back;

    private final int W = 600, H = 600;

    public CustomGUI() {
        frame = new JFrame("Custom Selections");
        frame.setPreferredSize(new Dimension(W, H));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(W,H));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(W, H/6));
        panel1.setBackground(new Color(0, 0, 128));

        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(W, H/6));
        panel2.setBackground(Color.WHITE);

        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(W, H/6));
        panel3.setBackground(Color.WHITE);

        panel4 = new JPanel();
        panel4.setPreferredSize(new Dimension(W, H/6));
        panel4.setBackground(Color.WHITE);
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));

        panel4Left = new JPanel();
        panel4Left.setPreferredSize(new Dimension(W/2, H/6));
        panel4Left.setBackground(Color.WHITE);

        panel4Right = new JPanel();
        panel4Right.setPreferredSize(new Dimension(W/2, H/6));
        panel4Right.setBackground(Color.WHITE);

        panel5 = new JPanel();
        panel5.setPreferredSize(new Dimension(W, H/6));
        panel5.setBackground(Color.WHITE);

        panel6 = new JPanel();
        panel6.setPreferredSize(new Dimension(W, H/6));
        panel6.setBackground(Color.WHITE);
        panel6.setLayout(new BoxLayout(panel6, BoxLayout.X_AXIS));

        panel6Left = new JPanel();
        panel6Left.setPreferredSize(new Dimension(W/2, H/6));
        panel6Left.setBackground(Color.WHITE);

        panel6Right = new JPanel();
        panel6Right.setPreferredSize(new Dimension(W/2, H/6));
        panel6Right.setBackground(Color.WHITE);

        title = new JLabel("Customize");
        title.setForeground(Color.YELLOW);

        hostileTakeoverPrompt = new JLabel("Hostile Takeover: ");
        hostileTakeoverPrompt.setForeground(new Color(0, 0, 128));

        gridTypePrompt = new JLabel("Grid generation: ");
        gridTypePrompt.setForeground(new Color(0, 0, 128));

        turnPrompt = new JLabel("Number of turns: ");
        turnPrompt.setForeground(new Color(0, 0, 128));

        powerupPrompt = new JLabel("Power-Ups: ");
        powerupPrompt.setForeground(new Color(0, 0, 128));

        powerDensity = new JLabel("Power-Up Frequency (in %): ");
        powerDensity.setForeground(new Color(0, 0, 128));

        heightPrompt = new JLabel("Height: ");
        heightPrompt.setForeground(new Color(0, 0, 128));

        widthPrompt = new JLabel("Width: ");
        widthPrompt.setForeground(new Color(0, 0, 128));

        hostileYes = new JRadioButton("Enable");
        hostileNo = new JRadioButton("Disable");

        ButtonGroup group1 = new ButtonGroup();
        group1.add(hostileYes);
        group1.add(hostileNo);

        gridTypeSt = new JRadioButton("Standard");
        gridTypeRd = new JRadioButton("Randomized");

        ButtonGroup group2 = new ButtonGroup();
        group2.add(gridTypeSt);
        group2.add(gridTypeRd);

        powerY = new JRadioButton("Enabled");
        powerN = new JRadioButton("Disabled");

        ButtonGroup group3 = new ButtonGroup();
        group3.add(powerY);
        group3.add(powerN);

        turns = new JTextField();
        turns.setPreferredSize(new Dimension(300, 30));

        height = new JTextField();
        height.setPreferredSize(new Dimension(100, 30));

        width = new JTextField();
        width.setPreferredSize(new Dimension(100, 30));

        powerUpDensity = new JTextField();
        powerUpDensity.setPreferredSize(new Dimension(75, 30));

        play = new JButton("Play");
        play.addActionListener(new PlayListener());

        back = new JButton("Back");
        back.addActionListener(new BackListener());

        panel1.add(back);
        panel1.add(title);
        panel1.add(play);
        panel2.add(hostileTakeoverPrompt);
        panel2.add(hostileYes);
        panel2.add(hostileNo);
        panel3.add(gridTypePrompt);
        panel3.add(gridTypeSt);
        panel3.add(gridTypeRd);
        panel4Left.add(heightPrompt);
        panel4Left.add(height);
        panel4Right.add(widthPrompt);
        panel4Right.add(width);
        panel4.add(panel4Left);
        panel4.add(panel4Right);
        panel5.add(turnPrompt);
        panel5.add(turns);
        panel6Left.add(powerupPrompt);
        panel6Left.add(powerY);
        panel6Left.add(powerN);
        panel6Right.add(powerDensity);
        panel6Right.add(powerUpDensity);
        panel6.add(panel6Left);
        panel6.add(panel6Right);
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        mainPanel.add(panel6);

        frame.getContentPane().add(mainPanel);
    }
    public void display() {
        frame.pack();
        frame.setVisible(true);
    }
    public class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            frame.setVisible(false);
            HomeScreen temp = new HomeScreen();
            temp.display();
        }
    }
    public class PlayListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                boolean canCapture; 
                if (hostileYes.isSelected()) {
                    canCapture = true;
                } else {
                    canCapture = false; 
                }
                int numTurns = Integer.parseInt(turns.getText());
                int prefHeight = Integer.parseInt(height.getText());
                int prefWidth = Integer.parseInt(width.getText());
                double puDensity = Double.parseDouble(powerUpDensity.getText()) / 100;
                boolean pUp = powerY.isSelected();
                if (!pUp) {
                    puDensity = 0.0;
                }
                if (numTurns <= 0 || prefHeight > 100 || prefWidth > 100 || puDensity > 1.0) {
                    throw new IllegalArgumentException();
                }
                GameMode mode;
                if (gridTypeSt.isSelected()) {
                    mode = GameMode.STANDARD;
                    frame.setVisible(false);
                    Grid grid = new Grid(prefHeight, prefWidth, 1.0, puDensity, false);
                    Player p1 = new Player(grid, State.P2, State.P2OWNED, State.P1, State.P1OWNED, 
                                           grid.getPlayerStartPosition());
                    Player p2 = new Player(grid, State.P1, State.P1OWNED, State.P2, State.P2OWNED,
                                           grid.getAIStartPosition());
                    RandomAI ai = new RandomAI(p2);

                    GameGUI gameGUI = new GameGUI(grid, p1, ai, mode, canCapture, numTurns);
                    gameGUI.display();
                    
                } else {
                    mode = GameMode.RANDOM;
                    frame.setVisible(false);
                    Random rand = new Random();
                    double randDouble = rand.nextDouble() + 0.15;
                    if (randDouble > 1.0) {
                        randDouble = 0.5;
                    }
                    Grid grid = new Grid(prefHeight, prefWidth, randDouble, puDensity, true);
                    Player p1 = new Player(grid, State.P2, State.P2OWNED, State.P1, State.P1OWNED,
                                           grid.getPlayerStartPosition());
                    Player p2 = new Player(grid, State.P1, State.P1OWNED, State.P2, State.P2OWNED, 
                                           grid.getAIStartPosition());
                    RandomAI ai = new RandomAI(p2);

                    GameGUI gameGUI = new GameGUI(grid, p1, ai, mode, canCapture, numTurns);
                    gameGUI.display();
                }
                
            } catch (Exception e) {
                turns.setText("Invalid Input. Please try again.");
            }
        }
    }
}


