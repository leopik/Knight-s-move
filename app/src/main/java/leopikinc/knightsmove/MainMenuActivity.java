package leopikinc.knightsmove;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainMenuActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button ButtonPlayPVP = (Button) findViewById(R.id.ButtonPlayPVPid);
        ButtonPlayPVP.setOnClickListener(this);
        Button ButtonPlayPVE = (Button) findViewById(R.id.ButtonPlayPVEid);
        ButtonPlayPVE.setOnClickListener(this);
        Button ButtonRules = (Button) findViewById(R.id.ButtonRulesid);
        ButtonRules.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        EditText fieldsizetext = (EditText) findViewById(R.id.getfieldsizetext);
        int fieldsize;
        switch (v.getId()){
            case R.id.ButtonRulesid:
                i = new Intent(this, RulesActivity.class);
                startActivity(i);
                break;

            case R.id.ButtonPlayPVPid:
                try {
                    fieldsize = Integer.parseInt(fieldsizetext.getText().toString());
                }catch (Exception ex){
                    fieldsize = 8;
                }
                if (fieldsize<2)
                    fieldsize = 8;

                i = new Intent(this,OneVSOne.class);
                i.putExtra("fieldsize", fieldsize);
                startActivity(i);
                break;

            case R.id.ButtonPlayPVEid:
                try {
                    fieldsize = Integer.parseInt(fieldsizetext.getText().toString());
                }catch (Exception ex){
                    fieldsize = 8;
                }
                if (fieldsize<2)
                    fieldsize = 8;

                i = new Intent(this,PlayerVSComputer.class);
                i.putExtra("fieldsize", fieldsize);
                startActivity(i);
                break;

        }
    }
    //TODO: add images
    //TODO: PvE
    //TODO: highlight chosen figure?
    //TODO: normally comment
}
