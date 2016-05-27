package leopikinc.knightsmove;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


public class Core {

    private piece blackpiece,whitepiece;

    public piece getBlackpiece(){
        return blackpiece;
    }

    public piece getWhitepiece() {
        return whitepiece;
    }

    private static String TAG = "Core class";

    public enum objectColour {
        black, white
    }

    public class cell {
        private int x;

        public int getX() { return x; }

        private int y;

        public int getY() { return y; }

        private piece piece;

        public piece getPiece() {
            return piece;
        }

        public void setPiece(piece piece) {
            this.piece = piece;
        }

        cell(int x, int y) {
            this.x = x;
            this.y = y;
            this.piece = null;
        }
    }


    public class piece {

        private cell location;

        public cell getLocation() { return location; }

        private objectColour colour;

        public objectColour getPieceColour() {
            return colour;
        }

        private ArrayList<cell> availableMoves = new ArrayList<>();

        public ArrayList<cell> getAvailableMoves(){
            return availableMoves;
        }

        private void findAvailMoves(){

            availableMoves.clear();

            int xLoc = location.getX();
            int yLoc = location.getY();

            if ((xLoc-2 >= 0) && (yLoc-1 >= 0))
                availableMoves.add(board[xLoc-2][yLoc-1]);

            if ((xLoc-2 >= 0) && (yLoc+1 < fieldsize))
                availableMoves.add(board[xLoc-2][yLoc+1]);

            if ((xLoc-1 >= 0) && (yLoc+2 < fieldsize))
                availableMoves.add(board[xLoc-1][yLoc+2]);

            if ((yLoc+2 < fieldsize) && (xLoc+1 < fieldsize))
                availableMoves.add(board[xLoc+1][yLoc+2]);

        }

        // 0 means one knight ate another
        // 1 means successful move
        // 2 means unsuccessful move
        public byte move(cell targetmove){
            if ( availableMoves.contains(targetmove)){
                if ((targetmove.getPiece()!=null) && (targetmove.getPiece()!=this)) {
                    targetmove.setPiece(this);
                    this.location.setPiece(null);
                    this.location = targetmove;
                    return 0;
                }
                targetmove.setPiece(this);
                this.location.setPiece(null);
                this.location = targetmove;
                findAvailMoves();
                return 1;
            }
            return 2;
        }

        // CREATOR
        piece(objectColour colour, cell location) {
            this.colour = colour;
            this.location = location;
            findAvailMoves();
        }

    }


    private cell board[][];
//    private player white, black;
    private objectColour turn;
    private int fieldsize;

    public cell[][] getBoard() {
        return board;
    }

    public objectColour getTurn() {
        return turn;
    }

    public void changeTurn(){
        if (getTurn() == objectColour.black)
            turn = objectColour.white;
        else
            turn = objectColour.black;
    }

    public int getFieldSize() { return fieldsize; }


    void populateBoard(cell[][] board,int fieldsize) {



        // init the cells
        for (int x = 0; x < fieldsize; x++) {
            for (int y = 0; y < fieldsize; y++) {
                board[x][y] = new cell(x, y);
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

        whitepiece = new piece(objectColour.white,board[pos1x][pos1y]);
        blackpiece = new piece(objectColour.black,board[pos2x][pos2y]);
        board[pos1x][pos1y].setPiece(whitepiece);
        board[pos2x][pos2y].setPiece(blackpiece);

        Log.d(TAG, "Positions of players are set");

//        white = new player(objectColour.white, whitepiece);
//        black = new player(objectColour.black, blackpiece);
    }


    // CREATOR
    Core(int fieldsize) {
        this.fieldsize = fieldsize;
        board = new cell[fieldsize][fieldsize];
        Log.d(TAG, "Board created");
        populateBoard(board,fieldsize);
        turn = objectColour.white;
    }
}
