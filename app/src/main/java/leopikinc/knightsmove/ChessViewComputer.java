package leopikinc.knightsmove;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

/**
 * View class for PvE instance
 */
public class ChessViewComputer extends ChessView {

    protected static final String TAG = "ChessViewComputer class";
    protected Core.objectColour playerColour;
    protected Core.Piece playerPiece;
    protected Core.Piece computerPiece;

    public void setMoveOrder(Core.objectColour color) {

        playerColour = color;
        if (color == Core.objectColour.black) {
            playerPiece = core.getBlackPiece();
            computerPiece = core.getWhitePiece();
        } else {
            playerPiece = core.getWhitePiece();
            computerPiece = core.getBlackPiece();
        }

        // if player have chosen to play black, then computer does first move
        if (playerColour == Core.objectColour.black) {
            move();
            invalidate();
        }
    }

    // method for computer's move
    private void move() {

        // check whether game is over or not
        if (isOver || current.getAvailableMoves().size() == 0) {
            finishGame();
            return;
        }

        // if only one move is available  - do it
        if (computerPiece.getAvailableMoves().size() == 1) {
            int success = computerPiece.move(computerPiece.getAvailableMoves().get(0));
            switch (success) {
                case 0:
                    isOver = true;
                    break;
                case 1:
                    core.changeTurn();
                    current = computerPiece;
                    break;
            }
            invalidate();
            return;
        // else - find cell which is farthest from the left bottom corner
        } else {
            int xMax = 0;
            int yMax = 0;

            // check all available moves
            for (int i = 0; i < computerPiece.getAvailableMoves().size(); i++) {
                // if on one of available moves we find opponents figure - eat it
                if (computerPiece.getAvailableMoves().get(i).getPiece() != null) {
                    int success = computerPiece.move(computerPiece.getAvailableMoves().get(i));

                    switch (success) {
                        case 0:
                            isOver = true;
                            break;
                        case 1:
                            core.changeTurn();
                            current = computerPiece;
                            break;
                    }
                    invalidate();
                    return;
                }

                // check if move is under attack
                boolean isUnderAttack = false;
                for (int j = 0; j < playerPiece.getAvailableMoves().size(); j++) {
                    if (playerPiece.getAvailableMoves().get(j) == computerPiece.getAvailableMoves().get(i))
                        isUnderAttack = true;
                }

                // if it's not under attack and has biggest distance than save this position
                if (!isUnderAttack && ((computerPiece.getAvailableMoves().get(i).getXCord() + Math.abs(computerPiece.getAvailableMoves().get(i).getYCord() - core.getFieldSize() +1)) > xMax + yMax)){
                    xMax = computerPiece.getAvailableMoves().get(i).getXCord();
                    yMax = Math.abs(computerPiece.getAvailableMoves().get(i).getYCord() - core.getFieldSize() +1);
                }
            }

            // finally move to the best suited position
            int success = computerPiece.move(core.getBoard()[xMax][Math.abs(yMax - core.getFieldSize() +1)]);

            switch (success) {
                case 0:
                    isOver = true;
                    break;
                case 1:
                    core.changeTurn();
                    current = computerPiece;
                    break;
            }
            invalidate();
            return;
        }
    }

    /**
     * Same as parent's method, but with only change that after player's successful move,
     * called computer's "move" method
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        if (isOver) {
            // game has already ended
            finishGame();
            return super.onTouchEvent(event);
        }

        int targetX = (int) (event.getX()*core.getFieldSize()/ sizeOfField); // X cord of cell  which player pressed
        int targetY = (int) ((event.getY()-dy)*core.getFieldSize()/ sizeOfField); // Y cord of cell  which player pressed

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
                        invalidate();
                        move(); // make computer's move
                        break;
                }
            }
        }
        isSelected=false;
        invalidate();
        return true;
    }

    public ChessViewComputer(Context context, Core core) {
        super(context, core);
    }
}
