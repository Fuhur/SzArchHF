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

    public float getPredictedVelocityY(){
        return predictedVelocityY;
    }

    protected void setPaint(Paint paint){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
    }

    public void predictPosition(long actTime) {

        float elapsedS =  (actTime - previousTimeStamp) / 1000f;

        float deltaX = elapsedS * predictedVelocityX;
        float deltaY = elapsedS * predictedVelocityY;

        position.X += deltaX;
        position.Y += deltaY;

        if (Float.isNaN(position.Y) || Float.isNaN(position.X) || Double.isNaN(previousTimeStamp)){
            float a = 0;
            position.X += a;
        }

    }

    public void setPosition(MyVector pos, long timeStamp){

        float elapsedS =  (timeStamp - previousTimeStamp) / 1000f;

        predictedVelocityX = (pos.X - lastKnowPosition.X) / elapsedS;
        predictedVelocityY = (pos.Y - lastKnowPosition.Y) / elapsedS;

        this.position = pos;
        lastKnowPosition = pos;
        previousTimeStamp = timeStamp;

    }

    public void setPositionAndVelocity(MyVector pos, float velocityX, float velocityY, long timeStamp){

        predictedVelocityX = velocityX;
        predictedVelocityY = velocityY;

        this.position = pos;
        lastKnowPosition = pos;

        previousTimeStamp = timeStamp;
    }


}
