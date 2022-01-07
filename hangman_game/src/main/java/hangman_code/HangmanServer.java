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
                    numberClients += 1;
                    Platform.runLater(() -> {
                        ta.appendText("Client #" + (numberClients) + " connected\n");
                    });

                    if (numberClients < 2)
                    {
                        Platform.runLater(() -> {
                            ta.appendText("Waiting for Client #" + (numberClients + 1) + "\n");
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

        try {
            fromSocket1 = new DataInputStream(socket1.getInputStream());
            fromSocket2 = new DataInputStream(socket2.getInputStream());
            toSocket1 = new DataOutputStream(socket1.getOutputStream());
            toSocket2 = new DataOutputStream(socket2.getOutputStream());

            toSocket1.writeInt(keyWord.getKeyWord().size());
            toSocket1.writeBoolean(true); // indicating that it's the first player turn
            toSocket2.writeInt(keyWord.getKeyWord().size());
            toSocket2.writeBoolean(false);

        } catch (IOException ex) {
            ta.appendText(ex + "\n");
        }

        while (!endGame)
        {
            if (firstPlayerTurn)
            {
                try
                {
                    toSocket1.writeBoolean(true);
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }
            }
        }
    }


}