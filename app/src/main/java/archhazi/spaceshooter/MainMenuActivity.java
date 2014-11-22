package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainMenuActivity extends Activity {

    public final static String SEED_KEY = "SEED_KEY";
    public final static String LENGTH_KEY = "LENGTH_KEY";
    public final static String MULTIPLAYER_KEY = "MULTIPLAYER_KEY";

    public void startSinglePlayerGame(View view){
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(SEED_KEY,0);
        intent.putExtra(LENGTH_KEY,5f);
        intent.putExtra(MULTIPLAYER_KEY,false);

        startActivity(intent);
    }

    public void startMultiPlayerGame(View view){
        Intent intent = new Intent(this, GameActivity.class);

        // TO DO
        // HANDSHAKING
        intent.putExtra(SEED_KEY,0);
        intent.putExtra(LENGTH_KEY,30);
        intent.putExtra(MULTIPLAYER_KEY,true);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
