package hangman_code;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class HangmanPane extends Pane
{
    private Circle head;
    private Line body;
    private Line leftArm;
    private Line rightArm;
    private Line leftLeg;
    private Line rightLeg;


    public HangmanPane()
    {
        setLayoutX(100);
        setLayoutY(100);

        drawHanger();
    }

    public void drawHanger()
    {
        Rectangle base = new Rectangle(10, 15, Color.WHITE);
        base.setStroke(Color.BLACK);
        base.setLayoutX(getLayoutX() / 2);

        getChildren().add(base);
    }
}
