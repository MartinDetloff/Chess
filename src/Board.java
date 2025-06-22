import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int rows;
    private int cols;
    private ArrayList<ArrayList<Piece>> board = new ArrayList<>();
    private Movement[][] movements = new Movement[rows][cols];
    private int[] b_kingPos = new int[2];
    private int[] w_kingPos = new int[2];


    ArrayList<String> f_lRows = new ArrayList<>(Arrays.asList(
            "Rook",
            "Knight",
            "Bishop",
            "Queen",
            "King",
            "Bishop",
            "Knight",
            "Rook"
    ));


    public Board() {
        this.rows = 8;
        this.cols = 8;
        this.board = new ArrayList<>();
    }

    //todo: layout all of the pieces on the board
    // what am i going to keep in here?


    /**
     * Method to make the initial board
     */
    public void makeInitialBoard(){
        // set the initial positions of the kings
        setB_kingPos(4, 0);
        setW_kingPos(4, 7);

        for(int i = 0; i < rows; i++){
            // if we are on the first row
            if (i == 0 || i == rows - 1){
                boolean isWhite = i == 0 ? false : true;

                ArrayList<Piece> temp = new ArrayList<>();

                int count = 0;
                for (String f : f_lRows){
                    temp.add(new Piece(f, isWhite, i, count));
                    count++;
                }

                // add the hardcoded values
                board.add(temp);
            }
            else if (i == 1 || i == rows - 2){

                ArrayList<Piece> temp = new ArrayList<>();
                boolean isWhite = i == 1 ? false : true;

                for(int j = 0; j < cols; j++){
                    temp.add(new Piece("Pawn", isWhite, i, j));
                }

                board.add(temp);
            }

            else {
                ArrayList<Piece> temp = new ArrayList<>();

                for(int j = 0; j < cols; j++){
                    temp.add(new Piece("Blank", true, i, j));
                }

                board.add(temp);
            }
        }

    }

    private void correlateMovements(){




        // we are going to populate the movement array with
    }

    private void printArrayList(){
        int index = 0;
        for(ArrayList<Piece> temp : board){
            System.out.println(" Row " + index);
            for(Piece i : temp){
                System.out.print(i.toString() + " ");
            }
            index++;
        }

    }


    /**
     * Method to get the current board
     * @return the board
     */
    public ArrayList<ArrayList<Piece>> getBoard() {
        return board;
    }

    /**
     * Print the entire board
     */
    public void printBoard() {
        System.out.println();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                System.out.print(board.get(i).get(j).toString() + " ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
            * Print the entire board
     */
    public void printBoard(ArrayList<ArrayList<Piece>> board) {
//        System.out.println("DEEP HERE ");
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                System.out.print(board.get(i).get(j).toString() + " ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Method to grab the piece at a specified square on our board.
     * @param row our row
     * @param col our col
     * @return the piece in question
     */
    public Piece getPiece(int row, int col) {
        return board.get(col).get(row);
    }

    public void setPiece(int row, int col, Piece piece) {
        board.get(col).set(row, piece);
//        System.out.println("Update the board here " + board.get(col).get(row).toString());
    }

    /**
     * Method to check for mate/ check mate
     * @param isWhite if the last move was white or black
     * @return 1 for check, 0 for check mate, and -1 for none
     */
    public int checkForMate(boolean isWhite){
        // if we are in check return 1
        // if we are in check mate return 0
        int[] kingPos = isWhite ? b_kingPos : w_kingPos;
        int kingRow = kingPos[0];
        int kingCol = kingPos[1];
        Piece kingPiece = getPiece(kingRow, kingCol);

        int possibleKingMoves = (kingPiece.getMovement().getValidMoves(board, kingRow, kingCol, kingPiece).size());


        for(ArrayList<Piece> temp : board){
            // go into movement, get possible moves, check if these move positions are the same as king position
            for (Piece piece : temp) {
                int[] pos1 = piece.getPosition();
                int row = pos1[0];
                int col = pos1[1];

                String pieceName = piece.toString();
                boolean pieceIsWhite = piece.getIsWhite();

                if (!pieceName.equals("Blank") && !pieceName.equals("King") && pieceIsWhite == isWhite){
                    Movement m = piece.getMovement();
                    ArrayList<ArrayList<Integer>> pos = m.getValidMoves(board, row, col, piece);

                    // loop through all the positions and check if the kings position is there
                    for (ArrayList<Integer> tempPos : pos) {
                        if (tempPos.get(0) == kingRow && tempPos.get(1) == kingCol){
                            // return 0 if we are in check mate
                            System.out.println("The color is white " + pieceIsWhite);

                            if (possibleKingMoves == 0){
                                System.out.println(row + " " + col + " " + pieceName);
                                return 0;
                            }
                            // return 1 if we are in mate
                            else{
                                System.out.println(row + " " + col + " " + pieceName);
                                return 1;
                            }
                        }
                    }
                }
//                col ++;
            }
//            row ++;
        }

        return -1;
    }

    /**
     * Setter for the black king position
     * @param row the row to set it to
     * @param col the col to set it to
     */
    public void setB_kingPos(int row, int col){
        b_kingPos[0] = row;
        b_kingPos[1] = col;
    }

    /**
     * Setter for the white king position
     * @param row the row to set it to
     * @param col the col to set it to
     */
    public void setW_kingPos(int row, int col){
        w_kingPos[0] = row;
        w_kingPos[1] = col;
    }


//    public Piece getPiece(Piece piece) {
//        return board.
//    }

    public static void main(String[] args) {
        Board board = new Board();
        board.makeInitialBoard();
        board.printBoard();

//        board.setPiece(0, 2, new Piece("King", true));
        System.out.println(board.getPiece(0, 2).toString());
        System.out.println(board.getPiece(7, 0).toString());

    }
}
