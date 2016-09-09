package leopikinc.knightsmove;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/** Class containing board logic */
public class Core {

    private Piece blackPiece, whitePiece;
    private static final String TAG = "Core class"; // for logging

    public Piece getBlackPiece(){
        return blackPiece;
    }
    public Piece getWhitePiece() {
        return whitePiece;
    }

    public enum objectColour {
        black, white
    }
    /** Every cell on chessboard is implmented with this class */
    public class Cell {

        private int xCord, yCord;
        private Piece piece;

        public int getXCord() {
            return xCord;
        }
        public int getYCord() {
            return yCord;
        }
        public Piece getPiece() {
            return piece;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
        }

        Cell(int xCord, int yCord) {
            this.xCord = xCord;
            this.yCord = yCord;
            this.piece = null;
        }
    }

    /**
     * Chess figure class
     */
    public class Piece {

        private Cell location;
        private objectColour colour;
        private ArrayList<Cell> availableMoves = new ArrayList<>();

        public Cell getLocation() {
            return location;
        }
        public objectColour getPieceColour() {
            return colour;
        }
        public ArrayList<Cell> getAvailableMoves(){
            return availableMoves;
        }

        private void findAvailMoves(){

            availableMoves.clear();

            int xLoc = location.getXCord();
            int yLoc = location.getYCord();

            if ((xLoc-2 >= 0) && (yLoc-1 >= 0))
                availableMoves.add(board[xLoc-2][yLoc-1]); // Left + Down move

            if ((xLoc-2 >= 0) && (yLoc+1 < fieldsize))
                availableMoves.add(board[xLoc-2][yLoc+1]); // Left + Up move

            if ((xLoc-1 >= 0) && (yLoc+2 < fieldsize))
                availableMoves.add(board[xLoc-1][yLoc+2]); // Down + Left move

            if ((yLoc+2 < fieldsize) && (xLoc+1 < fieldsize))
                availableMoves.add(board[xLoc+1][yLoc+2]); // Down + Right move
        }

        /**
         * 0 means one knight ate another
         * 1 means successful move
         * 2 means unsuccessful move
         */
        public byte move(Cell targetMove){

            if (availableMoves.contains(targetMove)) {

                // if smth exists on cell and it's not player's figure
                if ((targetMove.getPiece() != null) && (targetMove.getPiece() != this)) {
                    targetMove.setPiece(this); // move figure to another cell
                    this.location.setPiece(null);
                    this.location = targetMove;
                    return 0;
                }

                targetMove.setPiece(this); // move figure to another cell
                this.location.setPiece(null);
                this.location = targetMove;
                findAvailMoves(); // find new available moves
                return 1;
            }
            return 2;
        }

        Piece(objectColour colour, Cell location) {

            this.colour = colour;
            this.location = location;
            findAvailMoves();
        }

    }

    private Cell[][] board;
    private objectColour turn;
    private int fieldsize;

    public Cell[][] getBoard() {
        return board;
    }
    public objectColour getTurn() {
        return turn;
    }
    public int getFieldSize() {
        return fieldsize;
    }

    public void changeTurn(){

        if (getTurn() == objectColour.black)
            turn = objectColour.white;
        else
            turn = objectColour.black;
    }

    void populateBoard(Cell[][] board, int fieldsize) {

        // init the cells
        for (int x = 0; x < fieldsize; x++) {
            for (int y = 0; y < fieldsize; y++) {
                board[x][y] = new Cell(x, y);
            }
        }

        // init random positions
        Random random = new Random();
        int pos1x = random.nextInt(fieldsize-2)+2;
        int pos2x = random.nextInt(fieldsize-2)+2;
        int pos1y = random.nextInt(fieldsize-2)+2;
        int pos2y;
        if (pos1x == pos2x){
            do{
                pos2y = random.nextInt(fieldsize-2)+2;
            } while (pos2y==pos1y);
        }
        else{
            pos2y=random.nextInt(fieldsize);
        }

        // create chess figures and locate them on chess board
        whitePiece = new Piece(objectColour.white,board[pos1x][pos1y]);
        blackPiece = new Piece(objectColour.black,board[pos2x][pos2y]);
        board[pos1x][pos1y].setPiece(whitePiece);
        board[pos2x][pos2y].setPiece(blackPiece);
    }

    Core(int fieldsize) {

        this.fieldsize = fieldsize;
        board = new Cell[fieldsize][fieldsize];
        Log.d(TAG, "Board created");
        populateBoard(board,fieldsize);
        turn = objectColour.white;
    }
}
