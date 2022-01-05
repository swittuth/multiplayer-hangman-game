package hangman_code;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanClient extends Application
{
    private StackPane hangmanPane;
    private StackPane keyboardPane;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private int lengthGuessWord = 0;

    @Override
    public void start(Stage primaryStage)
    {

        BorderPane main = new BorderPane();
        Scene scene = new Scene(main);

        initiateHangman();
        initiateKeyboard();
        main.setTop(hangmanPane);
        main.setBottom(keyboardPane);
        BorderPane.setAlignment(hangmanPane, Pos.CENTER);
        BorderPane.setMargin(hangmanPane, new Insets(10, 0, 0, 0));

        primaryStage.setTitle("Hangman Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        try
        {
            Socket socket = new Socket("localhost", 8000);

            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex)
        {
            System.out.println(ex + "\n");
        }

        new Thread(() -> {
            while (lengthGuessWord == 0)
            {
                try
                {
                    lengthGuessWord = fromServer.readInt();
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }
            }

            initiateGuessWord();
        }).start();

    }

    private void initiateGuessWord()
    {

    }

    private void initiateKeyboard()
    {
        HBox firstRow = new HBox();
        HBox secondRow = new HBox();
        HBox thirdRow = new HBox();
        VBox keyContainer = new VBox(firstRow, secondRow, thirdRow);
        keyboardPane = new StackPane(keyContainer);

        String[] firstRowKey = "qwertyuiop".split("");
        String[] secondRowKey = "asdfghjkl".split("");
        String[] thirdRowKey = "zxcvbnm".split("");

        firstRow.setAlignment(Pos.CENTER);
        secondRow.setAlignment(Pos.CENTER);
        thirdRow.setAlignment(Pos.CENTER);

        addKeyToKeyboard(firstRow, firstRowKey);
        addKeyToKeyboard(secondRow, secondRowKey);
        addKeyToKeyboard(thirdRow, thirdRowKey);
        keyContainer.setAlignment(Pos.CENTER);

    }

    private void addKeyToKeyboard(HBox row, String[] keys)
    {
        for (String s: keys)
        {
            row.getChildren().add(new Button(s));
        }
    }

    private void initiateHangman()
    {
        hangmanPane = new StackPane(new HangmanPane());
    }

}
