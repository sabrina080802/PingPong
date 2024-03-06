package game;

import javafx.scene.shape.Shape;

public interface IEntity {
    Shape getSprite();
    void update();
}
