package hangman_code;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeyboardPane extends StackPane
{
    Button btA = new Button("a");
    Button btB = new Button("b");
    Button btC = new Button("c");
    Button btD = new Button("d");
    Button btE = new Button("e");
    Button btF = new Button("f");
    Button btG = new Button("g");
    Button btH = new Button("h");
    Button btI = new Button("i");
    Button btJ = new Button("j");
    Button btK = new Button("k");
    Button btL = new Button("l");
    Button btM = new Button("m");
    Button btN = new Button("n");
    Button btO = new Button("o");
    Button btP = new Button("p");
    Button btQ = new Button("q");
    Button btR = new Button("r");
    Button btS = new Button("s");
    Button btT = new Button("t");
    Button btU = new Button("u");
    Button btV = new Button("v");
    Button btW = new Button("w");
    Button btX = new Button("x");
    Button btY = new Button("y");
    Button btZ = new Button("z");
    private String letterRecentlyEntered = "a";
    private boolean clicked = false;

    public KeyboardPane()
    {
        initiateKeyboard();

        btA.setOnAction(e -> {
            clicked = true;
            letterRecentlyEntered = "a";
        });
    }

    public void setLetterRegistered(boolean value)
    {
        clicked = value;
    }

    public boolean isLetterRegistered()
    {
        return clicked;
    }

    public String getLetterRecentlyEntered()
    {
        return letterRecentlyEntered;
    }

    private void initiateKeyboard()
    {
        Button[] btArray = {btA, btB, btC, btD, btE, btF, btG, btH, btI
                , btJ, btK, btL, btM, btN, btO, btP, btQ, btR, btS, btT, btU, btV
                , btW, btX, btY, btZ};

        HBox firstRow = new HBox();
        HBox secondRow = new HBox();
        HBox thirdRow = new HBox();
        VBox keyContainer = new VBox(firstRow, secondRow, thirdRow);
        getChildren().add(keyContainer);

        firstRow.setAlignment(Pos.CENTER);
        secondRow.setAlignment(Pos.CENTER);
        thirdRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < btArray.length; i++)
        {
            if (i < 10)
            {
                firstRow.getChildren().add(btArray[i]);
            }
            else if (i < 19)
            {
                secondRow.getChildren().add(btArray[i]);
            }
            else
            {
                thirdRow.getChildren().add(btArray[i]);
            }
        }
    }
}
