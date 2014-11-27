package archhazi.spaceshooter.Model;

import android.graphics.Color;
import android.graphics.Paint;

import archhazi.spaceshooter.MyVector;

/**
 * Created by MBence on 11/27/2014.
 */
public class OpponentSpaceShip extends SpaceShip {

    private long previousTimeStamp = 0;

    private MyVector lastKnowPosition;

    private float predictedVelocityX = 0;
    private float predictedVelocityY = startVelocityY;

    public OpponentSpaceShip(long timeStamp){
        this.previousTimeStamp = timeStamp;

        lastKnowPosition = position;
    }

    protected void setPaint(Paint paint){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
    }

    public void predictPosition(long actTime) {

        float deltaX = (actTime - previousTimeStamp) * predictedVelocityX;
        float deltaY = (actTime - previousTimeStamp) * predictedVelocityY;

        position.X += deltaX;
        position.Y += deltaY;

        if (Float.isNaN(position.Y) || Float.isNaN(position.X) || Double.isNaN(previousTimeStamp)){
            float a = 0;
            position.X += a;
        }


    }

    public void setPosition(MyVector pos, long timeStamp){

        predictedVelocityX = (pos.X - lastKnowPosition.X) / (timeStamp - previousTimeStamp);
        predictedVelocityY = (pos.Y - lastKnowPosition.Y) / (timeStamp - previousTimeStamp);

        this.position = pos;
        lastKnowPosition = pos;
        previousTimeStamp = timeStamp;

        if (Float.isNaN(position.Y) || Float.isNaN(position.X) || Double.isNaN(previousTimeStamp)){
            float a = 1;
            position.X *= a;
            return;
        }
    }


}
