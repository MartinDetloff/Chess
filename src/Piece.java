public class Piece {
    private Movement movement;
    private String pieceName;
    private boolean isAlive = true;
    private boolean isWhite;
    private boolean isClicked = false;

    public Piece(String pieceName, boolean isWhite) {
        this.pieceName = pieceName;
        this.isWhite = isWhite;
        this.movement = new Movement(pieceName);

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


    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean getIsClicked() {
        return isClicked;
    }
}
