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
    private HBox headContainer;
    private HBox bodyContainer;
    private HBox leftArmContainer;
    private HBox rightArmContainer;
    private HBox leftLegContainer;
    private HBox rightLegContainer;
    private VBox hangmanContainer;
    private int life;

    public HangmanPane()
    {
        life = 6;
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
        headContainer = new HBox(head);
        headContainer.setAlignment(Pos.CENTER);
        headContainer.setVisible(false);

        body = new Line(0, 0, 0, 30);
        bodyContainer = new HBox(body);
        bodyContainer.setAlignment(Pos.CENTER);
        bodyContainer.setVisible(false);

        leftArm = new Line(0, 0, 10, -10);
        leftArmContainer = new HBox(leftArm);
        leftArmContainer.setAlignment(Pos.TOP_RIGHT);
        leftArmContainer.setVisible(false);

        rightArm = new Line(0, 0, 10, 10);
        rightArmContainer = new HBox(rightArm);
        rightArmContainer.setAlignment(Pos.TOP_LEFT);
        rightArmContainer.setVisible(false);

        HBox bodyArmContainer = new HBox(leftArmContainer, bodyContainer, rightArmContainer);
        bodyArmContainer.setAlignment(Pos.CENTER);

        leftLeg = new Line(0,0,5,-20);
        leftLegContainer = new HBox(leftLeg);
        leftLegContainer.setAlignment(Pos.TOP_RIGHT);
        leftLegContainer.setVisible(false);

        rightLeg = new Line(0,0,5,20);
        rightLegContainer = new HBox(rightLeg);
        rightLegContainer.setAlignment(Pos.TOP_LEFT);
        rightLegContainer.setVisible(false);

        HBox legsContainer = new HBox(leftLegContainer, rightLegContainer);
        legsContainer.setAlignment(Pos.TOP_CENTER);

        hangmanContainer.getChildren().addAll(headContainer, bodyArmContainer, legsContainer);
    }

    public void decreaseLife()
    {
        life -= 1;
        drawHangmanParts();
    }

    public void drawHangmanParts()
    {
        switch (life)
        {
            case 5:
                headContainer.setVisible(true);
                break;
            case 4:
                bodyContainer.setVisible(true);
                break;
            case 3:
                leftArmContainer.setVisible(true);
                break;
            case 2:
                rightArmContainer.setVisible(true);
                break;
            case 1:
                leftLegContainer.setVisible(true);
                break;
            case 0:
                rightLegContainer.setVisible(true);
                break;
            default:
                break;
        }
    }

    public boolean hanged()
    {
        return life == 0;
    }
}
