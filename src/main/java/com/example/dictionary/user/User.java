package com.example.dictionary.user;

import com.example.dictionary.Application;
import com.example.dictionary.word.Trie;
import com.example.dictionary.word.Word;
import com.example.dictionary.controller.*;
import javafx.scene.image.Image;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {
    private int theme = 0;

    /**
     * Retrieves the theme set for the user interface.
     *
     * @return The theme value.
     */
    public int getTheme() {
        return theme;
    }

    public static final String SPLITTING_CHARACTERS = "<::>";
    public static final String IMAGE_PATH = "data/images/";
    public static final String DEFAULT_IMAGE = "default.jpg";
    public static final String WORDS_PATH = "data/words/";
    public static int lastUserId;
    private boolean isReceiveCoin = false;

    /**
     * Sets the theme for the user interface.
     *
     * @param theme The theme value to set.
     */
    public void setTheme(int theme) {
        this.theme = theme;
        Application.getInstance().changeTheme(theme);
    }

    /**
     * Retrieves the count of tasks.
     *
     * @return An ArrayList representing task counts.
     */
    public ArrayList<Integer> getTasksCount() {
        return tasksCount;
    }

    /**
     * Retrieves the tracking status of tasks.
     *
     * @return An ArrayList representing task tracking status.
     */
    public ArrayList<Boolean> getTasksTrack() {
        return tasksTrack;
    }

    private final ArrayList<Integer> tasksCount = new ArrayList<>();
    private final ArrayList<Boolean> tasksTrack = new ArrayList<>();
    private int hint = 0;

    /**
     * Sets the hint count for the user.
     *
     * @param hint The new hint count to be set.
     */
    public void setHint(int hint) {
        this.hint = hint;
        UserController.getInstance().initUserDetail();
    }

    /**
     * Checks whether the user has received a coin.
     *
     * @return True if the user has received a coin, otherwise false.
     */
    public boolean isReceiveCoin() {
        return isReceiveCoin;
    }

    /**
     * Marks that the user has received a coin and handles the streak change.
     */
    public void setReceiveCoin() {
        isReceiveCoin = true;
        Controller.handleChangeStreak();
    }

    /**
     * Retrieves the current coin count of the user.
     *
     * @return The total count of coins the user possesses.
     */
    public int getCoin() {
        return coin;
    }

    /**
     * Sets the coin count for the user and updates user details.
     *
     * @param coin The new coin count to be set.
     */
    public void setCoin(int coin) {
        this.coin = coin;
        UserController.getInstance().initUserDetail();
    }
    private int coin = 0;
    private String username;
    private String password;
    private final int id;
    private transient Image image;
    private final Set<LocalDate> loginDays = new HashSet<>();
    private int countOfAddWords;

    /**
     * Retrieves the count of added words.
     *
     * @return The count of added words.
     */
    public int getCountOfAddWords() {
        return countOfAddWords;
    }

    /**
     * Retrieves the number of searched words by the user.
     *
     * @return The count of searched words.
     */
    public int getCountOfSearchWords() {
        return countOfSearchWords;
    }


    /**
     * Handles the addition of a word, increments the count of added words,
     * updates task count, and triggers a change in statistics.
     */
    public void handleAddWord() {
        countOfAddWords++;
        tasksCount.set(2, tasksCount.get(2) + 1);
        Controller.handleChangeStatics();
    }

    /**
     * Handles the searching of a word, increments the count of searched words,
     * updates the task count, and triggers a change in statistics.
     */
    public void handleSearchWord() {
        tasksCount.set(0, tasksCount.get(0) + 1);
        countOfSearchWords++;
        Controller.handleChangeStatics();
    }

    /**
     * Handles the completion of a game, increments the game finish count,
     * and triggers a change in statistics.
     */
    public void handleFinishGame() {
        tasksCount.set(1, tasksCount.get(1) + 1);
        Controller.handleChangeStatics();
    }

    private int countOfSearchWords;

    /**
     * Retrieves the set of login dates for the user.
     *
     * @return The set containing the login dates.
     */
    public Set<LocalDate> getLoginDays() {
        return loginDays;
    }

    /**
     * Retrieves the image associated with the user profile.
     *
     * @return The user's profile image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    private transient ArrayList<String> allWords;
    private transient Map<String, Word> words;
    private transient Trie trie;

    /**
     * Adds a word to the user's dictionary.
     *
     * @param word The word object to be added.
     */
    public void addWord(Word word) {
        if (word != null) {
            words.put(word.getWord(), word);
            trie.insert(word.getWord());
            handleAddWord();
        }
    }

    /**
     * Removes a word from the user's dictionary.
     *
     * @param word The word to be removed.
     */
    public void removeWord(String word) {
        if (word != null) {
            words.remove(word.trim());
            trie.remove(word.trim());
        }
    }

    /**
     * Reads the user's word data from the file.
     */
    private void readData() {
        words = new HashMap<>();
        trie = new Trie();
        allWords = new ArrayList<>();
        try {
            FileReader fr = new FileReader(WORDS_PATH + id + ".txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            String[] list = builder.toString().split(SPLITTING_CHARACTERS);
            for (int i = 0; i + 1 < list.length; i += 2) {
                String word = list[i];
                String definition = list[i + 1];
                words.put(word.trim(), new Word(word, definition));
                trie.insert(word.trim());
            }
            allWords.addAll(words.keySet());
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the map of words in the user's dictionary.
     *
     * @return A map containing words as keys and their corresponding definitions as values.
     */
    public Map<String, Word> getWords() {
        return words;
    }

    /**
     * Retrieves the trie structure used for the user's dictionary.
     *
     * @return The Trie structure containing the words stored in the dictionary.
     */
    public Trie getTrie() {
        return trie;
    }

    /**
     * Writes the user's word data to the file, persisting the dictionary.
     * This method is used internally to save the dictionary data.
     */
    public void writeData() {
        try {
            FileWriter fw = new FileWriter(WORDS_PATH + id + ".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            words.forEach((key, value) -> {
                try {
                    bw.write(key + SPLITTING_CHARACTERS + value.getDef() + SPLITTING_CHARACTERS);
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of random words from the user's dictionary based on a given predicate.
     *
     * @param n The number of random words to retrieve.
     * @param predicate The predicate condition to filter the words.
     * @return An ArrayList of Word objects that satisfy the given predicate, chosen randomly.
     */
    public ArrayList<Word> getRandomWords(int n, Predicate<? super Word> predicate) {
        Random random = new Random();
        ArrayList<Word> res = new ArrayList<>();
        Set<Integer> st = new HashSet<>();
        ArrayList<Word> wordsFiltered = new ArrayList<>(words.values().stream().filter(predicate).collect(Collectors.toList()));

        if (n > wordsFiltered.size()) return res;
        while (st.size() < n) {
            st.add(random.nextInt(wordsFiltered.size()));
        }
        for (Integer t : st) {
            res.add(words.get(allWords.get(t)));
        }
        return res;
    }

    /**
     * Reads the user's image data from the filesystem and initializes the 'image' property.
     * If the user-specific image is not found, the default image is used instead.
     */
    private void readImage() {
        try {
            File file;
            if (new File(IMAGE_PATH + id + ".jpg").exists()) {
                file = new File(IMAGE_PATH + id + ".jpg");
            } else {
                file = new File(IMAGE_PATH + DEFAULT_IMAGE);
            }
            FileInputStream in = new FileInputStream(file);
            image = new Image(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the user's image using the provided image file path.
     *
     * @param path The path to the image file to set for the user.
     */
    public void setImage(String path) {
        try {
            Files.copy(Paths.get(path), Paths.get(IMAGE_PATH + id + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        image = new Image(path);
        Controller.handleChangeImage();
    }

    /**
     * Initializes a new User object with the provided username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = ++lastUserId;

        try {
            new File(WORDS_PATH + id + ".txt").createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tasksCount.add(0);
        tasksCount.add(0);
        tasksCount.add(0);

        tasksTrack.add(false);
        tasksTrack.add(false);
        tasksTrack.add(false);
    }

    /**
     * Logs the user into the system, updating login-related data and initializing user-specific settings.
     */
    public void login() {
        if (!loginDays.contains(LocalDate.now())) {
            isReceiveCoin = false;
            for(int i = 0;i < tasksCount.size(); i++) {
                tasksCount.set(i, 0);
            }
            for(int i = 0;i < tasksTrack.size(); i++) {
                tasksTrack.set(i, false);
            }
        }
        loginDays.add(LocalDate.now());
        readData();
        readImage();
    }

    /**
     * Sets the password for the user.
     *
     * @param password The new password to be set for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Removes user-specific data files associated with the user, including word and image files.
     */
    public void remove() {
        try {
            new File(WORDS_PATH + id + ".txt").delete();
            new File(IMAGE_PATH + id + ".jpg").delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the unique identifier (ID) of the user.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Provides a string representation of the User object.
     * The string contains the username, password, and user ID.
     *
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", password='" + password + '\'' + ", id=" + id + '}';
    }

    /**
     * Retrieves the number of hints available to the user.
     *
     * @return The number of hints the user has.
     */
    public int getHint() {
        return hint;
    }
}
