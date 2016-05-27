package leopikinc.knightsmove;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayerVSComputer extends AppCompatActivity {

    Core.objectColour playercolor;
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

        // Dialog asks player which side he wants to play
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Выбор фигуры");  // заголовок
        ad.setMessage("Выберите за кого вы будете играть"); // сообщение
        ad.setPositiveButton("Белый", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                playercolor = Core.objectColour.white;
                ChessBoard.setMoveOrder(playercolor);
            }
        });
        ad.setNegativeButton("Чёрный", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                playercolor = Core.objectColour.black;
                ChessBoard.setMoveOrder(playercolor);
            }
        });
        ad.setCancelable(false);
        ad.show();

    }
}
