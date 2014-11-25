package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import archhazi.spaceshooter.Communication.ServerProxy;


public class LobbyActivity extends Activity {

    private TextView waitingText;
    private ProgressBar progressBar;

    private ServerProxy serverProxy = new ServerProxy();

    private static final String TAG = "LobbyActivity";

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

            final ScheduledExecutorService scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    final HttpResponse response = serverProxy.sendMessageToServer(deviceId, "StartMultiplayer");
                    try {
                        String entity = EntityUtils.toString(response.getEntity());
                        JSONObject request = new JSONObject(entity);
                        boolean ready = (Boolean) request.get("Ready");
                        final int seed = (Integer) request.get("LevelSeed");
                        final long startTimeStamp = (Long) request.get("StartTimeStamp");
                        if (ready) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    waitingText.setText("Seed = " + seed + ", start in " + (startTimeStamp - System.currentTimeMillis()) + " ms");
                                }
                            });
                            scheduleTaskExecutor.shutdown();
                            try {
                                Thread.sleep((startTimeStamp - System.currentTimeMillis()), 0);
                            } catch (InterruptedException e) {
                            }

                            Intent intent = new Intent(LobbyActivity.this, GameActivity.class);

                            intent.putExtra(MainMenuActivity.SEED_KEY, seed);
                            intent.putExtra(MainMenuActivity.LENGTH_KEY, 5f);
                            intent.putExtra(MainMenuActivity.MULTIPLAYER_KEY, true);

                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        }
    }
}
