package hangman_code;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanServer extends Application
{
    private Hangman keyWord = new Hangman("delicate");
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private TextArea ta = new TextArea();
    private int numberClients = 0;
    private ArrayList<Socket> players = new ArrayList<>();
    private boolean endGame = false;
    private boolean firstPlayerTurn = true;
    private DataInputStream fromSocket1;
    private DataOutputStream toSocket1;
    private DataInputStream fromSocket2;
    private DataOutputStream toSocket2;

    @Override
    public void start(Stage primaryStage)
    {
        Scene scene = new Scene(new ScrollPane(ta));
        primaryStage.setTitle("Hangman Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try
            {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> {
                   ta.appendText("Hangman Server started at " + new Date() + "\n");
                });

                while (numberClients < 2)
                {
                    Socket socket = serverSocket.accept();
                    Platform.runLater(() -> {
                        ta.appendText("Client #" + numberClients + " connected\n");
                    });

                    if (numberClients < 2)
                    {
                        Platform.runLater(() -> {
                            ta.appendText("Waiting for Client #" + (++numberClients) + "\n");
                        });
                    }
                    players.add(socket);
                }

                startGame();
            }
            catch (IOException ex)
            {
                ta.appendText(ex + "\n");
            }
        }).start();
    }

    public void startGame()
    {
        Socket socket1 = players.get(0);
        Socket socket2 = players.get(1);

        try
        {
            fromSocket1 = new DataInputStream(socket1.getInputStream());
            fromSocket2 = new DataInputStream(socket2.getInputStream());
            toSocket1 = new DataOutputStream(socket1.getOutputStream());
            toSocket2 = new DataOutputStream(socket2.getOutputStream());

            toSocket1.writeInt(keyWord.getKeyWord().size());
            toSocket2.writeInt(keyWord.getKeyWord().size());

        }
        catch (IOException ex)
        {
            ta.appendText(ex + "\n");
        }

        new Thread(() -> {
            while (!endGame)
            {
                try
                {
                    if (firstPlayerTurn)
                    {
                        endGame = fromSocket1.readBoolean();
                    }
                    else
                    {
                        endGame = fromSocket2.readBoolean();
                    }
                }
                catch(IOException ex)
                {
                    ta.appendText(ex + "\n");
                }

            }
        }).start();

        while (!endGame)
        {
            char letter;
            ArrayList<Character> word = keyWord.getKeyWord();
            try
            {
                if (firstPlayerTurn)
                {
                    letter = fromSocket1.readChar();

                    if (word.contains(letter))
                    {
                        toSocket1.writeInt(1);
                    }
                    else
                    {
                        toSocket1.writeInt(0);
                    }
                }
                else
                {
                    letter = fromSocket2.readChar();

                    if (word.contains(letter))
                    {
                        toSocket2.writeInt(1);
                    }
                    else
                    {
                        toSocket2.writeInt(0);
                    }
                }


            }
            catch (IOException ex)
            {
                ta.appendText(ex + "\n");
            }

            checkWinGame();
            firstPlayerTurn = !firstPlayerTurn;

        }
    }

    public void checkWinGame()
    {
        // check if the player won the game
        if (keyWord.getKeyWord().size() == 0)
        {
            try
            {
                if (firstPlayerTurn)
                {
                    toSocket1.writeBoolean(true);
                }
                else
                {
                    toSocket2.writeBoolean(true);
                }
            }
            catch(IOException ex)
            {
                ta.appendText(ex + "\n");
            }

            endGame = true;
        }
        else
        {
            try
            {
                if (firstPlayerTurn)
                {
                    toSocket1.writeBoolean(false);
                }
                else
                {
                    toSocket2.writeBoolean(false);
                }
            }
            catch(IOException ex)
            {
                ta.appendText(ex + "\n");
            }
        }
    }

}