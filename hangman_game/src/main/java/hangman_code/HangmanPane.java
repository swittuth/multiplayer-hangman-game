package hangman_code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class HangmanPane extends StackPane
{
    private Circle head;
    private Line body;
    private Line leftArm;
    private Line rightArm;
    private Line leftLeg;
    private Line rightLeg;
    private VBox hangmanContainer;

    public HangmanPane()
    {
        drawHanger();
    }

    private void drawHanger()
    {
        drawHangman();

        Rectangle base = new Rectangle(50, 20, Color.WHITE);
        base.setStroke(Color.BLACK);
        base.setFill(Color.WHITE);

        // create connector to put on top of the pole
        Line poleExtension = new Line(0, 0, 0, 30);
        Line poleConnector = new Line(0, 0, 40, 0);
        Line rope = new Line(0,0,0,30);
        HBox poleExtensionContainer = new HBox(poleExtension);
        HBox poleConnectorContainer = new HBox(poleConnector);
        HBox ropeContainer = new HBox(rope);
        ropeContainer.setAlignment(Pos.TOP_RIGHT);
        poleConnectorContainer.setAlignment(Pos.TOP_CENTER);
        poleExtensionContainer.setAlignment(Pos.TOP_LEFT);
        HBox poleRopeExtensionContainer = new HBox(ropeContainer, poleConnectorContainer, poleExtensionContainer);
        // realign the extension with the pole below
        poleRopeExtensionContainer.setAlignment(Pos.CENTER);
        poleRopeExtensionContainer.setPadding(new Insets(0, poleConnector.getEndX() + 2, 0, 0));

        // pole along with the hangman
        Line pole = new Line(0, 0, 0, 70);
        HBox poleHangmanContainer = new HBox(hangmanContainer, pole);
        poleHangmanContainer.setPadding(new Insets(0, poleConnector.getEndX() + 14, 0, 0));
        poleHangmanContainer.setAlignment(Pos.CENTER);

        VBox hangerContainer = new VBox(poleRopeExtensionContainer, poleHangmanContainer, base);
        hangerContainer.setAlignment(Pos.CENTER);
        //hangerContainer.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        getChildren().add(hangerContainer);

    }

    private void drawHangman()
    {
        hangmanContainer = new VBox();
        hangmanContainer.setAlignment(Pos.TOP_CENTER);
        hangmanContainer.setPadding(new Insets(0, 30, 0, 0));

        head = new Circle(5);
        head.setStroke(Color.RED);
        head.setFill(Color.WHITE);
        HBox headContainer = new HBox(head);
        headContainer.setAlignment(Pos.CENTER);

        body = new Line(0, 0, 0, 30);
        HBox bodyContainer = new HBox(body);
        bodyContainer.setAlignment(Pos.CENTER);

        leftArm = new Line(0, 0, 10, -10);
        HBox leftArmContainer = new HBox(leftArm);
        leftArmContainer.setAlignment(Pos.TOP_RIGHT);

        rightArm = new Line(0, 0, 10, 10);
        HBox rightArmContainer = new HBox(rightArm);
        rightArmContainer.setAlignment(Pos.TOP_LEFT);

        HBox bodyArmContainer = new HBox(leftArmContainer, bodyContainer, rightArmContainer);
        bodyArmContainer.setAlignment(Pos.CENTER);

        leftLeg = new Line(0,0,5,-20);
        HBox leftLegContainer = new HBox(leftLeg);
        leftLegContainer.setAlignment(Pos.TOP_RIGHT);

        rightLeg = new Line(0,0,5,20);
        HBox rightLegContainer = new HBox(rightLeg);
        rightLegContainer.setAlignment(Pos.TOP_LEFT);

        HBox legsContainer = new HBox(leftLegContainer, rightLegContainer);
        legsContainer.setAlignment(Pos.TOP_CENTER);


        hangmanContainer.getChildren().addAll(headContainer, bodyArmContainer, legsContainer);
    }
}
