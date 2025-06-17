import java.util.ArrayList;
import java.util.Arrays;

public class Movement {
    private String moveMentString;
    private boolean isFirstMove = true;
    private String pieceName;
    private int[][] diagonalMoves = {
            {1,1},
            {1,-1},
            {-1,1},
            {-1,-1},
    };

    public Movement(String pieceName) {
        this.pieceName = pieceName;
        this.moveMentString = getMoveMentString(pieceName);
    }

    public String getMoveMentString(String pieceName) {
        return switch (pieceName) {
            case "King" -> "H1V1D1";
            case "Queen" -> // if we are a queen we can move diag/ up down
             "HIVIDI";
            case "Knight" ->
                // if we are a knight we can only move in L shapes
                    "LI";
            case "Rook" ->
                // if we are a rook we can move horizontally,
                // and vertically H for horizontally V for Vertical I for no limit
                    "HIVI";
            case "Bishop" ->
                // if we are a bishop we can only diagonally
                // D for diagonally I for no limit
                    "DI";
            case "Pawn" ->
                // need a way to say they can move forward 1
                // unless it's the first move and then take diagonally
                    "P";
            default -> "";
        };
    }

    public ArrayList<ArrayList<Integer>> getValidMoves(ArrayList<ArrayList<Piece>> board, int row, int col, Piece piece) {
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();
        String pieceC = piece.toString();

        // if we are a queen or a rook or a bishop then we want to use this method
        if(pieceName.equals("Queen") || pieceName.equals("Rook") || pieceName.equals("Bishop") || pieceName.equals("Knight")){
            parseMoveMentString(moveMentString, row, col, board);
        }

        return validMoves;
    }


    public void parseMoveMentString(String moveMentString, int row, int col, ArrayList<ArrayList<Piece>> board) {

        for (int i = 0; i < moveMentString.length(); i++) {
            char currentChar = moveMentString.charAt(i);
            System.out.println("This is the current character " + currentChar);
            helperDirections(currentChar, moveMentString, i, row, col, board);
        }
    }


    public void helperDirections(char direction, String moveMentString, int i, int row, int col, ArrayList<ArrayList<Piece>> board) {
        //we need a way to determine from the direction if we should be updating y or x
        int[][] moves;

        if(direction == 'H'){
            moves = new int[][] {
                    {1,0},
                    {-1,0}
            };
        }
        else if(direction == 'V'){
            moves = new int[][] {
                    {0,1},
                    {0,-1}
            };
        }
        else if(direction == 'D') {
            moves = new int[][]{
                    {1, 1},
                    {1, -1},
                    {-1, 1},
                    {-1, -1},
            };
        }
        else if(direction == 'L') {
            moves = new int[][]{
                    {1, 2},
                    {-1, 2},
                    {-1, -2},
                    {1, -2},
            };
        }

        else {
            return;
        }

        // always look at the next char
        if(moveMentString.charAt(i + 1) == 'I'){
            ArrayList<ArrayList<Integer>> combined = new ArrayList<>();

            int deltaX = 0;
            int deltaY = 0;

            for (int[] move : moves){
                deltaX = move[0];
                deltaY = move[1];

                ArrayList<ArrayList<Integer>> pos = exploreUntilYouHitSomething(deltaX, deltaY, row, col, board, direction == 'L' ? true : false);
                combined.addAll(pos);
            }
        }
    }


    /**
     * Method to explore until you hit something (USED FOR ROOK, BISHOP, QUEEN)
     * @param deltaX change in x
     * @param deltaY change in y
     * @param row start row
     * @param col start col
     * @param board the current board configuration
     * @return An arraylist of positions List ((ROW,COL), (ROW, COL), ETC)
     */
    public ArrayList<ArrayList<Integer>> exploreUntilYouHitSomething(int deltaX, int deltaY,
                                                                     int row, int col,
                                                                     ArrayList<ArrayList<Piece>> board,
                                                                     boolean isThereALimit) {
        // initial color here
        boolean isWhite = board.get(row).get(col).getIsWhite();
        int limit = isThereALimit ? 100 : 1;

        row += deltaY;
        col += deltaX;
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();
        int currentIteration = 0;

        while(row >= 0 && col >= 0 && row < 8 && col < 8 && currentIteration < limit) {
            Piece currentP = board.get(row).get(col);

            if (!currentP.toString().equals("Blank")){
                // we need to check if this is the same color or a different color
                boolean currentIsWhite = currentP.getIsWhite();

                if (isWhite && !currentIsWhite || !isWhite && currentIsWhite){
                    validMoves.add(new ArrayList<>(Arrays.asList(row, col)));
                    System.out.println("ROW " + row + " " + "COL " + col);
                }
                break;
            }

            else{
                validMoves.add(new ArrayList<>(Arrays.asList(row, col)));
                System.out.println("ROW " + row + " " + "COL " + col);
            }
            row += deltaY;
            col += deltaX;
            currentIteration++;

        }
        return validMoves;
    }

    public String getMoveMentString() {
        return this.moveMentString;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.makeInitialBoard();
        ArrayList<ArrayList<Piece>> board1 = board.getBoard();

        Movement m = new Movement("Knight");
//        m.exploreUntilYouHitSomething(1, 0, 1,0, board1);

//        m.parseMoveMentString("VI", 3, 3, board1);
        m.getValidMoves(board1, 3, 3, board.getPiece(3, 3));
    }


}
