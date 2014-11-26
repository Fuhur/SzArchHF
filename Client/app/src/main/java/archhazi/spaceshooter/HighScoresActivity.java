package archhazi.spaceshooter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import archhazi.spaceshooter.Communication.ServerProxy;


public class HighScoresActivity extends Activity {

    private ListView listView;
    private List<HashMap<String, String>> highScores = new ArrayList<HashMap<String, String>>();
    private ProgressDialog progressDialog;

    private ServerProxy serverProxy = new ServerProxy();
    private static final String TAG = "HighScoresActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        listView = (ListView) findViewById(R.id.listView);

        progressDialog = new ProgressDialog(HighScoresActivity.this);
        new DownloadHighscoresTask().execute();
    }

    private class DownloadHighscoresTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(HighScoresActivity.this, "", "Please wait...", true, false);
        }

        protected Void doInBackground(Void... params) {

            try {
                HttpResponse response = serverProxy.getFromServer("HighScores");
                if (response != null)
                {
                    String entity = EntityUtils.toString(response.getEntity());
                    JSONArray json = new JSONArray(entity);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject highScore = json.getJSONObject(i);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("Player", highScore.getString("Name"));
                        map.put("High score", highScore.getString("Score"));
                        highScores.add(map);
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(HighScoresActivity.this, highScores, R.layout.highscore_row,
                    new String[] { "Player", "High score" }, new int[] { R.id.player_text, R.id.score_text });
            listView.setAdapter(adapter);
        }
    }
}
