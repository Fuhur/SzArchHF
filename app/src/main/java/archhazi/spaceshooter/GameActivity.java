package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import archhazi.spaceshooter.Communication.ServerProxy;
import archhazi.spaceshooter.Model.BackgroundSpace;
import archhazi.spaceshooter.Model.CollisionDetector;
import archhazi.spaceshooter.Model.CollisionType;
import archhazi.spaceshooter.Model.ForegroundSpace;
import archhazi.spaceshooter.Model.SpaceShip;

public class GameActivity extends Activity implements SensorEventListener {

    float mLastX,mLastY,mLastZ;
    boolean mInitialized= false;
    float NOISE = 0.1f;
    SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public final static String SCORE_KEY = "SCORE_KEY";
    public final static float PRACTICE_LENGTH = 10.f;
    public final static float MULTI_LENGTH = 5.f;
    public final static String TAG = "GameActivity";

    private String deviceId;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX > NOISE) mLastY++;
            if (deltaY < NOISE) deltaY = (float)0.0;
            if (deltaZ < NOISE) deltaZ = (float)0.0;

            mLastX = x;
            mLastY = y;
            mLastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    public class GameView extends View {

        private final View view = this;
        private final Handler viewHandler;
        private Runnable updateView;

        public GameView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub

            // Elvileg ezzel jo a periodikus invalidate de ettol szakad...
            viewHandler = new Handler();
            updateView = new Runnable(){
                @Override
                public void run(){
                    view.invalidate();
                    viewHandler.postDelayed(this, 40);
                }
            };

    //        viewHandler.postDelayed(updateView, 100);
        }

        private long lastTime = -1;
        private SpaceShip spaceShip = null;
        private BackgroundSpace backgroundSpace = null;
        private ForegroundSpace foregroundSpace = null;

        private boolean opponentPresent = false;
        private SpaceShip opponent = null;

        private Paint paint = null;

        private boolean gameEnded = false;

        private int seed;
        private float trackLength;

        private long startTime = -1;

        private ServerProxy serverProxy = new ServerProxy();

        @Override
        protected void onDraw(Canvas canvas) {
            if (gameEnded){
                return;
            }

            long elapsed = -1;

            // Ha ez az elso onDraw, init
            if (lastTime < 0){
                spaceShip = new SpaceShip();
                foregroundSpace = new ForegroundSpace(seed,trackLength);

                backgroundSpace = new BackgroundSpace(spaceShip.getVelocity());
            }

            long actTime = System.currentTimeMillis();

            if (startTime < 0){
                startTime = actTime;
            }

            if (this.lastTime > 0) {
                elapsed = actTime - this.lastTime;
            }

            // Eltelt ido masodpercben
            float elapsedS = elapsed / 1000.0f;

            if ( elapsedS > 0){
                backgroundSpace.moveStars(elapsedS);

                if (mInitialized){
                    spaceShip.move(mLastX, elapsedS);
                }

                CollisionType collision = CollisionDetector.CheckCollisions(spaceShip, foregroundSpace);

                // HUGE TODO
                switch (collision){
                    case GOAL:
                        viewHandler.removeCallbacks(updateView);
                        gameEnded = true;

                        Intent intent = new Intent(getContext(),GameOverActivity.class);

                        long gameLengthMS = actTime - startTime;

                        intent.putExtra(SCORE_KEY, (int)gameLengthMS);
                        intent.putExtra(MainMenuActivity.MULTIPLAYER_KEY, opponentPresent);

                        startActivity(intent);

                        finish();

                        break;
                    case SPEEDBONUS:
                        spaceShip.accelerate(elapsedS);
                        backgroundSpace.setTopLayerVelocity(spaceShip.getVelocity());
                        break;
                    case ASTEROID:
                        spaceShip.collided();
                     //   viewHandler.removeCallbacks(updateView);
                     //   gameEnded = true;
                     //   finish();
                        break;
                    case NO_COLLISION:

                        break;
                    default:
                        break;
                }

            }

            // TODO Auto-generated method stub
            super.onDraw(canvas);

            if (paint == null)
            {
                paint = new Paint();
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawText(Double.toString(elapsed),100,100,paint);
            canvas.drawText(Double.toString(spaceShip.getPosition().Y),100,120,paint);
            canvas.drawText(Double.toString(spaceShip.getVelocity()),100,140,paint);
            canvas.drawText(Double.toString(mLastX),100,160,paint);
            backgroundSpace.drawStars(canvas, paint);
            foregroundSpace.DrawEverything(canvas,paint,spaceShip.getPosition().Y - (1 - Utility.playerPosOnScreenY));
            if (opponentPresent){
                opponent.drawShipAsOpponent(canvas, paint, spaceShip.getPosition().Y -  (1 - Utility.playerPosOnScreenY));
            }
            spaceShip.drawShip(canvas,paint,spaceShip.getPosition().Y - (1 - Utility.playerPosOnScreenY));

            lastTime = actTime;

            view.invalidate();

            if (opponentPresent){
                new Thread() {
                    public void run() {
                        JSONObject request = new JSONObject();
                        try {
                            request.put("deviceId", deviceId);
                            request.put("X", spaceShip.getPosition().X);
                            request.put("Y", spaceShip.getPosition().Y);

                            final HttpResponse response = serverProxy.sendMessageToServer(request.toString(), "Tick");
                            if (response != null)
                            {
                                String entity = EntityUtils.toString(response.getEntity());
                                JSONObject json = new JSONObject(entity);

                                JSONObject opponentPosition = json.getJSONObject("OpponentPosition");
                                float opponentX = (float) opponentPosition.getDouble("X");
                                float opponentY = (float) opponentPosition.getDouble("Y");
                                opponent.setPosition(new MyVector(opponentX, opponentY));
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                }.start();
            }
        }

        public void setSeed(int seed){
            this.seed = seed;
        }

        public void setTrackLength(float trackLength){
            this.trackLength = trackLength;
        }

        public void setMultiplayer(boolean multi){
            opponentPresent = multi;

            if (multi){
                opponent = new SpaceShip();
            }
        }

        public void setOpponetPosition(MyVector position){
            if (opponentPresent){
                opponent.setPosition(position);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameView gameView = new GameView(this);

        setContentView(gameView);

        getActionBar().hide();

        Intent intent = getIntent();
        int seed = intent.getIntExtra(MainMenuActivity.SEED_KEY, -1);
        float length = intent.getFloatExtra(MainMenuActivity.LENGTH_KEY,1);
        boolean multiplayer = intent.getBooleanExtra(MainMenuActivity.MULTIPLAYER_KEY,false);

        gameView.setSeed(seed);
        gameView.setTrackLength(length);
        gameView.setMultiplayer(multiplayer);

        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
