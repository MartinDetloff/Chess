import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int rows;
    private int cols;
    private ArrayList<ArrayList<Piece>> board;

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
        for(int i = 0; i < rows; i++){


            if (i == 0 || i == rows - 1){
                boolean isWhite = i == 0 ? false : true;

                ArrayList<Piece> temp = new ArrayList<>();

                for (String f : f_lRows){
                    temp.add(new Piece(f, isWhite));
                }

                // add the hardcoded values
                board.add(temp);
            }
            else if (i == 1 || i == cols - 2){
                // whole row of pawns
                ArrayList<Piece> temp = new ArrayList<>();
                boolean isWhite = i == 1 ? false : true;

                for(int j = 0; j < cols; j++){
                    temp.add(new Piece("Pawn", isWhite));
                }

                board.add(temp);
            }

            else {
                ArrayList<Piece> temp = new ArrayList<>();

                for(int j = 0; j < cols; j++){

                    // todo : remove this later
                    if (j == 3 && i == 3){
                        temp.add(new Piece("Pawn", true));
                    }
                    else {
                        temp.add(new Piece("Blank", true));
                    }
                }

                board.add(temp);
            }
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
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                System.out.println("Row : " + i + " Col: " + j + " " + board.get(i).get(j).toString());
            }
        }
    }

    /**
     * Method to grab the piece at a specified square on our board.
     * @param row our row
     * @param col our col
     * @return the piece in question
     */
    public Piece getPiece(int row, int col) {
        return board.get(row).get(col);
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.makeInitialBoard();
        board.printBoard();

//        System.out.println(board.getPiece(0, 2).toString());
//        System.out.println(board.getPiece(7, 0).toString());

    }
}
