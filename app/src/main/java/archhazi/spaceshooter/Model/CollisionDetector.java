package archhazi.spaceshooter.Model;

import java.util.List;

import archhazi.spaceshooter.MyVector;
import archhazi.spaceshooter.Utility;

/**
 * Created by MBence on 11/21/2014.
 */
public class CollisionDetector {

    public static CollisionType CheckCollisions(SpaceShip ship, ForegroundSpace foregroundSpace){

        if (collisionAsteroid(ship,foregroundSpace.getAsteroids())){
            return CollisionType.ASTEROID;
        }

        if (ship.getPosition().Y >= foregroundSpace.getGoalY()){
            return CollisionType.GOAL;
        }

        if (collisionSpeedBonus(ship, foregroundSpace.getSpeedBonuses())){
            return CollisionType.SPEEDBONUS;
        }

        return CollisionType.NO_COLLISION;
    }

    public static boolean collisionSpeedBonus(SpaceShip ship, List<SpeedBonus> speedBonuses){

        MyVector front = ship.getFront();
        MyVector left = ship.getLeft();
        MyVector right = ship.getRight();

        MyVector center = ship.getCenter();

        for (SpeedBonus bonus:speedBonuses){
            if (Math.abs(bonus.getPosition().Y - ship.getPosition().Y) > 0.5){
                continue;
            }

            float top = bonus.getTop();
            float bottom = bonus.getBottom();
            float rightWall = bonus.getRight();
            float leftWall = bonus.getLeft();

            if (InRectangle(front,top,bottom,leftWall,rightWall)){
                return true;
            }
            if (InRectangle(left,top,bottom,leftWall,rightWall)){
                return true;
            }
            if (InRectangle(right,top,bottom,leftWall,rightWall)){
                return true;
            }
            if (InRectangle(center,top,bottom,leftWall,rightWall)){
                return true;
            }
        }

        return  false;
    }

    public static boolean collisionAsteroid(SpaceShip ship, List<Asteroid> asteroids){
        MyVector front = ship.getFront();
        MyVector left = ship.getLeft();
        MyVector right = ship.getRight();

        MyVector center = ship.getCenter();

        float sideLength = ship.sideLength();

        for(Asteroid asteroid:asteroids){

            if (Math.abs(asteroid.getPosition().Y - ship.getPosition().Y) > 0.5){
                continue;
            }

            float radiusSquared = asteroid.getRadius() * asteroid.getRadius();

            float radiusSquaredX = asteroid.getRadius() * asteroid.getRadius();
            float radiusSquaredY = asteroid.getRadius() * asteroid.getRadius() / Utility.YtoXratio() / Utility.YtoXratio();


            if (    InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,front)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,left)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,right)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,center) ){
                return true;
            }
            /*
            if (    (front.X - asteroid.position.X) * (front.X - asteroid.position.X) / radiusSquaredX + (front.Y - asteroid.position.Y) * (front.Y - asteroid.position.Y) / radiusSquaredY < 1
                    || (left.X - asteroid.position.X) * (left.X - asteroid.position.X) / radiusSquaredX + (left.Y - asteroid.position.Y) * (left.Y - asteroid.position.Y) / radiusSquaredY < 1
                    || (right.X - asteroid.position.X) * (right.X - asteroid.position.X) / radiusSquaredX + (right.Y - asteroid.position.Y) * (right.Y - asteroid.position.Y) / radiusSquaredY < 1
                    || (center.X - asteroid.position.X) * (center.X - asteroid.position.X) / radiusSquaredX + (center.Y - asteroid.position.Y) * (center.Y - asteroid.position.Y) / radiusSquaredY < 1 ){
                return true;
            } else {
                return false;
            }
            */
            /*
            if (    MyVector.distanceSquared(front,asteroid.position) < radiusSquared
                    || MyVector.distanceSquared(left,asteroid.position) < radiusSquared
                    || MyVector.distanceSquared(right,asteroid.position) < radiusSquared
                    || MyVector.distanceSquared(center,asteroid.position) < radiusSquared ){
                return true;
            } else {
                return false;
            }
            */
            /*
            MyVector B = front;
            MyVector A = left;

            float Dx = (B.X - A.X)/sideLength;
            float Dy = (B.Y - A.Y)/sideLength;

            float t = Dx * (asteroid.position.X - A.X) + Dy * (asteroid.position.Y - A.Y);

            float Ex = t*Dx + A.X;
            float Ey = t*Dy + A.Y;

            float tav = (float)Math.sqrt((Ex-asteroid.position.X)*(Ex-asteroid.position.X) + (Ey-asteroid.position.Y)*(Ey-asteroid.position.Y));

            if (tav < asteroid.radius && ((A.X > Ex && Ex > B.X) || (A.X < Ex && Ex < B.X)) && ((A.Y > Ey && Ey > B.Y) || (A.Y < Ey && Ey < B.Y))){
                return true;
            }

            B = front;
            A = right;

            Dx = (B.X - A.X)/sideLength;
            Dy = (B.Y - A.Y)/sideLength;

            t = Dx * (asteroid.position.X - A.X) + Dy * (asteroid.position.Y - A.Y);

            Ex = t*Dx + A.X;
            Ey = t*Dy + A.Y;

            tav = (float)Math.sqrt((Ex-asteroid.position.X)*(Ex-asteroid.position.X) + (Ey-asteroid.position.Y)*(Ey-asteroid.position.Y));

            if (tav < asteroid.radius && ((A.X > Ex && Ex > B.X) || (A.X < Ex && Ex < B.X)) && ((A.Y > Ey && Ey > B.Y) || (A.Y < Ey && Ey < B.Y))){
                return true;
            }
             */
        }

        return  false;
    }

    private static boolean InEllipse(MyVector ellipseCenter, float radXSq, float radYSq, MyVector point){
        return (point.X - ellipseCenter.X) * (point.X - ellipseCenter.X) / radXSq + (point.Y - ellipseCenter.Y) * (point.Y - ellipseCenter.Y) / radYSq < 1;
    }

    private static boolean InRectangle(MyVector point, float top, float bottom, float left, float right){
        return point.Y > bottom && point.Y < top && point.X < right && point.X  > left;
    }

}

