package hangman_code;

import java.util.ArrayList;
import javafx.scene.shape.Line;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class GuessWordPane extends StackPane
{
    private String keyWord;
    ArrayList<Character> keyWordList;
    private HBox wordBox = new HBox();
    private int numberSpaceFilled = 0;

    public GuessWordPane(String keyWord)
    {
        this.keyWord = keyWord;
        keyWordList = new ArrayList<>();

        for (int i = 0; i < keyWord.length(); i++)
        {
            keyWordList.add(keyWord.charAt(i));
        }

        for (int i = 0; i < keyWord.length(); i++)
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

    public void fill(String letter)
    {
        Character guessLetter = letter.charAt(0);
        for (int index = 0; index < keyWordList.size(); index++)
        {
            if (keyWordList.get(index).equals(guessLetter))
            {
                Label letterLabel = new Label(letter);
                letterLabel.setAlignment(Pos.CENTER);

                HBox letterContainer = new HBox(letterLabel);
                letterContainer.setAlignment(Pos.CENTER);

                HBox tempBox = (HBox)wordBox.getChildren().get(index);

                tempBox.getChildren().clear();
                tempBox.getChildren().add(letterContainer);

                getChildren().clear();
                getChildren().add(wordBox);
                numberSpaceFilled += 1;
            }
        }
    }

    public boolean hasLetter(String letter)
    {
        Character c = letter.charAt(0);
        return keyWordList.contains(c);
    }

    public boolean filled()
    {
        return numberSpaceFilled == keyWord.length();
    }
}