package pong;

import game.Ball;
import game.IEntity;
import game.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Pong extends Application {
    private static Pong instance;
    private static long time;
    private static long startTime = 0;
    public static Pong getInstance(){ return instance; }


    public int width = 800;
    public int height = 600;

    public Ball ball;
    public Player player1, player2;
    private Group root;

    private List<IEntity> entities = new ArrayList<IEntity>();

    @Override
    public void start(Stage stage) {
        instance = this;
        root = new Group();

        player1 = new Player("Player 1",50, Color.BLUE);
        player2 = new Player("Player 2", width - 50, Color.RED);
        ball = new Ball();

        root.getChildren().addAll(player1.getSprite(), player2.getSprite(), ball.getSprite());

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.BLACK);
        stage.setTitle("Pong");
        stage.setScene(scene);
        stage.show(); //test

        startTime = System.currentTimeMillis();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> updateEntities()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        entities.add(player1);
        entities.add(player2);
        entities.add(ball);
    }
    public static long getTime(){
        return time;
    }
    public void removeEntity(IEntity entity){ entities.remove(entity); }
    public void addEntity(IEntity entity){ entities.add(entity); }
    public void removeChildrens(List<Shape> shapes) {root.getChildren().removeAll(shapes);}
    public void addChildrens(List<Shape> shapes){
        root.getChildren().addAll(shapes);
    }
    public void addChildren(Shape shape){
        root.getChildren().add(shape);
    }
    public <T extends IEntity> T createEntity(Class<T> entityType) throws InstantiationException, IllegalAccessException {
        if(IEntity.class.isAssignableFrom(entityType)){
            T entity = entityType.newInstance();
            entities.add(entity);
            root.getChildren().add(entity.getSprite());
            return entity;
        }
        else return null;
    }
    private void updateEntities() {
        time = System.currentTimeMillis() - startTime;
        for (int i = 0;i < entities.size();i++)
            entities.get(i).update();
    }
    public static void main(String[] args) {
        launch();
    }
}