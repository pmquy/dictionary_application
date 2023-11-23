package com.example.dictionary.game;

import com.example.dictionary.word.Word;
import com.example.dictionary.user.UserManager;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Game3 extends AGame {
    public static final int GAME_ID = 3;
    private Map<String, Word> map;
    private ArrayList<Word> list;
    public static final int NUM_QUESTION = 5;
    private int solvedQuestion = 0;
    private int currentQuestionI = 0;
    public static int MAX_HINT = 3;

    public Game3() {

    }

    /**
     * Starts a new game by initializing word mappings and shuffling the word list.
     */
    public void newGame() {
        map = UserManager.getInstance().getCurrentUser().getWords();
        list = new ArrayList<Word>(map.values().stream().distinct().toList());
        solvedQuestion = 0;
        currentQuestionI = 0;
        Collections.shuffle(list);
    }

    /**
     * Checks if the game is ready to be played.
     *
     * @return True if the word list is not empty, false otherwise.
     */
    @Override
    public boolean isReady() {
        return !list.isEmpty();
    }

    /**
     * Retrieves the meaning of the current word being guessed.
     *
     * @return The meaning of the current word in the game.
     */
    public String getMeaning() {
        return getTextFromHTML(list.get(currentQuestionI).getDef());
    }

    /**
     * Retrieves the word to be guessed.
     *
     * @return The word to be guessed in the game.
     */
    public String getGuessWord() {
        return list.get(currentQuestionI).getWord();
    }

    /**
     * Checks if the provided player guess matches the word being guessed.
     *
     * @param playerGuess The player's guess for the word.
     * @return True if the player's guess matches the word, false otherwise.
     */
    public boolean isGuessedWord(String playerGuess) {
        return getGuessWord().equals(playerGuess);
    }

    /**
     * Moves to the next question in the game.
     */
    public void nextQuestion() {
        currentQuestionI ++;
        if(currentQuestionI == list.size()) {
            Collections.shuffle(list);
            currentQuestionI = 0;
        }
    }

    /**
     * Retrieves the game's progress as a ratio of solved questions to the total number of questions.
     *
     * @return The progress percentage of the game.
     */
    public double getProgress() {
        return 1.0 * solvedQuestion / NUM_QUESTION;
    }

    /**
     * Checks if all questions in the game have been solved.
     *
     * @return True if all questions have been solved, false otherwise.
     */
    public boolean isFinished() {
        return solvedQuestion == NUM_QUESTION;
    }

    /**
     * Increments the count of solved questions in the game.
     */
    public void increaseSolvedQuestion() {
        solvedQuestion ++;
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