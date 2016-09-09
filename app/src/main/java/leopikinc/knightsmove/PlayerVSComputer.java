package leopikinc.knightsmove;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** Activity which holds PvE instance */
public class PlayerVSComputer extends AppCompatActivity {

    Core.objectColour playerColor;
    int fieldsize;
    Context context;
    ChessViewComputer ChessBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        fieldsize = i.getIntExtra("fieldsize",8);
        context = this;

        Core core = new Core(fieldsize);
        ChessBoard = new ChessViewComputer(context, core);
        setContentView(ChessBoard);

        // Dialog asks player which side user wants to play
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Выбор фигуры");  // Title
        alertDialog.setMessage("Выберите за кого вы будете играть"); // Message
        alertDialog.setPositiveButton("Белый", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                playerColor = Core.objectColour.white;
                ChessBoard.setMoveOrder(playerColor);
            }
        });
        alertDialog.setNegativeButton("Чёрный", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                playerColor = Core.objectColour.black;
                ChessBoard.setMoveOrder(playerColor);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
}
