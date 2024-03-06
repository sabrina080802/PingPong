package game;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pong.Pong;

public class Player implements IEntity {
    public static final int RECT_WIDTH = 20;
    public static final int RECT_HEIGHT = 100;

    private static final int MAX_SPEED = 4;

    private Paint color;
    private Rectangle rect;
    private String name;
    private boolean isLeftPlayer;

    public Player(String name, int X, Paint color){
        this.color = color;
        this.name = name;
        this.isLeftPlayer = X < Pong.getInstance().width / 2;
        rect = new Rectangle(X - RECT_WIDTH / 2, Pong.getInstance().height / 2 - RECT_HEIGHT / 2, RECT_WIDTH, RECT_HEIGHT);
        rect.setFill(color);
    }
    public boolean isLeftPlayer(){ return isLeftPlayer; }
    public Paint getColor(){ return this.color; }
    public String getName(){
        return this.name;
    }
    @Override
    public void update() {
        Ball ball = Pong.getInstance().ball;
        double tY = rect.getY(), y = rect.getY();
        double midScreenX = Pong.getInstance().width / 2;
        if(isLeftPlayer){
            if(ball.getDirectionX() < 0 && ball.getX() < midScreenX){
                tY = ball.getY() - rect.getHeight() / 2;
            }
            else tY = Pong.getInstance().height / 2 - rect.getHeight() / 2;
        }
        else{
            if(ball.getDirectionX() > 0 && ball.getX() > midScreenX){
                tY = ball.getY() - rect.getHeight() / 2;
            }
            else tY = Pong.getInstance().height / 2 - rect.getHeight() / 2;
        }

        if(Math.abs(tY - y) > MAX_SPEED){
            if(y < tY)
                y += MAX_SPEED;
            else y -= MAX_SPEED;
        }
        else y = tY;
        rect.setY(y);
    }
    @Override
    public Shape getSprite(){
        return rect;
    }
}
