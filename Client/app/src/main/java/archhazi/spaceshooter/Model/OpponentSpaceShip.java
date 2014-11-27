package archhazi.spaceshooter.Model;

import android.graphics.Color;
import android.graphics.Paint;

import archhazi.spaceshooter.MyVector;

/**
 * Created by MBence on 11/27/2014.
 */
public class OpponentSpaceShip extends SpaceShip {

    private float previousTimeStamp = 0;

    private float predictedVelocityX = 0;
    private float predictedVelocityY = startVelocityY;

    public OpponentSpaceShip(long timeStamp){
        this.previousTimeStamp = timeStamp;
    }

    protected void setPaint(Paint paint){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
    }

    public void predictPosition(long actTime) {
        float elapsedS = (actTime - previousTimeStamp) / 1000f;

        float deltaX = elapsedS * predictedVelocityX;
        float deltaY = elapsedS * predictedVelocityY;

        position.X += deltaX;
        position.Y += deltaY;
    }

    public void setPosition(MyVector pos, long timeStamp){

        float elapsed = timeStamp - previousTimeStamp;
        float elapsedS = elapsed / 1000f;

        predictedVelocityX = (pos.X - position.X) / elapsedS;
        predictedVelocityY = (pos.Y - position.Y) / elapsedS;

        this.position = pos;
        previousTimeStamp = timeStamp;
    }


}
