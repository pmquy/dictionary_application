package com.example.dictionary.game;

import com.example.dictionary.word.Word;
import com.example.dictionary.controller.Game2Controller;
import com.example.dictionary.user.UserManager;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Game2 extends AGame{
    public static final int GAME_ID = 2;
    private ArrayList<Word> data;

    /**
     * Generates a list of words for the game.
     * Retrieves random words from the user's collection.
     *
     * @return An ArrayList containing shuffled word associations.
     */
    public ArrayList<String> generate() {
        ArrayList<String> res = new ArrayList<>();
        data = UserManager.getInstance().getCurrentUser().getRandomWords(Game2Controller.NUMBER_OF_QUESTIONS, word -> true);
        data.forEach(
            word -> {
                word.setWord(getTextFromHTML(word.getWord()));
                word.setDef(getTextFromHTML(word.getDef()));
                res.add(word.getWord());
                res.add(word.getDef());
            }
        );
        Collections.shuffle(res);
        return res;
    }

    /**
     * Checks whether the provided pair of strings constitutes a valid answer in the game.
     *
     * @param str1 The first string to check.
     * @param str2 The second string to check.
     * @return True if the provided strings form a valid answer pair, false otherwise.
     */
    public boolean checkAnswer(String str1, String str2) {
        return data.contains(new Word(str1, str2))
                || data.contains(new Word(str2, str1));
    }

    /**
     * Extracts text content from an HTML string.
     *
     * @param html The HTML content to be converted.
     * @return The text extracted from the HTML content.
     */
    private String getTextFromHTML(String html) {
        return Jsoup.parse(html).text();
    }
}
