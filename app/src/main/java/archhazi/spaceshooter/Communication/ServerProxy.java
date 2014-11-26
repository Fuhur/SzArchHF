package archhazi.spaceshooter.Communication;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Gergely on nov. 25..
 */
public class ServerProxy {

    private static final String SERVER_BASE = "http://nmobil.aut.bme.hu/SpaceService/SpaceService.svc/";
    private static final String CONTENT_TYPE = "application/json";
    private static final String TAG = "ServerProxy";

    private String androidId;
    private long delay;

    public void saveName(final String deviceId, final String name) {
        new Thread() {
            public void run() {
                JSONObject request = new JSONObject();
                try {
                    request.put("deviceId", deviceId);
                    request.put("name", name);
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
                sendMessageToServer(request.toString(), "SetName");
            }
        }.start();
    }

    public void calculateDelay() {
        new Thread() {
            public void run() {
                HttpResponse response = sendMessageToServer("" + System.currentTimeMillis(), "Delay");
                if (response != null) {
                    try {
                        String entity = EntityUtils.toString(response.getEntity());
                        delay = Long.parseLong(entity);
                        Log.d(TAG, "Delay: " + delay + " ms");
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }.start();
    }

    public HttpResponse sendMessageToServer(String message, String method) {
        String response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            String serverAddress = SERVER_BASE + method;
            HttpPost httpPost = new HttpPost(serverAddress);

            StringEntity entity = new StringEntity(message);
            entity.setContentEncoding(HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", CONTENT_TYPE);
            httpPost.setHeader("Accept", CONTENT_TYPE);

            Log.d(TAG, "Message: " + message);

            return httpClient.execute(httpPost);
        } catch (IOException e) {
            Log.d(TAG, "Init server connection failed: " + e.getMessage());
        }
        return null;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public long getDelay() {
        return delay;
    }
}
