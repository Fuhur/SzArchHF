package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainMenuActivity extends Activity {

    public final static String SEED_KEY = "SEED_KEY";
    public final static String START_TIME_KEY = "START_TIME_KEY";
    public final static String LENGTH_KEY = "LENGTH_KEY";
    public final static String MULTIPLAYER_KEY = "MULTIPLAYER_KEY";
    public static final String PLAYER_NAME_KEY = "PLAYER_NAME_KEY";
    public static final String NAME_SETTING_KEY = "NAME_SETTING_KEY";

    public final static String USER_INFO = "UserInfo";

    private String PLAYER_NAME;
    private static boolean NAME_SET = false;

    public void startSinglePlayerGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(SEED_KEY, 0);
        intent.putExtra(LENGTH_KEY, GameActivity.PRACTICE_LENGTH);
        intent.putExtra(MULTIPLAYER_KEY, false);

        startActivity(intent);
    }

    public void startMultiPlayerGame(View view) {
        startActivity(new Intent(this, LobbyActivity.class));
    }

    public void highScores(View view) {
        startActivity(new Intent(this, HighScoresActivity.class));
    }

    private void loadName(){

        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String name = settings.getString(PLAYER_NAME_KEY, "").toString();

        if (!name.equals("")){
            setName(name);
        }

    }

    public void setName(String name){
        PLAYER_NAME = name;
        NAME_SET = true;

        findViewById(R.id.practiceButton).setClickable(true);
        findViewById(R.id.multiButton).setClickable(true);
        findViewById(R.id.nameWarningText).setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findViewById(R.id.practiceButton).setClickable(false);
        findViewById(R.id.multiButton).setClickable(false);

        loadName();
        // Try and load the name
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == 0){
            return;
        }

        // Collect data from the intent and use it
        String nameSet = data.getStringExtra(NAME_SETTING_KEY);

        if (nameSet.equals("")){
            return;
        }

        setName(nameSet);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,0);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
