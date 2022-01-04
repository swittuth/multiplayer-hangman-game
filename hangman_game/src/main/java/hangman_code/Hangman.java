package hangman_code;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Hangman
{
    private ArrayList<Character> keyWord = new ArrayList<>();
    private int life;

    public Hangman(String word)
    {
        // initiate the number of lives
        life = 6;
        for (int i = 0; i < word.length(); i++)
        {
            keyWord.add(word.toLowerCase().charAt(i));
        }
    }

    public ArrayList<Character> getKeyWord()
    {
        return keyWord;
    }

    public int getLife()
    {
        return life;
    }

    public void decreaseLife()
    {
        life -= 1;
    }

    public boolean isAlive()
    {
        return life > 0;
    }
}
