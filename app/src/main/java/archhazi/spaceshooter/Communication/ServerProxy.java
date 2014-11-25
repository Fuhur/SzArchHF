package archhazi.spaceshooter.Communication;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.IOException;

/**
 * Created by Gergely on nov. 25..
 */
public class ServerProxy {

    private static final String SERVER_BASE = "http://nmobil.aut.bme.hu/SpaceService/SpaceService.svc/";
    private static final String CONTENT_TYPE = "application/json";
    private static final String TAG = "ServerProxy";

    private String androidId;

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
}
