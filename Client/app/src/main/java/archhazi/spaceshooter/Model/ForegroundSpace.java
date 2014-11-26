package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import archhazi.spaceshooter.Model.Asteroid;
import archhazi.spaceshooter.Model.SpeedBonus;
import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/21/2014.
 */
public class ForegroundSpace{
    private List<Asteroid> asteroids;
    private List<SpeedBonus> speedBonuses;
    private float goalY;

    private static final float AsteroidMeanRadius = 0.0625f;
    private static final float AsteroidStdRadius = 0.021f;

    public ForegroundSpace(int seed, float trackLength){
        asteroids = new ArrayList<Asteroid>(50);
        speedBonuses = new ArrayList<SpeedBonus>(10);

        generateTrack(seed,trackLength);

        goalY = trackLength;
    }

    private void generateTrack(int seed, float trackLength){
        Random random = new Random(seed);

        float asteroidDensity = 2f;
        float speedBonusDensity = 0.2f;

        int numberOfAsteroids = Math.round(asteroidDensity * trackLength);
        int numberOfSpeedBonuses = Math.round(speedBonusDensity * trackLength);

        for(int i = 0; i < numberOfAsteroids; i++){

            float radius = random.nextFloat()* 2 * AsteroidStdRadius - AsteroidStdRadius + AsteroidMeanRadius;
            float x = random.nextFloat();
            float y = random.nextFloat() * (trackLength-1) + 0.5f;

            Asteroid newAsteroid = new Asteroid(new MyVector(x,y),radius,random);

            asteroids.add(newAsteroid);
        }

        for (int i = 0; i < numberOfSpeedBonuses; i++){
            float x = random.nextFloat();
            float y = random.nextFloat() * (trackLength-1) + 0.5f;

            speedBonuses.add(new SpeedBonus(new MyVector(x,y)));
        }

    }

    public List<Asteroid> getAsteroids(){
        return asteroids;
    }

    public List<SpeedBonus> getSpeedBonuses(){
        return speedBonuses;
    }

    public float getGoalY(){
        return goalY;
    }

    public void drawAsteroids(Canvas canvas, Paint paint, float minY){
        for(Asteroid asteroid:asteroids){
            if (asteroid.getPosition().Y < minY + 1 && asteroid.getPosition().Y > minY){
                asteroid.draw(canvas, paint,minY);
            }

        }
    }

    public void drawGoal(Canvas canvas, Paint paint,float minY){

        float squareSizeX = 0.025f;
        float squareSizeY = squareSizeX / Utility.YtoXratio();
        int rows = 4;

        boolean red = true;
        boolean startRed = true;

        float actY = 1 - (goalY-minY);
        for (int row = 0; row < rows; row++){

            red = startRed;
            for (float startX = 0; startX < 1; startX += squareSizeX){
                if (red){
                    paint.setColor(Color.RED);
                }
                else {
                    paint.setColor(Color.WHITE);
                }

                canvas.drawRect(    Utility.RatioXToPX(startX),
                                    Utility.RatioYToPX(actY),
                                    Utility.RatioXToPX(startX + squareSizeX),
                                    Utility.RatioYToPX(actY + squareSizeY),paint);

                red ^= true;
            }

            startRed ^= true;

            actY += squareSizeY;
        }

    }

    public void DrawEverything(Canvas canvas, Paint paint, float minY){
        drawGoal(canvas, paint, minY);
        drawSpeedBonuses(canvas,paint,minY);
        drawAsteroids(canvas, paint, minY);
    }

    private void drawSpeedBonuses(Canvas canvas, Paint paint, float minY) {

        for(SpeedBonus bonus:speedBonuses){
            if (bonus.getPosition().Y < minY + 1 && bonus.getPosition().Y > minY - 1) {
                bonus.draw(canvas, paint, minY);
            }

        }
    }


}

