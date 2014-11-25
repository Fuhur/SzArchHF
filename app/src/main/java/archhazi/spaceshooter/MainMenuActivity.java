package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileInputStream;
import java.io.InputStreamReader;


public class MainMenuActivity extends Activity {

    public final static String SEED_KEY = "SEED_KEY";
    public final static String LENGTH_KEY = "LENGTH_KEY";
    public final static String MULTIPLAYER_KEY = "MULTIPLAYER_KEY";

    public final static String FILE_NAME = "settings.txt";
    public static String PLAYER_NAME;
    private static boolean NAME_SET = false;

    public void startSinglePlayerGame(View view){
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(SEED_KEY,0);
        intent.putExtra(LENGTH_KEY,35f);
        intent.putExtra(MULTIPLAYER_KEY,false);

        startActivity(intent);
    }

    public void startMultiPlayerGame(View view){
        Intent intent = new Intent(this, GameActivity.class);

        // TO DO
        // HANDSHAKING
        intent.putExtra(SEED_KEY,0);
        intent.putExtra(LENGTH_KEY,30f);
        intent.putExtra(MULTIPLAYER_KEY,true);

        startActivity(intent);
    }

    private boolean loadName(){
        String s="";
        try {
            FileInputStream fileIn=openFileInput(FILE_NAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];

            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
        } catch (Exception e) {
            return false;
        }

        setName(s);

        return true;
    }

    public void setName(String name){
        PLAYER_NAME = name;
        NAME_SET = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Try and load the name
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
