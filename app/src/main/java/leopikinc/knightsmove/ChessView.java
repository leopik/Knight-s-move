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



public class ChessView extends View{

    protected piece black, white, current; // is not current is previous player's
    protected boolean isSelected, isOver;
    protected Core core;
    protected Canvas canvas;
    protected int sizeoffield;
    protected int dy;
    protected int sizeofblock;
    protected int boardX, boardY;
    protected static String TAG = "ChessView class";

    public Core getCore() {
        return core;
    }

    @Override
    protected void onDraw(Canvas canvas){
        this.canvas = canvas;
        drawBoard(core.getBoard());
        Log.d(TAG, "onDraw called");
        if (isSelected){
            drawAvailableMoves(core.getBoard(), boardX, boardY);
            Log.d(TAG,"drawAvailableMoves called");
        }
        if (isOver || current.getAvailableMoves().size() == 0) {
            finishGame();
        }
    }

    protected void drawAvailableMoves(cell[][] board, int x, int y){

        piece piece = board[x][y].getPiece();
        Paint lightgrey = new Paint();
        lightgrey.setColor(Color.GRAY);
        lightgrey.setAlpha(128);
        for (int i = 0; i < piece.getAvailableMoves().size(); i++) {
            cell cell = piece.getAvailableMoves().get(i);
            canvas.drawCircle(cell.getX()*sizeofblock+sizeofblock/2, dy+cell.getY()*sizeofblock+sizeofblock/2, 0.5f*sizeofblock/2, lightgrey);
        }

    }

    protected void drawBoard(cell[][] board){

        Log.d(TAG, "drawBoard called");

        Paint black = new Paint();
        Paint white = new Paint();
        Paint darkgrey = new Paint();
        Paint lightgrey = new Paint();

        black.setColor(Color.BLACK);
        white.setColor(Color.WHITE);
        darkgrey.setColor(Color.DKGRAY);
        lightgrey.setColor(Color.LTGRAY);

        // dy - how far top side of field from screen
        sizeoffield = canvas.getWidth();
        dy = (canvas.getHeight()-sizeoffield)/2;
        sizeofblock = sizeoffield/core.getFieldSize();

        for (int i = 0; i < core.getFieldSize() ; i++) {
            for (int j = 0; j < core.getFieldSize(); j++) {
                canvas.drawRect(0 + i * sizeofblock
                        , dy + j * sizeofblock
                        , sizeofblock + i * sizeofblock
                        , dy + sizeofblock + j * sizeofblock
                        , (i + j) % 2 == 1 ? lightgrey : darkgrey);

                if (board[i][j].getPiece()!=null){
                    canvas.drawCircle(i*sizeofblock+sizeofblock/2
                            , dy+j*sizeofblock+sizeofblock/2
                            , 0.95f*sizeofblock/2
                            , (board[i][j].getPiece().getPieceColour() == Core.objectColour.black) ? black : white);

                    //TODO: images
                }

            }
        }

    }

    protected void finishGame(){
        Toast.makeText(getContext(), "Game over, won " + core.getTurn(), Toast.LENGTH_SHORT).show();
        isOver = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        if (isOver) {
            // game has already ended
            finishGame();
            return super.onTouchEvent(event);
        }

        int targetX = (int) (event.getX()*core.getFieldSize()/sizeoffield);
        int targetY = (int) ((event.getY()-dy)*core.getFieldSize()/sizeoffield);

        // checks bounds
        if (targetX < core.getFieldSize()
                && targetX >= 0
                && targetY < core.getFieldSize()
                && targetY >= 0) {

            // if not selected yet
            if (!isSelected) {
                if (core.getBoard()[targetX][targetY].getPiece() != null && core.getBoard()[targetX][targetY].getPiece().getPieceColour() == core.getTurn()) {
                    isSelected = true;
                    boardX = targetX;
                    boardY = targetY;
                    invalidate();
                    return true;
                }
            } else {
                int success = core
                        .getBoard()[boardX][boardY]
                        .getPiece()
                        .move(core.getBoard()[targetX][targetY]);
                Log.d(TAG, "Tryed to move");
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
        current = core.getWhitepiece();
        isOver = false;
        isSelected = false;

    }
}
