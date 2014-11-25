package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class SettingsActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getActionBar().hide();
    }

    public void saveName(View view){
        EditText nameText = (EditText) findViewById(R.id.name_input);
        String name = nameText.getText().toString();

        SharedPreferences settings = getSharedPreferences(MainMenuActivity.USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MainMenuActivity.PLAYER_NAME_KEY,name);
        editor.commit();

        Intent intent = new Intent();
        intent.putExtra(MainMenuActivity.NAME_SETTING_KEY, name);
        setResult(RESULT_OK, intent);
        finish();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
