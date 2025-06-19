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
            case "King" -> "K";
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
        validMoves = parseMoveMentString(moveMentString, row, col, board);

        // print for debugging
        printValidMoves(validMoves);

        return validMoves;
    }

    /**
     * Method to print out the valid moves for the chess piece in question
     * @param validMoves the valid moves
     */
    private void printValidMoves(ArrayList<ArrayList<Integer>> validMoves) {
        for (ArrayList<Integer> validMove : validMoves) {
            System.out.println(" Row " + validMove.get(0) + " Col " + validMove.get(1));
        }
    }

    public ArrayList<ArrayList<Integer>> parseMoveMentString(String moveMentString, int row, int col,
                                                             ArrayList<ArrayList<Piece>> board) {

        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();

        // for loop to loop through the movement string and parse out any valid moves
        for (int i = 0; i < moveMentString.length(); i++) {
            char currentChar = moveMentString.charAt(i);
            System.out.println("This is the current character " + currentChar);
            validMoves.addAll(helperDirections(currentChar, moveMentString, i, row, col, board));
        }
        // just need to set this after the initial move here.
        isFirstMove = false;

        return validMoves;
    }


    public ArrayList<ArrayList<Integer>>  helperDirections(char direction, String moveMentString, int i, int row,
                                                           int col, ArrayList<ArrayList<Piece>> board) {
        //we need a way to determine from the direction if we should be updating y or x
        int[][] moves;
        boolean isWhite = board.get(row).get(col).getIsWhite();
        ArrayList<ArrayList<Integer>> combined = new ArrayList<>();

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

        else if(direction == 'P') {
            System.out.println("HERE ");
            // todo: need to also work on if there is pieces that they can take
            // bounds checking to make sure that the pawn cant go off of the screen
            ArrayList<Integer> move1 = new ArrayList<>();
            ArrayList<Integer> move2 = new ArrayList<>();


            if(this.isFirstMove && isWhite){
                move1.addAll(Arrays.asList(row - 2, col));
                move2.addAll(Arrays.asList(row - 1, col));
            }

            else if(this.isFirstMove && !isWhite){
                move1.addAll(Arrays.asList(row + 2, col));
                move2.addAll(Arrays.asList(row + 1, col));
            }

            else if (!isWhite){
                move1.addAll(Arrays.asList(row - 1, col));
            }

            else{
                move1.addAll(Arrays.asList(row - 1, col));
            }

            if(!this.isFirstMove){
                combined.add(move1);
            }
            else {
                combined.add(move1);
                combined.add(move2);
            }

            combined.addAll(checkForPawnTakes(row, col, board, isWhite));

            return combined;
        }

        // todo: add a way to check if the king can move there based upon checks
        else if(direction == 'K') {

            int[][] allDirections = {
                    {1,0}, // down
                    {-1,0}, // up
                    {-1,1}, // up right
                    {-1,-1}, // up left
                    {1,1}, // down right
                    {1,-1}, // down left
                    {0,-1}, // left
                    {0,1}, // right
            };


            for(int[] dirT :  allDirections){
                int newRow = dirT[0] + row;
                int newCol = dirT[1] + col;

                boolean isSameColor = board.get(newRow).get(newCol).getIsWhite() == isWhite;
                boolean isBlankTile = board.get(newRow).get(newCol).toString().equals("Blank");

                if(!checkBounds(newRow, newCol)){
                    continue;
                }

                else if (!isSameColor || isBlankTile){
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(newRow);
                    temp.add(newCol);
                    combined.add(temp);
                }
            }

            return combined;
        }

        else {
            return combined;
        }

        // always look at the next char
        if(moveMentString.charAt(i + 1) == 'I'){
            int deltaX = 0;
            int deltaY = 0;

            for (int[] move : moves){
                deltaX = move[0];
                deltaY = move[1];

                ArrayList<ArrayList<Integer>> pos = exploreUntilYouHitSomething(deltaX, deltaY, row, col, board, direction == 'L' ? true : false);
                combined.addAll(pos);
            }
        }

        return combined;
    }





    private ArrayList<ArrayList<Integer>> checkForPawnTakes(int row, int col, ArrayList<ArrayList<Piece>> board, boolean isWhite) {
        ArrayList<ArrayList<Integer>> combined = new ArrayList<>();
        int row2Check = isWhite ? row - 1 : row + 1;
        int[] cols2Check = {col - 1, col + 1};

        if (row2Check > 7 || row2Check < 0){
            return combined;
        }

        for (int currentCol : cols2Check){
            if(currentCol > 7 || currentCol < 0){
                continue;
            }
            else if (!board.get(row2Check).get(currentCol).toString().equals("Blank") &&
                    board.get(row2Check).get(currentCol).getIsWhite() != isWhite){

                System.out.println("We are here");

                // temp hold the current row and col that we could potentially take
               ArrayList<Integer> temp = new ArrayList<>();
               temp.add(row2Check);
               temp.add(currentCol);

               combined.add(temp);
            }
        }





        return combined;
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


    private boolean checkBounds(int row, int col){
        return row < 8 && row >= 0 && col < 8 && col >= 0;
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.makeInitialBoard();
        ArrayList<ArrayList<Piece>> board1 = board.getBoard();

        Movement m = new Movement("King");
//        m.exploreUntilYouHitSomething(1, 0, 1,0, board1);

//        m.parseMoveMentString("VI", 3, 3, board1);
        m.getValidMoves(board1, 3, 3, board.getPiece(3, 3));
    }


}
