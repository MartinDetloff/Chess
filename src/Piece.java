public class Piece {
    private Movement movement;
    private String pieceName;
    private boolean isAlive = true;
    private boolean isWhite;
    private boolean isClicked = false;
    int[] position = new int[2];

    public Piece(String pieceName, boolean isWhite, int row, int col) {
        this.pieceName = pieceName;
        this.isWhite = isWhite;
        this.movement = new Movement(pieceName);
        this.position[0] = row;
        this.position[1] = col;
    }

    private Movement findMovement(String pieceName) {
        return new Movement(pieceName);
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    @Override
    public String toString() {
        return pieceName;
    }

    public Movement getMovement() {
        return this.movement;
    }

    public int[] getPosition() {
        return this.position;
    }

    public void setPosition(int row, int col) {
        this.position[0] = row;
        this.position[1] = col;
    }


    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean getIsClicked() {
        return isClicked;
    }
}
