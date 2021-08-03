# Design:

## GridSquare:
A GridSquare represents each square that makes up the game grid. Each square is composed of two fields: one for whether or not it is active, and one for what state it is in.
The possible states are defined by an independent enum. Using an enum made all code using states much more readable and understandable. The states are listed below:

- NEUTRAL -- the square can be captured by either player
- P1      -- the square that player1 (the user) is on
- P2      -- the square that player2 (the computer) is on
- P1OWNED -- a square captured by p1, except for their current location
- P2OWNED -- a square captured by p2, except for their current location
- POWERUP -- a square that once moved to triggers a powerup

## Grid:
The `Grid` class represents a data type which stores the underlying 2D grid of GridSquares that form the map of the game. It is constructed using five parameters: the grid height and grid width, which are integers that store the height and width of the entire grid (including playable and non-playable area), the grid density, which is a double value between 0.0 and 1.0, that indicates the percentage of grid squares out of the total grid area that will be kept active, the power-up density, which is also a double value between 0.0 and 1.0, that indicates the probability of a particular grid square being a power-up, and random mode, a boolean which stores whether the generated grid is random or not. The grid density was added as a parameter to avoid unexpecxted results with randomly generated grids, i.e., grids with very less or no activated grid squares. The power-up density was added so that the player (in the custom mode), and the developer (in the standard and random modes) can control the density of power-ups in the grid of a particular game. A 2D array was used to store the grid squares as it has the simplest and fastest way to access and modify values at given x and y coordinates. Random grids are generated using generative recursion to produce truly unique grids each time. The active parts of standard (rectangular) grids are automatically centered within the grid.

### Methods:
1. `void randomGridActivator(Random random)`
Creates a randomly generated grid, which does not have any specific shape but ensures that all activated grid squares are connected to each other in the playable grid area, i.e., there is always a valid path of activated grid squares to reach any activated grid square from another activated Grid square. It keeps expanding this generated grid until the ratio of the activated area of the grid to the total area of the grid exactly matches the given grid density. It also places power-ups at random spots in the activated grid area, using the probability given by the power-up density.

2. `boolean biasedPowerupRandom(Random random)`
Acts as a biased random source for placing power-ups (biased according to the given power-up density), which returns random boolean values.

3. `Position getPlayerStartPosition()`
Returns the player’s start position, which is the top-leftmost activated grid square position in any grid.

4. `Position getAIStartPosition()`
Returns the AI’s start position, which is the bottom-rightmost activated grid square position in any grid.

5. `void printGrid()`
Prints the grid to the console, with the following key for the state of each grid square:

- [/] – inactive grid squares
- [N] – neutral grid squares
- [*] – current position of player 1
- [&] – current position of player 2
- [1] – grid squares captured by player 1
- [2] – grid squares captured by player 2
- [P] – power-up grid squares

6. `GridSquare getSquare(int x, int y)`
Returns the grid square present at the given x and y coordinates.

7. `void setSquareState(int x, int y, State state)`
Sets the State of the grid square present at the given x and y coordinates to the given state.

8. `void updateBlockCount(State state, int change)`
Updates the count of grid squares having a specific grid state in the block counts map: if none previously existed, adds the key to the block counts map; otherwise increases the count by the given change value.

9. `int getBlockCount(State state)`
Returns the total number of grid squares having the given state.

10. `GridSquare[][] getInternalGrid()`
Returns a two-dimensional grid of grid squares that form the map of the game.

11. `double getDensity()`
Returns the grid density value, which is a double value between 0.0 and 1.0, that indicates the percentage of grid squares out of the total grid area that are active.

## Position:
Position is an immutable data type which stores the x and y coordinates of a grid square. It was created to store both coordinates in a single object, enabling the creation of stacks of positions.

### Methods:
1. `int getX()`
Returns the x coordinate of the grid square.

2. `int getY()`
Returns the y coordinate of the grid square.

## Player:
Represents a player, and keeps track of their current position. This class also handles player movement, checking validity and value of potential moves, changing the player’s coordinates with a successful move, and updating the state of each GridSquare moved to and away from. We decided to have a separate Player class that handles the player instead of just keeping track of them in the Grid class, as this separation would make adding more players (like 4 instead of just 2) if we so wished much easier. The Player class stores a reference to a Grid, allowing it to access all of the relevant Grid information. This is important for checking moves. Each Player also stores what States define the player, such player1 taking in P1 and P1OWNED as its own States, and P2 and P2OWNED as its opponent’s States. This information is used when checking moves and when changing the state of a GridSquare. 

### Methods:
1. `char move()`
Given a direction char (such as ‘u’ for up) and whether or not capturing opponent’s territory is allowed, calls checkMove - and if returned true - moves the player’s coordinates and changes the state of any relevant GridSquares. Returns true if the move was successful and false otherwise.

2. `boolean checkMove()`
Returns whether a given move is valid  

3. `int checkMoveValue()`
Returns an integer value for a given move. A higher value represents a better move on the micro-scale (when just considering adjacent GridSquares). A value of 0 represents an invalid move. Powerups give the highest value, and a square not already controlled by the player gives a higher value than one that is.

## RandomAI:
Controls a non-human-controlled Player object. Has two methods for moving a Player-- randomMove and smarterRandomMove. 

### Methods:
1. `void randomMove()`
Purely random, selecting a direction at random and calling the Player’s move method until it returns true (when the move was successful).

2. `void smarterRandomMove()`
An improved randomization that gives priority to certain neighboring blocks depending on their state. Calls the Player’s checkMoveValue method for each direction, and then randomly selects a direction that will lead to a block with the highest value.   

## PowerUps:
Handles checking if a powerup is at certain coordinates, and then triggering a random powerup if true. Powerups capture additional squares beyond a player’s one move per turn. Powerups can capture squares of an opponent even when players themselves cannot. A PowerUps class stores a reference to a Grid in order to check and change GridSquares, and a reference to a Player so the correct state can be applied.

### Methods:
1. `checkPowerUp`
Checks if square at given coordinates is a powerup, and calls randomPowerUp if true;

2. `randomPowerUp`
Randomly selects 1 of 5 powerups to call.

3. `captureRandom`
Randomly attempts to capture one square on the grid for the referenced player. Calls checkSquare.

4. `captureDirection`
Recursively captures all squares in a given direction until a player or wall is hit. Calls checkSquare.

5. `captureCross`
Calls captureDirection for all 4 directions.

6. `checkSquare`
Checks to see if GridSquare at given coordinates is valid for capture (valid if GridSquare is not null, is active, and is not the current location of a player).

## GameGUI:
The GameGUI class is responsible for all of the visual aspects of the actual gameplay and primarily serves as the conduit between the back-end and front-end aspects of this project. Utilizing the javax.swing library allowed us to create aesthetically appealing non-terminal components that make playing the game a better experience for the user. Furthermore, we were very familiar working with components like JFrame, JLabel, JPanel, JTextField, JButton, JRadioButton, etc. and thus could use them in intricate ways to design the screen the way we believed the user should interact with the game. The GameGUI primarily consists of multiple JPanels which are arranged together in a JFrame; each of the JPanels contains either JLabels or JButtons to make up the screen that the user interacts with while playing the game. All of the components in the GameGUI are combined together in a BoxLayout (entailing that larger “overarching” JPanels like mainPanel often are made of multiple smaller JPanels like leftPanel and rightPanel which themselves include JLabels and JButtons). When a GameGUI is initialized (by the HomeScreen) the constructor performs the task of setting up the majority of the skeleton of the screen – it constructs several JPanels of logical dimensions and with logical BoxLayouts (oriented either in the X-Direction or the Y-Direction in terms of their layout), the JLabels with their specific tags and colors, and the JButtons, each with its respective Action Listener. Once all the necessary components have been initialized, they are all added to the mainPanel (which is finally added to the frame) in a specific order which is based on the layout of the mainPanel. 
The subclasses in the GameGUI class are the four specific ActionListeners that are fired as a result of pressing each of the four direction buttons (up, down, left, and right). Each of the ActionListeners is responsible for moving the player in the desired direction if the move is valid. GameGUI has another subclass for the backButton, the actionListener for which is responsible for taking the user back to the HomeScreen.  

### Methods:
1. `display`
Responsible for packing the frame and displaying all the components in the actual game.
A special note on the play area: when GameGUI is constructed, the leftPanel (where all the blocks representing the playing area are displayed) which has been oriented according to a GridLayout is filled with the appropriate number of JPanels (each possible game block is represented by a JPanel). When initializing in the constructor, a double array of JPanels holds a reference to each of the JPanels and all modifications are made to this double array of JPanels; these modifications (which will be explained further in Grid.Java) are reflected in the GameGUI whenever the display method is called.

2. `onMoveButtonClick()`
Responsible for ensuring that the number of turns, the number of blocks owned by the player and the number of blocks owned by the opponent are changed every time the user clicks a button and a successful move occurs. It is also responsible for checking whether a user has won/lost/tied and displaying the message accordingly.

## HomeScreen:
The HomeScreen is the central location from which users can access each of the three game modes: Random, Standard, or Custom. The GUI for the HomeScreen, much like all of the other GUI’s in this project, consists of a combination of JPanels and JLabels that come together to make the screen the user interacts with. The constructor for HomeScreenGUI sets up the necessary JPanels and ensures they have the right colors and orientations (direction used by the BoxLayout, etc.), initializes all of the JLabels used in the GUI with their respective Foregrounds, and creates all of the buttons from which the user can access the game modes. The subclasses for the HomeScreen consist of the ActionListeners attached to each of the game modes, which set up a GameGUI with the chosen game mode accordingly. If the user clicks on the JButton for CustomMode, they are taken to the screen described by CustomGUI. 

### Methods:
1. `display()`
Responsible for packing all of the components into the main frame and displaying the GUI

## CustomGUI:
The CustomGUI, like all of the other GUIs in this project, relies on a combination of several components which are all combined in a very specific order to create the screen visible to the player. The constructor for the CustomGUI performs the task of initializing all of the necessary JPanels and their respective input orientations (BoxLayout.X_AXIS or BoxLayout.Y_AXIS), the JLabels prompting the user for a specific input, the JTextFields which take in the user’s keyboard input (and show any subsequent error messages), the JRadioButtons which serve as the primary means of making a choice for the user, and the JButton which reads all of the choices the user has made and sets up a GameGUI accordingly. The subclasses for the CustomGUI are the ActionListeners for the back and the play buttons. The main function performed by the ActionListener of the play button is taking in all of the user’s choices: for TextFields, the ActionListener utilizes the getText() method and then formats the input as necessary; for JRadioButtons, the ActionListener utilizes the isSelected() method and then continues accordingly. All of the reading is done using a try-catch structure which allows the program to catch both typos as well as invalid inputs and thus prompts the user to re-enter their choices if needed. 

### Methods:
1. `display()`
Packs all of the components into the JFrame and displays the screen accessible to the user.