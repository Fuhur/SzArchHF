package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/21/2014.
 */
public class SpaceShip{
    private MyVector position;

    private float lengthRatio = 0.12f;
    private float widthRatio = 0.1f;

    private float startVelocityY = 0.6f;
    private float afterCollisionVelocityY = 0f;

    private float shipVelocityX = 1/8f;
    private float shipVelocityY = startVelocityY;
    private float maxVelocityX = 0.5f;
    private float shipAccelerationY = 0.6f;
    private float shipAccelerationX = 0.003f;

    private boolean invincible = false;
    private float invincibleSince = 0;
    private final float invincibilityTimeSpan = 2; // ms

    public static final int PlayerColor = Color.RED;
    public static final int OpponentColor = Color.BLUE;

    private float sideLength = -1;

    private boolean present;

    public float sideLength(){
        if (sideLength < 0){
            sideLength = (float)Math.sqrt(lengthRatio*lengthRatio + widthRatio*widthRatio/4);
        }

        return sideLength;
    }

    public void move(float deltaX, float elapsedS){

        deltaX *= -1;

        deltaX *= shipVelocityX * elapsedS;

        if (Math.abs(deltaX) > maxVelocityX * elapsedS){
           // deltaX = Math.signum(deltaX) * maxVelocityX * elapsedS;
        }

        getPosition().X += deltaX;

        getPosition().X = Math.min(1 - widthRatio / 2 , getPosition().X);
        getPosition().X = Math.max(0 + widthRatio / 2, getPosition().X);

        getPosition().Y += elapsedS * shipVelocityY;

        if (invincible){
            invincibleSince += elapsedS;

            shipVelocityY += (startVelocityY-afterCollisionVelocityY) / invincibilityTimeSpan * elapsedS;
        }

        if (invincible && invincibleSince > invincibilityTimeSpan){
            invincible = false;
            invincibleSince = 0;
        }
    }

    public void accelerate(float elapsedS){

        if (invincible){
            return;
        }

        shipVelocityY += shipAccelerationY * elapsedS;
        shipVelocityX += shipAccelerationX * elapsedS;
    }

    public void drawShip(Canvas canvas, Paint paint, float minY){
        drawShip(canvas,paint, minY,false);
    }

    public void drawShipAsOpponent(Canvas canvas, Paint paint, float minY){
        if (position.Y < minY + 1 && position.Y >= minY){
            drawShip(canvas, paint, minY, true);
        }
    }

    public void drawShip(Canvas canvas, Paint paint, float minY, boolean asOpponent){

        paint.setStyle(Paint.Style.FILL);

        if (asOpponent){
            paint.setColor(OpponentColor);
        } else {
            if (invincible){
                paint.setColor(Color.DKGRAY);
            } else {
                paint.setColor(PlayerColor);
            }

        }

        float y = 1 - (position.Y - minY);

        Path path = new Path();
        path.moveTo(Utility.RatioXToPX((getPosition().X + widthRatio / 2)),
                Utility.RatioYToPX((y)));

        path.lineTo(Utility.RatioXToPX((getPosition().X)),
                Utility.RatioYToPX((y - lengthRatio)));

        path.lineTo(Utility.RatioXToPX((getPosition().X - widthRatio / 2)) ,
                Utility.RatioYToPX((y)));

        path.lineTo(Utility.RatioXToPX((getPosition().X + widthRatio / 2)),
                Utility.RatioYToPX((y)));
        path.close();

        canvas.drawPath(path,paint);
    }

    public MyVector getFront(){
        return new MyVector(getPosition().X, getPosition().Y + lengthRatio);
    }

    public MyVector getLeft(){
        return new MyVector(getPosition().X- widthRatio / 2, getPosition().Y);
    }

    public MyVector getRight(){
        return new MyVector(getPosition().X + widthRatio / 2, getPosition().Y);
    }

    public MyVector getCenter(){
        return new MyVector(getPosition().X, getPosition().Y + lengthRatio / 2);
    }

    public MyVector getPosition() {
        return position;
    }

    public float getVelocity(){
        return shipVelocityY;
    }

    public SpaceShip(boolean present) {
        this(new MyVector(0.5f,0f));
        this.present = present;
    }

    public SpaceShip(MyVector position){
        this.position = position;
    }

    public void setPosition(MyVector position) {
        this.position = position;
    }

    public void collided() {

        if (invincible){
            return;
        }

        shipVelocityY = afterCollisionVelocityY;

        invincible = true;
        invincibleSince = 0;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isInvincible() {
        return invincible;
    }


}