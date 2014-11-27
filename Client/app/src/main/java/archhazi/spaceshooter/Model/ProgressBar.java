package archhazi.spaceshooter.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Space;

import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/27/2014.
 */
public class ProgressBar {
    private float length;

    private final float positionY = 0.92f;
    private final float gapX = 0.1f;
    private final float halfEndHeight = 0.02f;
    private final float playerRad = 0.01f;

    private final int endColor = Color.GREEN;
    private final int trackColor = Color.WHITE;

    public ProgressBar(float trackLength){
        this.length = trackLength;
    }

    public void drawBar(Canvas canvas, Paint paint, SpaceShip player, SpaceShip opponent){
        drawTrack(canvas,paint);

        paint.setColor(SpaceShip.PlayerColor);
        float posX = (1 - 2 * gapX) * player.getPosition().Y / length + gapX;
        canvas.drawCircle(  Utility.RatioXToPX(posX),
                            Utility.RatioYToPX(positionY),
                            Utility.RatioXToPX(playerRad), paint);

        if (opponent.isPresent()){
            paint.setColor(SpaceShip.OpponentColor);
            float opponentPosX = (1 - 2 * gapX) * opponent.getPosition().Y / length + gapX;
            canvas.drawCircle(  Utility.RatioXToPX(opponentPosX),
                                Utility.RatioYToPX(positionY),
                                Utility.RatioXToPX(playerRad), paint);
        }



    }

    private void drawTrack(Canvas canvas, Paint paint){

        paint.setColor(trackColor);
        canvas.drawLine(    Utility.RatioXToPX(gapX),
                            Utility.RatioYToPX(positionY),
                            Utility.RatioXToPX(1-gapX),
                            Utility.RatioYToPX(positionY),paint);

        paint.setColor(endColor);
        canvas.drawLine(    Utility.RatioXToPX(gapX),
                            Utility.RatioYToPX(positionY + halfEndHeight),
                            Utility.RatioXToPX(gapX),
                            Utility.RatioYToPX(positionY - halfEndHeight),paint);

        canvas.drawLine(    Utility.RatioXToPX(1-gapX),
                            Utility.RatioYToPX(positionY + halfEndHeight),
                            Utility.RatioXToPX(1-gapX),
                            Utility.RatioYToPX(positionY - halfEndHeight),paint);
    }

}
