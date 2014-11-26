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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import archhazi.spaceshooter.Communication.ServerProxy;


public class LobbyActivity extends Activity {

    private static final String TAG = "LobbyActivity";
    private TextView waitingText;
    private ProgressBar progressBar;

    private ServerProxy serverProxy = new ServerProxy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        waitingText = (TextView) findViewById(R.id.waiting_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            waitingText.setText("No network connection available.");
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            SharedPreferences settings = getSharedPreferences(MainMenuActivity.USER_INFO, 0);
            serverProxy.saveName(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), settings.getString(MainMenuActivity.PLAYER_NAME_KEY, "Player"));

            serverProxy.calculateDelay();

            Thread thread = new Thread() {
                public void run() {
                    boolean ready = false;
                    JSONObject response = null;

                    // Only run for a minute
                    for (int i = 0; i < 120 && !ready; i++) {
                        try {
                            HttpResponse httpResponse = serverProxy.sendMessageToServer(deviceId, "StartMultiplayer");
                            String entity = EntityUtils.toString(httpResponse.getEntity());
                            response = new JSONObject(entity);
                            ready = (Boolean) response.get("Ready");

                            Thread.sleep(500, 0);
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage());
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                        } catch (InterruptedException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }

                    if (!ready || response == null) {
                        serverProxy.sendMessageToServer(deviceId, "QuitLobby");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                waitingText.setText("Couldn't find opponent.");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        return;
                    }

                    try {
                        final int seed = (Integer) response.get("LevelSeed");
                        final long startTimeStamp = (Long) response.get("StartTimeStamp") - serverProxy.getDelay();

                        Intent intent = new Intent(LobbyActivity.this, GameActivity.class);

                        intent.putExtra(MainMenuActivity.SEED_KEY, seed);
                        intent.putExtra(MainMenuActivity.LENGTH_KEY, GameActivity.MULTI_LENGTH);
                        intent.putExtra(MainMenuActivity.START_TIME_KEY, startTimeStamp);
                        intent.putExtra(MainMenuActivity.MULTIPLAYER_KEY, true);

                        startActivity(intent);
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            };
            thread.start();
        }
    }
}
