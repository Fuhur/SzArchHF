package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.zip.CheckedOutputStream;

import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/22/2014.
 */
public class SpeedBonus {
    private MyVector position;

    private static final float width = 0.1f;
    private static final float height = 0.05f;

    public SpeedBonus(MyVector pos){
        position = pos;
    }

    public MyVector getPosition(){
        return position;
    }

    public float getTop(){
        return position.Y + height / Utility.YtoXratio();
    }

    public float getBottom(){
        return position.Y - height / Utility.YtoXratio();
    }

    public float getLeft(){
        return position.X - width;
    }

    public float getRight(){
        return position.X + width;
    }

    public void draw(Canvas canvas, Paint paint, float minY) {
        paint.setColor(Color.GREEN);

        float top = Utility.RatioYToPX(1 - (getTop() - minY));
        float bottom = Utility.RatioYToPX(1 - (getBottom() - minY));
        float left = Utility.RatioXToPX(getLeft());
        float right = Utility.RatioXToPX(getRight());

        canvas.drawRect(left,top,right,bottom,paint);
    }
}
