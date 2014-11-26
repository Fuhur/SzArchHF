package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/21/2014.
 */
public class Asteroid{
    private MyVector position;
    private float radius;

    List<Hole> holes = new ArrayList<Hole>(3);

    public Asteroid(MyVector position, float radius, Random random){
        this.position = position;
        this.radius = radius;

        int maxHoles = 5;
        int holeCount = random.nextInt(maxHoles);
        float stdRad = radius / 2;
        float stdPos = radius;

        for (int i = 0; i < holeCount; i++){
            float x = random.nextFloat() * 2 * stdPos-  stdPos;
            float y = random.nextFloat() * 2*  stdPos - stdPos;

            float holeRad = random.nextFloat() * stdRad;

            holes.add(new Hole(new MyVector(x, y),holeRad));
        }
    }

    public void draw(Canvas canvas, Paint paint, float minY){

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);

        float radX = Utility.RatioXToPX(radius);
        float radY = Utility.RatioYToPX(radius);
        float centerX = Utility.RatioXToPX(position.X);
        float centerY = Utility.RatioYToPX(position.Y-minY);

        if (true){
            canvas.drawCircle(   Utility.RatioXToPX(position.X),
                    Utility.RatioYToPX(1 - (position.Y - minY)),
                    Utility.RatioXToPX(radius),paint);
        } else {
            Path path = new Path();
            path.moveTo(Utility.RatioXToPX(position.X + radius) ,Utility.RatioYToPX(1-(position.Y - minY)));
            for (float alpha = 0; alpha <= 2 * Math.PI; alpha += Math.PI / 16f){
                float px = (float)(radius * Math.cos(alpha) + position.X);
                float py = 1 - (float)(radius * Math.sin(alpha) + position.Y - minY);
                path.lineTo(Utility.RatioXToPX(px) ,Utility.RatioYToPX(py));
            }

            path.close();
            canvas.drawPath(path,paint);
        }




        paint.setColor(Color.BLACK);
        for (Hole hole:holes){
            canvas.drawCircle(  Utility.RatioXToPX((hole.position.X + position.X)) ,
                    Utility.RatioYToPX(1 - (hole.position.Y + position.Y - minY)),
                    Utility.RatioXToPX(hole.radius),paint);
        }
    }

    public MyVector getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    private class Hole{
        MyVector position;
        float radius;

        public Hole(MyVector position,float radius){
            this.position = position;
            this.radius = radius;
        }
    }
}
