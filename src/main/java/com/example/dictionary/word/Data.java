package com.example.dictionary.word;


import java.io.*;
import java.time.LocalDate;
import java.util.*;

public final class Data {
    private static Data instance = new Data();

    /**
     * Retrieves the instance of the Data class.
     *
     * @return The instance of the Data class.
     */
    public static Data getInstance() {
        return instance;
    }

    /**
     * Constructor for the Data class.
     */
    public Data() {
        readSubWords();
    }

    private final ArrayList<String> allSubWords = new ArrayList<>();
    private final Map<String, Word> subWords = new HashMap<>();
    private final Trie subTrie = new Trie();


    /**
     * Retrieves the sub word data.
     *
     * @return The map containing sub word data.
     */
    public Map<String, Word> getSubData() {
        return subWords;
    }

    /**
     * Retrieves the trie structure containing sub words.
     *
     * @return The trie structure of sub words.
     */
    public Trie getSubTrie() {
        return subTrie;
    }


    /**
     * Reads and processes sub words data from a file.
     */
    private void readSubWords() {
        try {
            FileReader fr = new FileReader("data/words/E_V.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            String[] list = builder.toString().split("</html>");
            for (int i = 0; i < list.length; i++) {
                String[] temp = list[i].split("<html>");
                subWords.put(temp[0].trim(), new Word(temp[0], temp[1]));
                subTrie.insert(temp[0].trim());
            }
            allSubWords.addAll(subWords.keySet());
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of random sub words based on the current day.
     *
     * @param n The number of sub words to retrieve.
     * @return An ArrayList containing randomly selected sub words.
     */
    public ArrayList<Word> getRandomWordsByDay(int n) {
        ArrayList<Word> res = new ArrayList<>();

        if (n <= 0 || allSubWords.size() < n)
            return res;
        LocalDate date = LocalDate.now();
        long seed = date.getYear() * 10000L + date.getMonthValue() * 100L + date.getDayOfMonth();
        Random random = new Random(seed);
        Set<Integer> st = new HashSet<>();

        while (st.size() < n) {
            int t = random.nextInt(allSubWords.size());
            st.add(t);
        }
        for (Integer t : st) {
            res.add(subWords.get(allSubWords.get(t)));
        }
        return res;
    }

    /**
     * Retrieves a list of random sub words based on the given date.
     *
     * @param n The number of sub words to retrieve.
     * @param date The specific date to use for randomization.
     * @return An ArrayList containing randomly selected sub words.
     */
    public ArrayList<Word> getRandomWordsByDay(int n, LocalDate date) {
        ArrayList<Word> res = new ArrayList<>();

        if (n <= 0 || allSubWords.size() < n)
            return res;
        long seed = date.getYear() * 10000L + date.getMonthValue() * 100L + date.getDayOfMonth();
        Random random = new Random(seed);
        Set<Integer> st = new HashSet<>();

        while (st.size() < n) {
            int t = random.nextInt(allSubWords.size());
            st.add(t);
        }
        for (Integer t : st) {
            res.add(subWords.get(allSubWords.get(t)));
        }
        return res;
    }

}
