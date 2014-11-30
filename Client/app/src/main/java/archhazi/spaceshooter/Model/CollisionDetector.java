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

        if (ship.isInvincible()){
            return false;
        }

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

        if (ship.isInvincible()){
            return false;
        }

        MyVector front = ship.getFront();
        MyVector left = ship.getLeft();
        MyVector right = ship.getRight();

        MyVector center = ship.getCenter();

        for(Asteroid asteroid:asteroids){

            if (Math.abs(asteroid.getPosition().Y - ship.getPosition().Y) > 0.5){
                continue;
            }

            float radiusSquaredX = asteroid.getRadius() * asteroid.getRadius();
            float radiusSquaredY = asteroid.getRadius() * asteroid.getRadius() / Utility.YtoXratio() / Utility.YtoXratio();


            if (    InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,front)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,left)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,right)
                    || InEllipse(asteroid.getPosition(),radiusSquaredX,radiusSquaredY,center) ){
                return true;
            }
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

