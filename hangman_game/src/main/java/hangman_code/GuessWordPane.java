package hangman_code;

import javafx.scene.shape.Line;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class GuessWordPane extends StackPane
{
    HBox wordBox = new HBox();

    public GuessWordPane(int length)
    {
        for (int i = 0; i < length; i++)
        {
            HBox line = new HBox(new Line(0,0,10,0));
            line.setAlignment(Pos.CENTER);
            line.setPadding(new Insets(0,3, 0 ,0));
            wordBox.getChildren().add(line);
        }

        wordBox.setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        getChildren().add(wordBox);
    }
}