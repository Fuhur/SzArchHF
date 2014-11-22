package archhazi.spaceshooter;

/**
 * Created by MBence on 11/18/2014.
 */
public class MyVector{
    public float X;
    public float Y;

    public MyVector(float x, float y){
        X = x;
        Y = y;
    }

    public static float distance(MyVector v1, MyVector v2){
        return (float)Math.sqrt(distanceSquared(v1,v2));
    }

    public static float distanceSquared(MyVector v1, MyVector v2){
        return (v1.X-v2.X)*(v1.X-v2.X) + (v1.Y-v2.Y)*(v1.Y-v2.Y);
    }

    public MyVector add(MyVector vector){
        return new MyVector(X + vector.X,Y + vector.Y);
    }
}