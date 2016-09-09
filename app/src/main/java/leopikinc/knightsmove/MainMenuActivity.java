package leopikinc.knightsmove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** Main menu */
public class MainMenuActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainMenuActivity class"; // Used for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button buttonPlayPVP = (Button) findViewById(R.id.ButtonPlayPVPid);
        Button buttonPlayPVE = (Button) findViewById(R.id.ButtonPlayPVEid);
        Button buttonRules = (Button) findViewById(R.id.ButtonRulesid);

        buttonPlayPVP.setOnClickListener(this);
        buttonPlayPVE.setOnClickListener(this);
        buttonRules.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        EditText fieldSizeText = (EditText) findViewById(R.id.getfieldsizetext);
        int fieldSize;

        switch (view.getId()) {
            case R.id.ButtonRulesid:
                intent = new Intent(this, RulesActivity.class);
                startActivity(intent);
                break;

            case R.id.ButtonPlayPVPid:
                try {
                    fieldSize = Integer.parseInt(fieldSizeText.getText().toString());
                } catch (NumberFormatException exception){
                    fieldSize = 8;
                }
                if (fieldSize<2)
                    fieldSize = 8; // minimum field size is 2

                intent = new Intent(this,OneVSOne.class);
                intent.putExtra("fieldSize", fieldSize);
                startActivity(intent);
                break;

            case R.id.ButtonPlayPVEid:
                try {
                    fieldSize = Integer.parseInt(fieldSizeText.getText().toString());
                }catch (Exception ex){
                    fieldSize = 8;
                }
                if (fieldSize<2)
                    fieldSize = 8;

                intent = new Intent(this,PlayerVSComputer.class);
                intent.putExtra("fieldSize", fieldSize);
                startActivity(intent);
                break;
        }
    }
    //TODO: add images
    //TODO: highlight chosen figure?
}
