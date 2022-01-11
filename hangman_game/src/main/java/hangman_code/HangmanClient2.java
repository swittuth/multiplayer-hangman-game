package hangman_code;

// should include read server feedback on each response from the two clients

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanClient2 extends Application
{
    private Socket socket;
    private BorderPane main;
    private HangmanPane hangmanPane;
    private KeyboardPane keyboardPane;
    private GuessWordPane guessWordPane;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private String keyWord;
    private boolean endGame = false;
    private String letter;
    private boolean firstPlayer;
    private boolean playerTurn;
    private int index = -5;
    private Label status = new Label();
    private VBox wordKeyboardContainer;
    private int gameStatus;

    private final int WON = 1;
    private final int LOST = 2;
    private final int DRAW = 3;
    private final int AWAIT = 4;

    @Override
    public void start(Stage primaryStage)
    {
        initiateConnectionToServer("localhost", 8000);

        hangmanPane = new HangmanPane();
        keyboardPane = new KeyboardPane();
        guessWordPane = new GuessWordPane(keyWord);

        main = new BorderPane();
        Scene scene = new Scene(main, 500, 500);

        wordKeyboardContainer = new VBox(guessWordPane, keyboardPane);
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

        main.requestFocus();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(new PlayGame());

    }

    class PlayGame implements Runnable
    {
        @Override
        public void run()
        {
            if (firstPlayer)
            {
                Platform.runLater(() -> {
                    status.setText("You are player #1 take your guess!");
                });
            }
            else
            {
                Platform.runLater(() -> {
                    status.setText("Your are player #2 please wait for other player's turn");
                });
            }

            while (!endGame)
            {
                if (firstPlayer)
                {
                    try
                    {
                        waitForPlayerAction();
                        registerMove();
                        readServerFeedback();
                    }
                    catch (InterruptedException ex)
                    {
                        System.out.println(ex + "\n");
                    }
                }
                else
                {
                    try
                    {
                        readServerFeedback();
                        waitForPlayerAction();
                        registerMove();
                    }
                    catch (InterruptedException ex)
                    {
                        System.out.println(ex + "\n");
                    }
                }
            }

            if (gameStatus == WON)
            {
                main.getChildren().clear();
                main.setCenter(new Label("YOU WON!"));
            }
            else if (gameStatus == AWAIT)
            {
                try
                {
                    index = fromServer.readInt();
                    letter = fromServer.readUTF();
                    playerTurn = fromServer.readBoolean();
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }

                if (letter.equals("L"))
                {
                    main.getChildren().clear();
                    main.setCenter(new Label("YOU LOST..."));
                }
                else if (letter.equals("D"))
                {
                    main.getChildren().clear();
                    main.setCenter(new Label("DRAW!"));
                }
            }
        }
    }

    private void waitForPlayerAction() throws InterruptedException
    {

        while (!keyboardPane.isLetterRegistered() && playerTurn)
        {
            Thread.sleep(100);
        }
    }

    private void initiateConnectionToServer(String hostName, int portNumber)
    {
        try
        {
            socket = new Socket(hostName, portNumber);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());

            keyWord = fromServer.readUTF();
            firstPlayer = fromServer.readBoolean();
            playerTurn = firstPlayer;
        }
        catch (IOException ex)
        {
            System.out.println(ex + "\n");
        }
    }

    private void updateGuessWordPane()
    {
        Platform.runLater(() -> {
            if (playerTurn)
            {
                if (index >= 0)
                {
                    guessWordPane.fill(letter);
                    wordKeyboardContainer = new VBox(guessWordPane, keyboardPane);
                    main.setCenter(wordKeyboardContainer);
                    status.setText("Correct! Keep Going!");
                }
                else
                {
                    hangmanPane.decreaseLife();
                    if (hangmanPane.hanged())
                    {
                        try
                        {
                            toServer.writeUTF("L");
                        }
                        catch (IOException ex)
                        {
                            System.out.println(ex + "\n");
                        }

                    }
                    else
                    {
                        status.setText("Wrong... Waiting for the other player's turn");
                    }
                }

                if (guessWordPane.filled())
                {
                    status.setText("You won the game!");
                    try
                    {
                        toServer.writeUTF("W");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex + "\n");
                    }
                    endGame = true;
                    gameStatus = WON;
                }
            }
            else
            {
                if (index >= 0)
                {
                    guessWordPane.fill(letter);
                    wordKeyboardContainer = new VBox(guessWordPane, keyboardPane);
                    main.setCenter(wordKeyboardContainer);
                    status.setText("Other player guessed correctly!");
                }
                else
                {
                    status.setText("Other player guessed incorrectly... Your turn!");
                }
            }
        });
    }


    public void readServerFeedback()
    {
        try
        {
            index = fromServer.readInt();
            letter = fromServer.readUTF();

            if (letter.equals("L"))
            {
                Platform.runLater(() -> {
                    status.setText("Other player guessed the word. You lost...");
                });
                gameStatus = LOST;
                endGame = true;
            }
            else if (letter.equals("K"))
            {
                System.out.println("Have to wait for player 2 turn");
                gameStatus = AWAIT;
                endGame = true;
            }
            else
            {
                updateGuessWordPane();
                Thread.sleep(1000);
                playerTurn = fromServer.readBoolean();
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex + "\n");
        }
        catch (InterruptedException ex)
        {
            System.out.println(ex + "\n");
        }

    }

    public void registerMove()
    {
        Platform.runLater(() -> {
            if (keyboardPane.isLetterRegistered())
            {
                String letter = keyboardPane.getLetterRecentlyEntered();
                keyboardPane.setLetterRegistered(false);
                try
                {
                    toServer.writeUTF(letter);
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }

            }
        });
    }

}