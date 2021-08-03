public class GridSquare {
    public boolean active;
    public State state;

    // Defualt state of a square
    public GridSquare() {
        this(false, State.NEUTRAL);
    }

    public GridSquare(boolean active, State state) {
        this.active = active;
        this.state = state;
    }
}
