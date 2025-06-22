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

            // if we are a queen we can move diag/ up down
            case "Queen" -> "HIVIDI";

            // if we are a knight we can only move in L shapes
            case "Knight" -> "LI";

            // if we are a rook we can move horizontally,
            // and vertically H for horizontally V for Vertical I for no limit
            case "Rook" -> "HIVI";

            // if we are a bishop we can only diagonally
            // D for diagonally I for no limit
            case "Bishop" -> "DI";

            // need a way to say they can move forward 1
            // unless it's the first move and then take diagonally
            case "Pawn" -> "P";

            default -> "";
        };
    }

    public ArrayList<ArrayList<Integer>> getValidMoves(ArrayList<ArrayList<Piece>> board, int row, int col, Piece piece) {
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();
        String pieceC = piece.toString();
        String currentMove = getMoveMentString(pieceC);

        // if we are a queen or a rook or a bishop then we want to use this method
        validMoves = parseMoveMentString(currentMove, row, col, board);

        // print for debugging
//        printValidMoves(validMoves);




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
            validMoves.addAll(helperDirections(currentChar, moveMentString, i, row, col, board));
        }

        return validMoves;
    }


    public ArrayList<ArrayList<Integer>>  helperDirections(char direction, String moveMentString, int i, int row,
                                                           int col, ArrayList<ArrayList<Piece>> board) {
        //we need a way to determine from the direction if we should be updating y or x
        int[][] moves;
        boolean isWhite = board.get(col).get(row).getIsWhite();
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
                    {2, 1},
                    {-2, 1},
                    {-2, -1},
                    {2, -1},
            };
        }

        else if(direction == 'P') {
            return handlePawnMoves(row, col, isWhite, board);
        }

        else if(direction == 'K') {
            return handleKingMoves(row, col, isWhite, board);
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

                ArrayList<ArrayList<Integer>> pos = exploreUntilYouHitSomething(
                        deltaX,
                        deltaY,
                        row,
                        col,
                        board,
                        direction == 'L' ? true : false
                );

                combined.addAll(pos);
            }
        }

        return combined;
    }


    /**
     * Helper method to handle the pawn moves
     * @param row the current row
     * @param col the current col
     * @param isWhite the boolean to check if a piece is white
     * @param board the current board
     * @return All the possible moves that the pawn could make
     */
    private  ArrayList<ArrayList<Integer>> handlePawnMoves(int row, int col, boolean isWhite,
                                                           ArrayList<ArrayList<Piece>> board){

        ArrayList<ArrayList<Integer>> combined = new ArrayList<>();

//        System.out.println("HERE ");
        // todo: need to also work on if there is pieces that they can take
        // bounds checking to make sure that the pawn cant go off of the screen
        ArrayList<Integer> move1 = new ArrayList<>();
        ArrayList<Integer> move2 = new ArrayList<>();


        if(this.isFirstMove && isWhite){

            if (board.get(col - 1).get(row).toString().equals("Blank")) {
                move2.addAll(Arrays.asList(row, col - 1));

                if (board.get(col - 2).get(row).toString().equals("Blank")) {
                    move1.addAll(Arrays.asList(row, col - 2));
                }
            }
        }

        else if(this.isFirstMove && !isWhite){

            if (board.get(col + 1).get(row).toString().equals("Blank")){
                move2.addAll(Arrays.asList(row , col + 1));

                if (board.get(col + 2).get(row).toString().equals("Blank")){
                    move1.addAll(Arrays.asList(row , col + 2));
                }
            }


        }

        else if (!isWhite && board.get(col + 1).get(row).toString().equals("Blank")){
            move1.addAll(Arrays.asList(row, col + 1));
        }

        else if (isWhite && board.get(col - 1).get(row).toString().equals("Blank")){
            move1.addAll(Arrays.asList(row, col - 1));
        }


        if(!this.isFirstMove){
            if (!move1.isEmpty()){
                combined.add(move1);
            }
        }
        else {
            if (!move1.isEmpty()){
                combined.add(move1);
            }
            if (!move2.isEmpty()){
                combined.add(move2);
            }
        }

        combined.addAll(checkForPawnTakes(col, row, board, isWhite));

        return combined;
    }


    /**
     * Helper method to handle the kings moves
     * @param row the current row
     * @param col the current col
     * @param isWhite the current boolean to check if the piece is white
     * @param board the current board configuration
     * @return All the possible moves that the current king can make
     */
    private  ArrayList<ArrayList<Integer>> handleKingMoves(int row, int col, boolean isWhite,
                                                           ArrayList<ArrayList<Piece>> board){
        ArrayList<ArrayList<Integer>> combined = new ArrayList<>();

//        System.out.println("IS WHITE IS " + isWhite);

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
            ArrayList<ArrayList<Piece>> deepCopy = createDeepCopy(board);
            int newRow = dirT[0] + row;
            int newCol = dirT[1] + col;

            if(!checkBounds(newRow, newCol)){
                continue;
            }

            boolean isSameColor = board.get(newCol).get(newRow).getIsWhite() == isWhite;
            boolean isBlankTile = board.get(newCol).get(newRow).toString().equals("Blank");

            Board board1 = new Board();
//            board1.printBoard(deepCopy);

            Piece piece = deepCopy.get(newRow).get(newCol);

            deepCopy.get(col).set(row, new Piece("Blank", true, row, col));
            deepCopy.get(newCol).set(newRow, new Piece("King", isWhite, newRow, newCol));

//            board1.printBoard(deepCopy);

//            System.out.println("Trying move to " + newRow + "," + newCol);

            if (((!isSameColor || isBlankTile) && !isSquareUnderAttack(newRow, newCol, deepCopy, isWhite))){
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(newRow);
                temp.add(newCol);
                combined.add(temp);
            }
        }

        // here we want to calculate the possible moves of all the other pieces, and make sure that's not conflicting
        // meaning that we cannot get into check by moving somewhere

        return combined;

    }

    private ArrayList<ArrayList<Piece>> createDeepCopy(ArrayList<ArrayList<Piece>> board){
        ArrayList<ArrayList<Piece>> deepCopy = new ArrayList<>();

        for(ArrayList<Piece> row : board){
            ArrayList<Piece> temp = new ArrayList<>();

            for(Piece piece : row){
                int[] pos = piece.getPosition();
                int rowT = pos[0];
                int colT = pos[1];
                temp.add(new Piece(piece.toString(), piece.getIsWhite(), rowT, colT));
            }
            deepCopy.add(temp);
        }

        return deepCopy;
    }

    /**
     * Helper method to check if the current square is under attack by any of the pieces
     * @param row the current row
     * @param col the current col
     * @param board the current board
     * @return true if underattack
     */
    private boolean isSquareUnderAttack(int row, int col, ArrayList<ArrayList<Piece>> board, boolean isWhite){

        for (int r = 0; r < 8; r ++){
            for (int c  = 0; c < 8; c ++){
                Piece piece = board.get(c).get(r);


                if (piece.toString().equals("King") || piece.getIsWhite() == isWhite){
                    continue;
                }


                ArrayList<ArrayList<Integer>> validMoves = getValidMoves(board,r ,c ,piece );
                for(ArrayList<Integer> move : validMoves){

                    if (move.get(0) == row && move.get(1) == col){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method to check for pawn takes
     * @param row current row
     * @param col current col
     * @param board current board
     * @param isWhite boolean to check if its white
     * @return positions that are valid
     */
    private ArrayList<ArrayList<Integer>> checkForPawnTakes(int row, int col, ArrayList<ArrayList<Piece>> board,
                                                            boolean isWhite) {

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

            if (!board.get(row2Check).get(currentCol).toString().equals("Blank") &&
                    board.get(row2Check).get(currentCol).getIsWhite() != isWhite){

                // temp hold the current row and col that we could potentially take
               ArrayList<Integer> temp = new ArrayList<>();

               temp.add(currentCol);
               temp.add(row2Check);


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
        boolean isWhite = board.get(col).get(row).getIsWhite();
        int limit = isThereALimit ? 1 : 100;

        row += deltaY;
        col += deltaX;
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>();
        int currentIteration = 0;

        while(row >= 0 && col >= 0 && row < 8 && col < 8 && currentIteration < limit) {
            Piece currentP = board.get(col).get(row);

            if (!currentP.toString().equals("Blank")){
                // we need to check if this is the same color or a different color
                boolean currentIsWhite = currentP.getIsWhite();

                if (isWhite && !currentIsWhite || !isWhite && currentIsWhite){
                    validMoves.add(new ArrayList<>(Arrays.asList(row, col)));
                }
                break;
            }
            else{
                validMoves.add(new ArrayList<>(Arrays.asList(row, col)));
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
    }

}
