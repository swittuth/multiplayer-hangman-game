package hangman_code;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanClient2 extends Application
{
    private Socket socket;
    private StackPane hangmanPane;
    private KeyboardPane keyboardPane;
    private GuessWordPane guessWordPane;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private int lengthGuessWord = 0;
    private boolean playerTurn = false;
    private boolean endGame = false;
    private Thread startGame;
    private Thread waitGameConnection;
    private Label status = new Label();

    @Override
    public void start(Stage primaryStage)
    {
        initiateConnectionToServer("localhost", 8000);
        waitGameConnection = new Thread(new waitPlayerConnection(primaryStage));
        waitGameConnection.start();

    }

    public void initiateConnectionToServer(String hostName, int portNumber)
    {
        try
        {
            socket = new Socket(hostName, portNumber);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex)
        {
            System.out.println(ex + "\n");
        }
    }

    class initiateGame implements Runnable
    {
        Stage primaryStage;

        public initiateGame(Stage primaryStage)
        {
            this.primaryStage = primaryStage;
        }

        @Override
        public void run()
        {
            Platform.runLater(() -> {

                initiateHangman();
                keyboardPane = new KeyboardPane();
                initiateGuessWord(lengthGuessWord);

                BorderPane main = new BorderPane();
                Scene scene = new Scene(main);

                VBox wordKeyboardContainer = new VBox(guessWordPane, keyboardPane);
                wordKeyboardContainer.setAlignment(Pos.CENTER);
                HBox statusContainer = new HBox(status);
                statusContainer.setAlignment(Pos.CENTER);
                main.setBottom(statusContainer);

                main.setTop(hangmanPane);
                main.setCenter(wordKeyboardContainer);
                BorderPane.setAlignment(hangmanPane, Pos.CENTER);
                BorderPane.setMargin(hangmanPane, new Insets(10, 0, 0, 0));
                primaryStage.setTitle("Hangman Client 2");
                primaryStage.setScene(scene);
                primaryStage.show();

                keyboardPane.requestFocus();
                playGame();
            });
        }
    }

    public void playGame()
    {

    }


    class waitPlayerConnection implements Runnable
    {
        Stage primaryStage;

        public waitPlayerConnection(Stage primaryStage)
        {
            this.primaryStage = primaryStage;
        }

        @Override
        public void run()
        {
            while (lengthGuessWord == 0)
            {
                try
                {
                    lengthGuessWord = fromServer.readInt();
                    playerTurn = fromServer.readBoolean();
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }
            }
            startGame = new Thread(new initiateGame(primaryStage));
            if (playerTurn)
            {
                status.setText("Your turn");
            }
            else
            {
                status.setText("Waiting for the other player");
            }

            startGame.start();
        }
    }

    private void initiateGuessWord(int length)
    {
        guessWordPane = new GuessWordPane(length);
    }



    private void initiateHangman()
    {
        hangmanPane = new StackPane(new HangmanPane());
    }

}
