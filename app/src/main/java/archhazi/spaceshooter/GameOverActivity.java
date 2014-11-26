package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import archhazi.spaceshooter.Communication.ServerProxy;


public class GameOverActivity extends Activity {

    private ServerProxy serverProxy = new ServerProxy();

    private TextView scoreText;
    private Button sendButton;

    private TextView gameOverText;
    private TextView resultText;

    private static final String TAG = "GameOverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getActionBar().hide();

        Intent intent = getIntent();

        float score = intent.getIntExtra(GameActivity.SCORE_KEY, 0);
        scoreText = (TextView) findViewById(R.id.game_score);
        scoreText.setText(Float.toString(score));

        boolean multiplayer = intent.getBooleanExtra(MainMenuActivity.MULTIPLAYER_KEY, false);

        sendButton = (Button) findViewById(R.id.sendScore_button);
        gameOverText = (TextView) findViewById(R.id.game_over);
        resultText = (TextView) findViewById(R.id.result_text);

        if (multiplayer) {
            sendButton.setVisibility(View.INVISIBLE);
            finishMultiplayer();
        } else {
            gameOverText.setText("Game over");
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                sendButton.setEnabled(false);
                resultText.setText("No network connection available.");
            }
        }
    }

    private void finishMultiplayer() {
        final Float score = Float.parseFloat(scoreText.getText().toString());
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        new Thread() {
            public void run() {
                try {
                    JSONObject request = new JSONObject();
                    request.put("deviceId", deviceId);
                    request.put("score", score);

                    HttpResponse response = serverProxy.sendMessageToServer(request.toString(), "Finish");

                    Thread.sleep(100, 0);

                    response = serverProxy.sendMessageToServer(deviceId, "Result");
                    String entity = EntityUtils.toString(response.getEntity()).trim();
                    final String result = entity.replace("\"", "");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            gameOverText.setText(result);
                            if (result == "Victory") {
                                gameOverText.setTextColor(getResources().getColor(R.color.green));
                            } else {
                                gameOverText.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                } catch (InterruptedException e) {
                    Log.d(TAG, e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void sendScore(View view) {
        SharedPreferences settings = getSharedPreferences(MainMenuActivity.USER_INFO, 0);
        final String name = settings.getString(MainMenuActivity.PLAYER_NAME_KEY, "Player").toString();
        serverProxy.saveName(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), name);

        final Float score = Float.parseFloat(scoreText.getText().toString());

        new Thread() {
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
        }.start();
    }
}
