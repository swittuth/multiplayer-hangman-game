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
    private String keyWord = "delicate";
    private TextArea ta = new TextArea();
    private int numberClients = 0;
    private ArrayList<Socket> players = new ArrayList<>();
    private boolean endGame = false;
    private boolean firstPlayerTurn = true;
    private DataInputStream fromSocket1;
    private DataOutputStream toSocket1;
    private DataInputStream fromSocket2;
    private DataOutputStream toSocket2;
    private boolean firstPlayerAlive;
    private boolean secondPlayerAlive;

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

                    if (numberClients == 1)
                    {
                        fromSocket1 = new DataInputStream(socket.getInputStream());
                        toSocket1 = new DataOutputStream(socket.getOutputStream());
                        toSocket1.writeUTF(keyWord);
                        toSocket1.writeBoolean(true); // indicate that the player is the first player
                        firstPlayerAlive = true;
                    }
                    else
                    {
                        fromSocket2 = new DataInputStream(socket.getInputStream());
                        toSocket2 = new DataOutputStream(socket.getOutputStream());
                        toSocket2.writeUTF(keyWord);
                        toSocket2.writeBoolean(false); // indicate that the player is the second player
                        secondPlayerAlive = true;
                    }

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
        Platform.runLater(() -> {
            ta.appendText("Game started!\n");
        });

        while (!endGame)
        {
            if (firstPlayerTurn)
            {
                try
                {
                    Platform.runLater(() -> {
                        ta.appendText("Player 1's turn: \n");
                    });

                    String letter = fromSocket1.readUTF();
                    Platform.runLater(() -> {
                        ta.appendText("Player 1 entered letter " + letter + "\n");
                    });

                    if (letter.equals("W"))
                    {
                        toSocket2.writeInt(-1);
                        toSocket2.writeUTF("L");
                        toSocket2.writeBoolean(false);

                        Platform.runLater(() -> {
                            ta.appendText("Player 1 WON the game!");
                        });
                    }
                    else if (letter.equals("L"))
                    {
                        firstPlayerAlive = false;
                        firstPlayerTurn = false;

                        if (secondPlayerAlive)
                        {
                            toSocket1.writeInt(-1);
                            toSocket1.writeUTF("K");
                            toSocket1.writeBoolean(false);
                            Platform.runLater(() -> {
                                ta.appendText("Player 1 couldn't guess... Player 2 turn to guess\n");
                            });
                        }
                        else
                        {
                            toSocket1.writeInt(-1);
                            toSocket1.writeUTF("D");
                            toSocket1.writeBoolean(false);
                        }
                    }
                    else
                    {
                        char charLetter = letter.charAt(0);
                        int index = keyWord.indexOf(charLetter);
                        if (secondPlayerAlive)
                        {
                            if (index >= 0)
                            {
                                toSocket2.writeInt(index);
                                toSocket2.writeUTF(letter);
                                toSocket2.writeBoolean(false); // indicate that not player 2 turn yet
                            }
                            else
                            {
                                toSocket2.writeInt(index);
                                toSocket2.writeUTF(letter);
                                toSocket2.writeBoolean(true); // indicate that not player 2 turn yet
                                firstPlayerTurn = !firstPlayerTurn;
                            }
                        }
                        toSocket1.writeInt(index);
                        toSocket1.writeUTF(letter);
                        toSocket1.writeBoolean(firstPlayerTurn); // to indicate that it's not player 1 turn anymore
                    }
                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }
            }
            else
            {
                try
                {
                    Platform.runLater(() -> {
                        ta.appendText("Player 2's turn: \n");
                    });
                    String letter = fromSocket2.readUTF();
                    Platform.runLater(() -> {
                        ta.appendText("Player 2 entered letter " + letter + "\n");
                    });

                    if (letter.equals("W"))
                    {
                        toSocket1.writeInt(-1);
                        toSocket1.writeUTF("L");
                        toSocket1.writeBoolean(false);

                        Platform.runLater(() -> {
                            ta.appendText("Player 2 WON the game!");
                        });
                    }
                    else if (letter.equals("L"))
                    {
                        secondPlayerAlive = false;
                        firstPlayerTurn = true;

                        if (firstPlayerAlive)
                        {
                            toSocket2.writeInt(-1);
                            toSocket2.writeUTF("K");
                            toSocket2.writeBoolean(false);
                            Platform.runLater(() -> {
                                ta.appendText("Player 2 couldn't guess... Player 1 turn to guess");
                            });
                        }
                        else
                        {
                            toSocket2.writeInt(-1);
                            toSocket2.writeUTF("D");
                            toSocket2.writeBoolean(false);
                        }
                    }
                    else
                    {
                        char charLetter = letter.charAt(0);
                        int index = keyWord.indexOf(charLetter);
                        if (firstPlayerAlive)
                        {
                            if (index >= 0)
                            {
                                toSocket1.writeInt(index);
                                toSocket1.writeUTF(letter);
                                toSocket1.writeBoolean(false);
                            }
                            else
                            {
                                toSocket1.writeInt(index);
                                toSocket1.writeUTF(letter);
                                toSocket1.writeBoolean(true);
                                firstPlayerTurn = !firstPlayerTurn;
                            }
                        }
                        toSocket2.writeInt(index);
                        toSocket2.writeUTF(letter);
                        toSocket2.writeBoolean(!firstPlayerTurn);
                    }

                }
                catch (IOException ex)
                {
                    System.out.println(ex + "\n");
                }
            }
        }
    }


}