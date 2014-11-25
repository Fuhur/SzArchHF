package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import archhazi.spaceshooter.Communication.ServerProxy;


public class GameOverActivity extends Activity {

    private ServerProxy serverProxy = new ServerProxy();

    private TextView scoreText;
    private TextView nameText;
    private EditText editName;
    private Button sendButton;

    private TextView resultText;

    private static final String TAG = "GameOverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getActionBar().hide();

        Intent intent = getIntent();

        float score = intent.getIntExtra(GameActivity.SCORE_KEY,0);
        scoreText = (TextView) findViewById(R.id.game_score);
        scoreText.setText(Float.toString(score));

        boolean multiplayer = intent.getBooleanExtra(MainMenuActivity.MULTIPLAYER_KEY,false);

        nameText = (TextView) findViewById(R.id.yourName_text);
        editName = (EditText) findViewById(R.id.name_input);
        sendButton = (Button) findViewById(R.id.sendScore_button);
        resultText = (TextView) findViewById(R.id.result_text);

        if (!multiplayer){
            nameText.setVisibility(View.INVISIBLE);
            editName.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
            resultText.setVisibility(View.INVISIBLE);
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                sendButton.setEnabled(false);
                resultText.setText("No network connection available.");
            }
        }
    }

    public void sendScore(View view){
        final String name = editName.getText().toString();
        final Float score = Float.parseFloat(scoreText.getText().toString());

        Thread uploadThread = new Thread() {
            public void run() {
                JSONObject request = new JSONObject();
                try {
                    request.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    request.put("score", score);
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
                final HttpResponse response = serverProxy.sendMessageToServer(request.toString(), "UploadHighScore");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.getStatusLine().getStatusCode() == 200) {
                            resultText.setTextColor(getResources().getColor(R.color.green));
                            resultText.setText("Upload successful.");
                        } else {
                            resultText.setText("Upload failed.");
                        }
                    }
                });
            }
        };
        uploadThread.start();
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
