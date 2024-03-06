package game;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pong.Pong;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEffect implements IEntity {
    private static final int MAX_PARTICLE_COUNT = 10;
    private static final int MIN_PARTICLE_COUNT = 5;
    private static final Random random = new Random();
    private static final double MAX_SPEED = 3;
    private static final double MIN_SPEED = 0.5;
    private static final long ANIMATION_TIME = 750;
    private static final int MAX_PARTICLE_RADIUS = 5;
    private static final int MIN_PARTICLE_RADIUS = 1;

    private long time;
    private int dX;
    private Player player;
    private double startSpeed;
    private List<Shape> particles = new ArrayList<Shape>();


    public ParticleEffect(Player player, double startX, double startY){
        if(player.isLeftPlayer()){
            startX += ((Rectangle)player.getSprite()).getWidth();
        }
        else{
            //startX += ((Rectangle)player.getSprite()).getWidth();
        }

        this.player = player;
        this.time = Pong.getTime();
        GaussianBlur blurEffect = new GaussianBlur(Math.random() * 10);

        startSpeed = Math.random() * (MAX_SPEED - MIN_SPEED + 1) + MIN_SPEED;
        double particlesCount = Math.random() * (MAX_PARTICLE_COUNT - MIN_PARTICLE_COUNT + 1) + MIN_PARTICLE_COUNT;

        for(int i = 0;i < particlesCount;i++){
            double radius = Math.random() * (MAX_PARTICLE_RADIUS - MIN_PARTICLE_RADIUS + 1) + MIN_PARTICLE_RADIUS;
            Circle particle = new Circle(radius, player.getColor());
            particle.setBlendMode(BlendMode.ADD);
            particle.setCenterX(startX);
            particle.setCenterY(startY);
            if(radius > (MAX_PARTICLE_RADIUS / 2) + MIN_PARTICLE_RADIUS){
                particle.setEffect(blurEffect);
            }
            particles.add(particle);
        }

        Pong.getInstance().addChildrens(particles);
    }
    @Override
    public void update(){
        long t = Pong.getTime() - time;
        if(t > ANIMATION_TIME){
            Pong.getInstance().removeChildrens(particles);
            Pong.getInstance().removeEntity(this);
            return;
        }

        double speed = startSpeed - (t * startSpeed / (float)ANIMATION_TIME);
        if(!player.isLeftPlayer()){
            speed *= -1;
        }

        double tintModifier = (t / (float)ANIMATION_TIME);
        double opacity = 1.0f - tintModifier;
        Color playerColor = (Color)player.getColor();
        Color newColor = Color.color(
                playerColor.getRed(),
                playerColor.getGreen(),
                playerColor.getBlue(),
                opacity).brighter();

        for(int i = 0;i < particles.size();i++){
            Circle particle = (Circle)particles.get(i);
            double angle = Math.toRadians((90.0 * i / particles.size()) - 45);
            double xSpeed = Math.cos(angle) * speed;
            double ySpeed = Math.sin(angle) * speed;

            if(Math.random() < 0.15){
                ySpeed *= -1;
            }
            if(i % 2 == 0){
                xSpeed /= 2;
                ySpeed /= 2;
                if(i % 3 == 0){
                    xSpeed *= 0.5;
                }
            }
            else if(i % 3 == 0){
                xSpeed /= 1.5;
                ySpeed /= 1.75;
            }

            particle.setFill(newColor);
            particle.setCenterX(particle.getCenterX() + xSpeed);
            particle.setCenterY(particle.getCenterY() + ySpeed);
        }
    }
    private double lerp(double a, double b, double value){
        return a + (b - a) * value;
    }
    @Override
    public Shape getSprite(){
        return null;
    }
}
