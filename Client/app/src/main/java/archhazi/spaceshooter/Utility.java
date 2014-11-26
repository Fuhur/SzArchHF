package archhazi.spaceshooter;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by MBence on 11/21/2014.
 */
public class Utility {

    public static final float playerPosOnScreenY = 0.9f;

    public static float RatioXToPX(float ratio){
        return Resources.getSystem().getDisplayMetrics().widthPixels * ratio;
    }

    public static float RatioYToPX(float ratio){
        return Resources.getSystem().getDisplayMetrics().heightPixels * ratio;
    }

    public static float DIPtoPX(float DIP){

        return Resources.getSystem().getDisplayMetrics().density * DIP;
    }

    public static float YtoXratio(){
        return  ((float)Resources.getSystem().getDisplayMetrics().heightPixels) / Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
