import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends Application {

    private Board board = new Board();
    private int rows = 8;
    private int cols = 8;
    private int screenWidth = 500;
    private int screenHeight = 500;
    private GridPane grid = new GridPane();
    private HashMap<StackPane, int[]> pieces = new HashMap<>();
    private int[] lastPiecePos = null;
    private ArrayList<ArrayList<Integer>> lastPossibleMoves = new ArrayList<>();

//    private Color lastPieceColor = null;


//    private boolean isOnPiece = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // make the initial board here
        board.makeInitialBoard();

        GridPane grid = setUpGrid();

        // need to get the piece that I clicked on somehow


        primaryStage.setScene(new Scene(grid, screenWidth, screenHeight));
        primaryStage.show();
    }


    private GridPane setUpGrid(){

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Piece currentP = board.getPiece(i, j);
                Text text = new Text(currentP.toString());

                StackPane temp = new StackPane();
                Rectangle rectangle = new Rectangle(62.5, 62.5);

                // setup

                setUpActionEvent(temp);


                if (pieces.getOrDefault(temp, null) == null){
                    int[] location = {i, j};
                    pieces.put(temp, location);
                }


                Color color = (i + j) % 2 == 0 ? Color.BEIGE : Color.SADDLEBROWN;
                rectangle.setFill(color);

                temp.getChildren().addAll(rectangle, text);

                grid.add(temp, i, j);
            }
        }


        return grid;
    }


    /**
     * Method for redrawing the board based upon user clicks
     */
    private void updateBoard(){
        Color temp = Color.BLACK;

        System.out.println("HERE");
        for(int rowT = 0; rowT < rows; rowT ++){
            for (int colT = 0; colT < cols; colT ++){
                Piece currentP = board.getPiece(rowT, colT);
                Text updatedText = new Text(currentP.toString());

                int current1DIndex = colT  + rowT * 8;


                if((rowT == lastPiecePos[0] && colT == lastPiecePos[1])){
                    // we want to update the color to a new color of the current piece
                    if(grid.getChildren().get(current1DIndex) instanceof StackPane){
                        StackPane currentStackPane = (StackPane) grid.getChildren().get(current1DIndex);

                        // change the rect color
                        Rectangle rect = (Rectangle) currentStackPane.getChildren().get(0);
                        rect.setFill(Color.GREEN);

                        Text text = ((Text) ((StackPane) grid.getChildren().get(current1DIndex)).getChildren().get(1));
                        System.out.println("THIS IS THE TEXT : " + updatedText.toString());
                        text.setText(updatedText.getText());

                    };
                }


                else if (checkIfPossibleMovesContains(rowT, colT)){
                    if(grid.getChildren().get(current1DIndex) instanceof StackPane){
                        StackPane currentStackPane = (StackPane) grid.getChildren().get(current1DIndex);

                        // change the rect color
                        Rectangle rect = (Rectangle) currentStackPane.getChildren().get(0);
                        rect.setFill(Color.RED);

                        Text text = ((Text) ((StackPane) grid.getChildren().get(current1DIndex)).getChildren().get(1));
                        text.setText(updatedText.getText());

                    };
                }


                else {
                    if(grid.getChildren().get(current1DIndex) instanceof StackPane){
                        StackPane currentStackPane = (StackPane) grid.getChildren().get(current1DIndex);

                        // change the rect color
                        Rectangle rect = (Rectangle) currentStackPane.getChildren().get(0);
                        Color color = (rowT + colT) % 2 == 0 ? Color.BEIGE : Color.SADDLEBROWN;
                        rect.setFill(color);

                        //
                        Text text = ((Text) ((StackPane) grid.getChildren().get(current1DIndex)).getChildren().get(1));
                        text.setText(updatedText.getText());

                    };
                }
            }
        }
    }

    private boolean checkIfPossibleMovesContains(int rowT, int colT){
        for(ArrayList<Integer> possibleMove : lastPossibleMoves){
            if (possibleMove.get(0) == rowT && possibleMove.get(1) == colT){
                return true;
            }
        }
        return false;
    }


    /**
     * Method to handle updating the states based upon user input ( interactions )
     * @param rectangle ..
     */
    private void setUpActionEvent(StackPane rectangle){

        rectangle.setOnMouseClicked(mouseEvent -> {

            int row = pieces.get(rectangle)[0];
            int col = pieces.get(rectangle)[1];

            Piece currentP = board.getPiece(row, col);


            int[] location = new int[2];
            location[0] = row;
            location[1] = col;



            // need to update more states here
            if(location != lastPiecePos && checkIfPossibleMovesContains(row, col)){
                Piece lastP = board.getPiece(lastPiecePos[0], lastPiecePos[1]);

                // need to say that we need to update the board state based upon the new strings

                // replace the current row/ col with the piece in question'
                System.out.println("This is the last piece " + lastPiecePos[0] + " and " + lastPiecePos[1]);

                // set the new piece here and the old piece location to blank
                board.setPiece(row, col, lastP);
                board.setPiece(lastPiecePos[0], lastPiecePos[1], new Piece("Blank", true));

                // set the first move to false after this
                lastP.getMovement().setFirstMove(false);

                // update the location and the new available things
                // todo : might need to take turns instead
                lastPiecePos = location;
                lastPossibleMoves = currentP.getMovement().getValidMoves(board.getBoard(), row, col, currentP);


            }
            else if (location != lastPiecePos){
                lastPiecePos = location;
                lastPossibleMoves = currentP.getMovement().getValidMoves(board.getBoard(), row, col, currentP);
            }

            updateBoard();


        });
    }

}
