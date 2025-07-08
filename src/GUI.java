import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GUI extends Application {

    private Board board = new Board();
    private final int rows = 8;
    private final int cols = 8;
    private GridPane grid = new GridPane();
    private HashMap<StackPane, int[]> pieces = new HashMap<>();
    private int[] lastPiecePos = null;
    private ArrayList<ArrayList<Integer>> lastPossibleMoves = new ArrayList<>();
    private HashMap<String, Image> images = new HashMap<>();
    private int numberOfPeopleIn = 0;
    private boolean isWhitesTurn = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // make the initial board here
        board.makeInitialBoard();

        // set up the image assets here
        setUpImages();

        // get the grid pane setup here
        GridPane grid = setUpGrid();

        // show the current scene
        int screenWidth = 512;
        int screenHeight = 512;
        primaryStage.setScene(new Scene(grid, screenWidth, screenHeight));
        primaryStage.show();
    }


    /**
     * Method to map strings to all the images here
     *
     */
    private void setUpImages(){
        images.put("b_Bishop",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_bishop.png"))));
        images.put("b_Knight",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_knight.png"))));
        images.put("b_Pawn",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_pawn.png"))));
        images.put("b_Queen",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_queen.png"))));
        images.put("b_Rook",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_rook.png"))));
        images.put("b_King",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("b_king.png"))));
        images.put("w_Bishop",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_bishop.png"))));
        images.put("w_King",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_king.png"))));
        images.put("w_Knight",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_knight.png"))));
        images.put("w_Pawn",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_pawn.png"))));
        images.put("w_Queen",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_queen.png"))));
        images.put("w_Rook",  new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("w_rook.png"))));
    }


    /**
     * Method to set up the original grid at the start
     * @return the grid pane for the screen
     */
    private GridPane setUpGrid(){

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){

                // get the current piece
                Piece currentP = board.getPiece(i, j);

                // get the current piece string and make the image string
                String pieceString = currentP.toString();
                String imageString = currentP.getIsWhite() ? "w_" + pieceString  : "b_" + pieceString;

                // create the stackPane and the rectangle for the background
                StackPane temp = new StackPane();

                // set up the color and rectangle
                Rectangle rectangle = new Rectangle(64, 64);
                Color color = (i + j) % 2 == 0 ? Color.BLACK : Color.BEIGE;
                rectangle.setFill(color);

                // create the imageview from the imageString
                ImageView imageView = new ImageView();
                imageView = loadInImage(imageString);

                // load in an action event for every piece
                setUpActionEvent(temp);

                // if the piece is not in the hashmap put the stack pane in there
                if (pieces.getOrDefault(temp, null) == null){
                    int[] location = {i, j};
                    pieces.put(temp, location);
                }

                // add the rectangle and the imageview to the stack pane
                temp.getChildren().addAll(rectangle, imageView);

                // add the stack pane to the grid pane
                grid.add(temp, i, j);
            }
        }
        return grid;
    }


    /**
     * Helper method to load in image assets from the hashmap
     * @param imageString Image String for example : "b_Knight" = black Knight
     * @return The ImageView for the imageString
     */
    private ImageView loadInImage(String imageString){
        ImageView imageView = new ImageView();

        if (!imageString.contains("Blank")){
            // load all the images and set them here
            Image image = images.get(imageString);

            // check if the image is null
            if (image == null){
                System.out.println("NULL IMAGE");
            }

            imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);
        }

        return imageView;
    }



    /**
     * Method for redrawing the board based upon user clicks
     */
    private void updateBoard(){

        for(int rowT = 0; rowT < rows; rowT ++){
            for (int colT = 0; colT < cols; colT ++){
                // grab the current piece
                Piece currentP = board.getPiece(rowT, colT);

                // calculate the imageString from the piece String
                String pieceString = currentP.toString();
                String imageString = currentP.getIsWhite() ? "w_" + pieceString  : "b_" + pieceString;

                // calculate the 1D Index from the current 2D index
                int current1DIndex = colT  + rowT * 8;


                if((rowT == lastPiecePos[0] && colT == lastPiecePos[1])){
                    updateBHelper(Color.GREEN, current1DIndex, imageString);
                }

                else if (checkIfPossibleMovesContains(rowT, colT)){
                    updateBHelper(Color.RED, current1DIndex, imageString);
                }

                else {
                    Color color = (rowT + colT) % 2 == 0 ? Color.BLACK : Color.BEIGE;
                    updateBHelper(color, current1DIndex, imageString);
                }
            }
        }


//        board.printBoard();
    }


    private void updateBHelper(Color color, int current1DIndex, String imageString){
        if(grid.getChildren().get(current1DIndex) instanceof StackPane){
            StackPane currentStackPane = (StackPane) grid.getChildren().get(current1DIndex);

            // change the rect color
            Rectangle rect = (Rectangle) currentStackPane.getChildren().get(0);
            rect.setFill(color);

            // updating the current image that is in this square
            ImageView imageView = (ImageView) currentStackPane.getChildren().get(1);
            imageView.setImage(images.get(imageString));
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);

        };
    }


    /**
     * Helper function to check if the possible moves contain a certain row/ col
     * @param rowT the row in question
     * @param colT the col in question
     * @return true if the possible moves contain this position/ false if not
     */
    private boolean checkIfPossibleMovesContains(int rowT, int colT){
        for(ArrayList<Integer> possibleMove : lastPossibleMoves){
            if (possibleMove.get(0) == rowT && possibleMove.get(1) == colT){
                return true;
            }
        }
        return false;
    }

    /**
     * Method to help update the kings positions based upon moves
     * @param row new row
     * @param col new col
     * @param currentPiece the current piece
     */
    private void updateKingPos(int row, int col, Piece currentPiece){
        boolean isWhite = currentPiece.getIsWhite();
        if (currentPiece.toString().equals("King")){
            System.out.println("Updating the current position of the king to " + row + ", " + col);
            if (isWhite){
                board.setW_kingPos(row, col);
            }
            else {
                board.setB_kingPos(row, col);
            }
        }
    }


    /**
     * Method to handle updating the states based upon user input ( interactions )
     * @param rectangle ..
     */
    private void setUpActionEvent(StackPane rectangle){

        rectangle.setOnMouseClicked(mouseEvent -> {
            // Just store the current clicked on row and col
            int row = pieces.get(rectangle)[0];
            int col = pieces.get(rectangle)[1];

            // store the current piece
            Piece currentP = board.getPiece(row, col);


            // Enforce turn logic

            // boolean to check if the current piece is white
            boolean pieceIsWhite = false;

            // store the row and col in an array
            int[] location = new int[2];
            location[0] = row;
            location[1] = col;


            // need to update more states here
            if(location != lastPiecePos && checkIfPossibleMovesContains(row, col)){
                // just store the oldRow, and oldCol
                int oldRow = lastPiecePos[0];
                int oldCol = lastPiecePos[1];

                Piece lastP = board.getPiece(oldRow, oldCol);
                pieceIsWhite = lastP.getIsWhite();

                // need to say that we need to update the board state based upon the new strings
                // replace the current row/ col with the piece in question'
                // set the new piece here and the old piece location to blank
                board.setPiece(row, col, lastP);
                lastP.setPosition(row, col);

                board.setPiece(oldRow, oldCol, new Piece("Blank", true, oldRow, oldCol));

                // update the king positions in the board object
                updateKingPos(row, col, lastP);

                // set the first move to false after this
                lastP.getMovement().setFirstMove(false);

                currentP = board.getPiece(row, col);

                // update the location and the new available things
                // todo : might need to take turns instead
                lastPiecePos = location;
//                System.out.println( "THIS IS THE CURRENT PIECE "  +  board.getPiece(row, col).toString());
                lastPossibleMoves = new ArrayList<>();

                updateBoard();

                System.out.println("The color is white is  " + pieceIsWhite);
                int c = board.checkForMate(pieceIsWhite);

                if (c == 0){
                    // if we are here we want to stop the game
                    System.out.println("IN CHECK MATE");
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Check Mate");
//                    alert.setHeaderText(null);
//                    alert.setContentText("The color " + (!pieceIsWhite ? "white " : "black ") + "wins");
//                    alert.showAndWait();
                }

                else if (c == 1){
                    // if we are here we want to send a warning
                    System.out.println("IN CHECK");
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Check Mate");
//                    alert.setHeaderText(null);
//                    alert.setContentText("The color " + (!pieceIsWhite ? "white " : "black ") + "is in check");
//                    alert.showAndWait();

                }

                isWhitesTurn = !isWhitesTurn;
            }
            else if (location != lastPiecePos){

                if (!currentP.toString().equals("Blank")) {
                    if ((isWhitesTurn && !currentP.getIsWhite()) || (!isWhitesTurn && currentP.getIsWhite())) {
                        return;
                    }
                }
                lastPiecePos = location;
                lastPossibleMoves = currentP.getMovement().getValidMoves(board.getBoard(), row, col, currentP);

                updateBoard();
            }

//            updateBoard();


        });
    }

}
