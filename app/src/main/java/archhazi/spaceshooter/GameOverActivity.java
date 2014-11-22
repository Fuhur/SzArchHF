package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getActionBar().hide();

        Intent intent = getIntent();

        float score = intent.getIntExtra(GameActivity.SCORE_KEY,0);
        TextView scoreText = (TextView) findViewById(R.id.game_score);

        scoreText.setText(Float.toString(score));

        boolean multiplayer = intent.getBooleanExtra(MainMenuActivity.MULTIPLAYER_KEY,false);

        TextView nameText = (TextView) findViewById(R.id.yourName_text);
        EditText editName = (EditText) findViewById(R.id.name_input);
        Button sendButton = (Button) findViewById(R.id.sendName_button);

        if (!multiplayer){
            nameText.setVisibility(View.INVISIBLE);
            editName.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }

    }

    public void startSinglePlayerGame(View view){
        EditText editName = (EditText) findViewById(R.id.name_input);

        String name = editName.getText().toString();

        // TODO - SEND TO SERVER
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_of_track, menu);
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
