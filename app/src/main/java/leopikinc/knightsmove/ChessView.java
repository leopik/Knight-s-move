package leopikinc.knightsmove;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import leopikinc.knightsmove.Core.*;

/**
 * View class for PvP instance
 */
public class ChessView extends View{

    protected Piece current; // is not current is previous player's
    protected boolean isSelected; // whether chess figure is selected or not
    protected boolean isOver; // game over boolean
    protected Core core;
    protected Canvas canvas;
    protected int sizeOfField; // in pixels
    protected int dy; // offset from top of screen
    protected int sizeOfBlock; // in pixels
    protected int boardX, boardY;
    protected static final String TAG = "ChessView class";

    public Core getCore() {
        return core;
    }

    @Override
    protected void onDraw(Canvas canvas){

        this.canvas = canvas;
        drawBoard(core.getBoard());
        if (isSelected){
            drawAvailableMoves(core.getBoard(), boardX, boardY);
        }
        if (isOver || current.getAvailableMoves().size() == 0) {
            finishGame();
        }
    }

    protected void drawAvailableMoves(Cell[][] board, int x, int y){

        Piece piece = board[x][y].getPiece();

        Paint lightGrey = new Paint();
        lightGrey.setColor(Color.GRAY);
        lightGrey.setAlpha(128);

        for (int i = 0; i < piece.getAvailableMoves().size(); i++) {
            Cell cell = piece.getAvailableMoves().get(i);
            canvas.drawCircle(cell.getXCord()* sizeOfBlock + sizeOfBlock /2,
                    dy+cell.getYCord()* sizeOfBlock + sizeOfBlock /2,
                    0.5f* sizeOfBlock /2,
                    lightGrey);
        }

    }

    protected void drawBoard(Cell[][] board){

        float figureSize = 0.95f;
        Paint black = new Paint();
        Paint white = new Paint();
        Paint darkGrey = new Paint();
        Paint lightGrey = new Paint();

        black.setColor(Color.BLACK);
        white.setColor(Color.WHITE);
        darkGrey.setColor(Color.DKGRAY);
        lightGrey.setColor(Color.LTGRAY);

        // dy - offset from top
        sizeOfField = canvas.getWidth();
        dy = (canvas.getHeight()- sizeOfField)/2;
        sizeOfBlock = sizeOfField /core.getFieldSize();

        // Draws board itself
        for (int i = 0; i < core.getFieldSize() ; i++) {
            for (int j = 0; j < core.getFieldSize(); j++) {
                canvas.drawRect(0 + i * sizeOfBlock,
                        dy + j * sizeOfBlock,
                        sizeOfBlock + i * sizeOfBlock,
                        dy + sizeOfBlock + j * sizeOfBlock,
                        (i + j) % 2 == 1 ? lightGrey : darkGrey);

                // if there is a figure on current cell - draw it too
                if (board[i][j].getPiece()!=null){
                    canvas.drawCircle(i* sizeOfBlock + sizeOfBlock /2,
                            dy+j* sizeOfBlock + sizeOfBlock /2,
                            figureSize* sizeOfBlock /2,
                            // whether draw black or white figure
                            (board[i][j].getPiece().getPieceColour() == Core.objectColour.black) ? black : white);
                }
            }
        }
    }

    protected void finishGame(){
        Toast.makeText(getContext(), "Game over, won " + core.getTurn(), Toast.LENGTH_SHORT).show();
        isOver = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        // game has already ended
        if (isOver) {
            finishGame();
            return super.onTouchEvent(event);
        }

        int targetX = (int) (event.getX()*core.getFieldSize()/ sizeOfField); // X cord of cell  which player pressed
        int targetY = (int) ((event.getY()-dy)*core.getFieldSize()/ sizeOfField); // Y cord of cell which player pressed

        // checks bounds
        if (targetX < core.getFieldSize()
                && targetX >= 0
                && targetY < core.getFieldSize()
                && targetY >= 0) {

            // if not selected yet
            if (!isSelected) {
                // if cell which player pressed contains figure and current turn belongs to this figure
                if (core.getBoard()[targetX][targetY].getPiece() != null
                            && core.getBoard()[targetX][targetY].getPiece().getPieceColour() == core.getTurn()) {
                    isSelected = true;
                    boardX = targetX;
                    boardY = targetY;
                    invalidate();
                    return true;
                }
            // else - try to move to position where player pressed
            } else {
                int success = core
                        .getBoard()[boardX][boardY]
                        .getPiece()
                        .move(core.getBoard()[targetX][targetY]);

                switch (success) {
                    case 0:
                        isOver = true;
                        break;
                    case 1:
                        core.changeTurn();
                        current = core.getBoard()[targetX][targetY].getPiece();
                        break;
                }
            }
        }
        isSelected=false;
        invalidate();
        return true;
    }

    public ChessView(Context context, Core core){

        super(context);
        this.core = core;
        current = core.getWhitePiece();
        isOver = false;
        isSelected = false;

    }
}
