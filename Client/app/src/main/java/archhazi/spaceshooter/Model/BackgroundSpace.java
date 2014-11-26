package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/21/2014.
 */
public class BackgroundSpace {
    private List<MyVector> starsTopLayer = new ArrayList<MyVector>(50);
    private List<MyVector> starsBottomLayer = new ArrayList<MyVector>(20);
    private Random random = new Random();
    private float starProbability = 0.8f;
    private int startingStarsTopLayer = 40;
    private int startingStarsBottomLayer = 20;

    private float topLayerVelocity;
    private float bottomLayerVelocity;
    private static final float topToBottomVelocityCoeff = 0.8f;

    public BackgroundSpace(float topLayerVelocity){
        initStars();

        setTopLayerVelocity(topLayerVelocity);
    }

    public void setTopLayerVelocity(float topLayerVelocity){
        this.topLayerVelocity = topLayerVelocity * 0.8f;
        bottomLayerVelocity = this.topLayerVelocity * topToBottomVelocityCoeff;
    }

    private void initStars(){
        for(int i = 0; i < startingStarsTopLayer; i++){
            float x = random.nextFloat();
            float y = random.nextFloat();

            starsTopLayer.add(new MyVector(x, y));
        }
        for(int i = 0; i < startingStarsBottomLayer; i++){
            float x = random.nextFloat();
            float y = random.nextFloat();

            starsBottomLayer.add(new MyVector(x, y));
        }
    }

    private void addStarProb(){
        if(random.nextFloat() < starProbability){

            float x = random.nextFloat();

            starsTopLayer.add(new MyVector(x, 0));
        }

        if(random.nextFloat() < starProbability/2f){

            float x = random.nextFloat();

            starsBottomLayer.add(new MyVector(x, 0));
        }
    }

    public void moveStars(float elapsedS){
        float deltaY = elapsedS * topLayerVelocity;

        for (int i = 0; i < starsTopLayer.size(); i++){
            if (starsTopLayer.get(i).Y + deltaY > 1){
                starsTopLayer.remove(i);
                continue;
            }

            starsTopLayer.get(i).Y += deltaY;
        }

        deltaY = elapsedS * bottomLayerVelocity;
        for (int i = 0; i < starsBottomLayer.size(); i++){
            if (starsBottomLayer.get(i).Y + deltaY > 1){
                starsBottomLayer.remove(i);
                continue;
            }

            starsBottomLayer.get(i).Y += deltaY;
        }

        addStarProb();
    }

    public void drawStars(Canvas canvas, Paint paint){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        for (MyVector star: starsTopLayer){
            canvas.drawPoint(   Utility.RatioXToPX(star.X),
                                Utility.RatioYToPX(star.Y),
                                paint);
        }

        for (MyVector star: starsBottomLayer){
            canvas.drawPoint(   Utility.RatioXToPX(star.X),
                                Utility.RatioYToPX(star.Y),
                                paint);
        }
    }

}