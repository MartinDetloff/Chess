public class Piece {
    private Movement movement;
    private String pieceName;
    private boolean isAlive = true;
    private boolean isWhite;

    public Piece(String pieceName, boolean isWhite) {
        this.pieceName = pieceName;
        this.isWhite = isWhite;
    }

    private Movement findMovement(String pieceName) {
        return new Movement(pieceName);
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    @Override
    public String toString() {
        return pieceName;
    }
}
