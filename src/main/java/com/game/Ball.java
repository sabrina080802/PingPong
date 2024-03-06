package game;

import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pong.Pong;

public class Ball implements IEntity {
    private static final Paint COLOR = Color.GREENYELLOW;
    private static final double BALL_SIZE = 5;
    private static final int BALL_SPEED = 4;
    private static final double MIN_REFLECTION_ANGLE = Math.toRadians(30);
    private static final double MAX_REFLECTION_ANGLE = Math.toRadians(150);

    private Circle circle;
    private double dX = 4;
    private double dY = 4;

    public Ball(){
        circle = new Circle(
        Pong.getInstance().width / 2 - BALL_SIZE / 2,
        Pong.getInstance().height / 2 - BALL_SIZE / 2,
            BALL_SIZE, COLOR);
    }
    public double getX(){
        return circle.getCenterX();
    }
    public double getY(){
        return circle.getCenterY();
    }
    public double getDirectionX(){
        return dX;
    }
    @Override
    public void update(){
        Bounds bounds = circle.getBoundsInParent();
        if(bounds.getMinX() + dX <= 0 || bounds.getMaxX() + dX >= Pong.getInstance().width)
            dX = -dX;
        else if(bounds.getMinY() + dY <= 0 || bounds.getMaxY() + dY >= Pong.getInstance().height)
            dY = -dY;
        else {
            boolean hits = false;
            Player hittingEntity = null;;
            double minX = bounds.getMinX() + dX,
                    maxX = bounds.getWidth();
            double minY = bounds.getMinY() + dY,
                    maxY = bounds.getHeight();

            Bounds p1Bounds = Pong.getInstance().player1.getSprite().getBoundsInParent();
            Bounds p2Bounds = Pong.getInstance().player2.getSprite().getBoundsInParent();
            if(p1Bounds.intersects(minX, minY, maxX, maxY) || p1Bounds.contains(minX, minY, maxX, maxY)){
                hits = true;
                hittingEntity = Pong.getInstance().player1;
            }
            else if(p2Bounds.intersects(minX, minY, maxX, maxY) || p2Bounds.contains(minX, minY, maxX, maxY)){
                hits = true;
                hittingEntity = Pong.getInstance().player2;
            }

            if(hits == true){
                Rectangle playerRect = (Rectangle)hittingEntity.getSprite();

                double closestX = clamp(circle.getCenterX(), playerRect.getX(), playerRect.getX() + playerRect.getWidth());
                double closestY = clamp(circle.getCenterY(), playerRect.getY(), playerRect.getY() + playerRect.getHeight());
                double colPx, colPy;

                if(closestX == circle.getCenterX() && closestY != circle.getCenterY()){
                    colPx = circle.getCenterX();
                    colPy = closestY;
                }
                else if(closestX != circle.getCenterX() && closestY == circle.getCenterY()){
                    colPx = closestX;
                    colPy = circle.getCenterY();
                }
                else{
                    double _x = circle.getCenterX() - closestX;
                    double _y = circle.getCenterY() - closestY;
                    double distance = Math.sqrt(_x * _x + _y * _y);

                    double angle = Math.atan2(_y, _x);
                    colPx = closestX + Math.cos(angle) * circle.getRadius();
                    colPy = closestY + Math.sin(angle) * circle.getRadius();
                }
                createParticleEffect(colPx, colPy, hittingEntity);

                double angleBetween = Math.atan2(dY, dX) - Math.atan2(playerRect.getHeight(), playerRect.getWidth());
                double reflection = Math.atan2(-dY, -dX) + 2 * angleBetween;

                double absReflection = Math.abs(reflection);
                if(absReflection < MIN_REFLECTION_ANGLE)
                    absReflection = MIN_REFLECTION_ANGLE;
                else if(absReflection > MAX_REFLECTION_ANGLE)
                    absReflection = MAX_REFLECTION_ANGLE;

                if(dX > 0)
                    reflection = -absReflection;
                else reflection = absReflection;

                double nAngle = Math.atan2(playerRect.getHeight(), playerRect.getWidth());
                double incidence = Math.atan2(circle.getCenterY() - colPy, circle.getCenterX() - colPx);

                dY = Math.sin(reflection);
                dX = Math.cos(reflection);
                double magnitude = Math.sqrt(dX * dX + dY * dY);
                dX = (dX / magnitude) * BALL_SPEED;
                dY = (dY / magnitude) * BALL_SPEED;
            }
        }


        circle.setCenterX(circle.getCenterX() + dX);
        circle.setCenterY(circle.getCenterY() + dY);
    }
    private void createParticleEffect(double x, double y, Player hittingEntity){
        Pong.getInstance().addEntity(new ParticleEffect(hittingEntity, x, y));
    }
    private double clamp(double value, double min, double max){
        if(value < min) return min;
        else if(value > max) return max;
        else return value;
    }
    @Override
    public Shape getSprite(){
        return circle;
    }
}
