package leopikinc.knightsmove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class OneVSOne extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        int fieldsize = i.getIntExtra("fieldsize",8);
        Core core = new Core(fieldsize);
        ChessView ChessBoard = new ChessView(this, core);
        setContentView(ChessBoard);
    }

}
