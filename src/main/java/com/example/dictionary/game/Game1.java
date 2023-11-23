package com.example.dictionary.game;

import com.example.dictionary.word.Word;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.example.dictionary.user.UserManager;
import org.jsoup.Jsoup;

public class Game1 extends AGame{
    public static final int GAME_ID = 1;
    public static final int NUM_QUESTION = 5;
    public static final int MAX_FAULT = 3;
    public static final int MAX_HINT = 3;

    /**
     * Initializes the game.
     */
    public void init() {
        map = new HashMap<>();
        UserManager.getInstance().getCurrentUser().getWords().values().forEach(word -> {
            map.put(getTextFromHTML(word.getWord()),
                    new Word(getTextFromHTML(word.getWord()), getTextFromHTML(word.getDef())));
        });
        listWord = new ArrayList<>(this.map.keySet());
        listDef = this.map.values().stream().map(Word::getDef).distinct().
                collect(Collectors.toCollection(ArrayList::new));
        currentQuestionIndex = 0;
        solved = 0;
        fault = 0;
        isSolved.clear();
        for(int i = 0; i < NUM_QUESTION; i ++) isSolved.add(false);
        setReady(listWord.size() >= NUM_QUESTION && listDef.size() >= NUM_QUESTION);
        if(! isReady()) {
            return;
        }
        generateRandomQuestions();
    }

    /**
     * Checks if the game is finished based on the number of remaining questions or maximum faults reached.
     *
     * @return True if the game is finished; otherwise, false.
     */
    public boolean checkFinish() {
        return questionRemain() == 0 || fault >= MAX_FAULT;
    }

    /**
     * Handles the user's submission by verifying the answer correctness and updating the solved count or fault count.
     */
    public void submit() {
        if(getAnswerIndex() == getSelectedAns()) {
            solved++;
            isSolved.set(currentQuestionIndex, true);
        }
        else {
            fault ++;
        }
    }

    /**
     * Proceeds to the next question while skipping the already solved ones.
     */
    public void toNextQuestion() {
        do {
            currentQuestionIndex ++;
            currentQuestionIndex %= NUM_QUESTION;
        }
        while (isSolved.get(currentQuestionIndex));
        selectAns(- 1);
    }

    /**
     * Calculates the number of remaining questions.
     *
     * @return The number of questions remaining.
     */
    public int questionRemain() {
        return NUM_QUESTION - solved;
    }

    /**
     * Calculates the progress based on the number of questions solved.
     *
     * @return The progress percentage.
     */
    public double getProgress() {
        return 1.0 * solved / NUM_QUESTION;
    }

    /**
     * Generates random questions by shuffling word and definition lists and creating answer choices based on words and definitions.
     */
    private void generateRandomQuestions() {
        Random rd = new Random();
        Collections.shuffle(listWord);
        Collections.shuffle(listDef);
        questions.clear();
        answers.clear();
        questionsSelections.clear();
        selectedAnswers.clear();
        for(int i = 0; i < NUM_QUESTION; i ++) {
            ArrayList<String> selections = new ArrayList<>();
            String question;
            String answer;

            if((int) (Math.random() * 1000) % 2 == 0) {
                // cho từ, chọn nghĩa đúng
                question = listWord.get(i % listWord.size());
                answer = map.get(question).getDef();
                Set<Integer> tmp = new HashSet<>();
                while(tmp.size() < 3) {
                    tmp.add(rd.nextInt(listDef.size()));
                }
                selections = tmp.stream().map(integer -> listDef.get(integer)).
                        collect(Collectors.toCollection(ArrayList::new));
                if (!selections.contains(answer)) {
                    selections.set(2, answer);
                }
            } else {
                // cho nghĩa, chọn từ đúng
                question = listDef.get(i % listDef.size());
                answer = map.keySet().stream().filter(w -> map.get(w).getDef().equals(question)).toList().get(0);
                Set<Integer> tmp = new HashSet<>();
                while(tmp.size() < 2) {
                    int ii = rd.nextInt(listWord.size());
                    if (! listWord.get(ii).equals(answer)) tmp.add(ii);
                }
                selections = tmp.stream().map(integer -> listWord.get(integer)).
                        collect(Collectors.toCollection(ArrayList::new));
                selections.add(answer);
            }
            Collections.shuffle(selections);
            questions.add(question);
            answers.add(answer);
            questionsSelections.add(selections);
            selectedAnswers.add(-1);
        }
    }

    private Map<String, Word> map;
    private ArrayList<String> listWord;
    private ArrayList<String> listDef;
    private final ArrayList<String> questions = new ArrayList<>();
    private final ArrayList<String> answers = new ArrayList<>();
    private final ArrayList<ArrayList<String>> questionsSelections = new ArrayList<>();
    private final ArrayList<Integer> selectedAnswers = new ArrayList<>();
    private final ArrayList<Boolean> isSolved = new ArrayList<>();
    private int solved = 0;
    private int fault = 0;
    private int currentQuestionIndex = 0;
    /**
     * Retrieves the number of questions solved correctly in the game.
     *
     * @return The number of questions solved.
     */
    public int getSolved() {
        return solved;
    }
    /**
     * Retrieves the number of faults made in the game.
     *
     * @return The number of faults.
     */
    public int getFault() {
        return fault;
    }
    /**
     * Retrieves the current question in the game.
     *
     * @return The current question.
     */
    public String getQuestion() {
        return questions.get(currentQuestionIndex);
    }
    /**
     * Retrieves the correct answer for the current question.
     *
     * @return The correct answer for the current question.
     */
    public String getAnswer() {
        return answers.get(currentQuestionIndex);
    }
    /**
     * Retrieves the index of the correct answer within the answer choices for the current question.
     *
     * @return The index of the correct answer.
     */
    public int getAnswerIndex() {
        return questionsSelections.get(currentQuestionIndex).indexOf(getAnswer());
    }
    /**
     * Retrieves the answer choices for the current question.
     *
     * @return The list of answer choices.
     */
    public ArrayList<String> getSelections() {
        return questionsSelections.get(currentQuestionIndex);
    }
    /**
     * Sets the selected answer for the current question.
     *
     * @param selectedAns The index of the selected answer.
     */
    public void selectAns(int selectedAns) {
        selectedAnswers.set(currentQuestionIndex, selectedAns);
    }
    /**
     * Retrieves the index of the selected answer for the current question.
     *
     * @return The index of the selected answer.
     */
    public int getSelectedAns() {
        return selectedAnswers.get(currentQuestionIndex);
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
