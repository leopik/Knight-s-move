package leopikinc.knightsmove;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Leopik on 15.05.2016.
 */
public class ChessViewComputer extends ChessView {
    protected static String TAG = "ChessViewComputer class";
    protected Core.objectColour player;
    protected Core.piece playerpiece;
    protected Core.piece computerpiece;

    public void setMoveOrder(Core.objectColour color) {
        player = color;
        if (color == Core.objectColour.black) {
            playerpiece = core.getBlackpiece();
            computerpiece = core.getWhitepiece();
        } else {
            playerpiece = core.getWhitepiece();
            computerpiece = core.getBlackpiece();
        }


        if (player == Core.objectColour.black) {
            move();
            invalidate();

        }
    }

    ChessViewComputer(Context context, Core core) {
        super(context, core);
    }

    private void move() {
        Log.d("called move", "whay");
        if (isOver || current.getAvailableMoves().size() == 0) {
            finishGame();
            return;
        }
        // if only one available move - do it
        if (computerpiece.getAvailableMoves().size() == 1) {
            int success = computerpiece.move(computerpiece.getAvailableMoves().get(0));
            switch (success) {
                case 0:
                    isOver = true;
                    break;
                case 1:
                    core.changeTurn();
                    current = computerpiece;
                    break;
                case 2:
                    Log.d("switch1","didnot work");
                    break;
            }
            invalidate();
            return;
        }
        else {
            int xmax = 0;
            int ymax = 0;
            for (int i = 0; i < computerpiece.getAvailableMoves().size(); i++) {
                if (computerpiece.getAvailableMoves().get(i).getPiece() != null) {
                    int success = computerpiece.move(computerpiece.getAvailableMoves().get(i));
                    switch (success) {
                        case 0:
                            isOver = true;
                            break;
                        case 1:
                            core.changeTurn();
                            current = computerpiece;
                            break;
                        case 2:
                            Log.d("switch2","didnot work");
                            break;
                    }
                    invalidate();
                    return;
                }

                // check if move is under attack
                boolean isunderattack = false;
                for (int j = 0; j < playerpiece.getAvailableMoves().size(); j++) {
                    if (playerpiece.getAvailableMoves().get(j) == computerpiece.getAvailableMoves().get(i))
                        isunderattack = true;
                }

                // if it's not under attack and has big distance
                if (!isunderattack && ((computerpiece.getAvailableMoves().get(i).getX() + Math.abs(computerpiece.getAvailableMoves().get(i).getY() - core.getFieldSize() +1)) > xmax + ymax)){
                    xmax = computerpiece.getAvailableMoves().get(i).getX();
                    ymax = Math.abs(computerpiece.getAvailableMoves().get(i).getY() - core.getFieldSize() +1);
                }
            }

            int success = computerpiece.move(core.getBoard()[xmax][Math.abs(ymax - core.getFieldSize() +1)]);
            switch (success) {
                case 0:
                    isOver = true;
                    break;
                case 1:
                    core.changeTurn();
                    current = computerpiece;//core.getBoard()[xmax][Math.abs(ymax - core.getFieldSize() +1)].getPiece();
                    break;
                case 2:
                    Log.d("switch3","didnot work");
                    break;
            }
            invalidate();
            return;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

//        if (//current != playerpiece) {
//
//            return false;//super.onTouchEvent(event);
//        }
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
                Log.d(TAG, "Tryed to move"+ success);
                switch (success) {
                    case 0:
                        isOver = true;
                        break;
                    case 1:
                        core.changeTurn();
                        current = core.getBoard()[targetX][targetY].getPiece();
                        invalidate();
                        move();
                        break;
                }
            }
        }
        isSelected=false;
        invalidate();
        return true;
    }
}
