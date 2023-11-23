package com.example.dictionary.word;

import java.util.*;

class TrieNode {
    private boolean endOfWord = false;

    public boolean getEndOfWord() {
        return this.endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    private final Map<Character, TrieNode> children = new TreeMap<>();
}

public class Trie {
    private TrieNode root = new TrieNode();
    /**
     * Inserts a word into the Trie.
     *
     * @param word The word to be inserted into the Trie.
     */
    public void insert(String word) {
        TrieNode temp = this.root;
        for(int i = 0; i < word.length(); i++) {
            if(temp.getChildren().get(word.charAt(i)) == null) {
                temp.getChildren().put(word.charAt(i), new TrieNode());
            }
            temp = temp.getChildren().get(word.charAt(i));
        }
        temp.setEndOfWord(true);
    }

    /**
     * Clears the Trie, removing all words.
     */
    public void clear() {
        root = new TrieNode();
    }

    /**
     * Removes a word from the Trie.
     *
     * @param word The word to be removed from the Trie.
     */
    public void remove(String word) {
        TrieNode temp = this.root;
        for(int i = 0; i < word.length(); i++) {
            if(temp.getChildren().get(word.charAt(i)) == null) {
                return;
            }
            temp = temp.getChildren().get(word.charAt(i));
        }
        temp.setEndOfWord(false);
    }

    /**
     * Inserts a list of words into the Trie.
     *
     * @param words The list of words to be inserted into the Trie.
     */
    public void insertAll(List<String> words) {
        for(String word : words) {
            this.insert(word);
        }
    }

    /**
     * Removes all words from the Trie.
     */
    public void removeAll() {
        this.root = new TrieNode();
    }

    /**
     * Retrieves all words that start with a given prefix.
     *
     * @param prefix The prefix to search for.
     * @return A list of words starting with the specified prefix.
     */
    public ArrayList<String> getAllWordsStartWith(String prefix) {
        ArrayList<String> words = new ArrayList<>();
        TrieNode temp = this.root;
        for(int i = 0; i < prefix.length(); i++) {
            if(temp.getChildren().get(prefix.charAt(i)) == null) {
                return words;
            }
            temp = temp.getChildren().get(prefix.charAt(i));
        }
        this.dfs(temp, prefix, words);
        return words;
    }

    /**
     * Performs a depth-first search traversal starting from the given node.
     *
     * @param node The current node in the Trie.
     * @param word The accumulated word formed so far.
     * @param words The list to store words found during traversal.
     */
    private void dfs(TrieNode node, String word, ArrayList<String> words) {
        if(node.getEndOfWord()) {
            words.add(word);
        }
        for(Character ch : node.getChildren().keySet()) {
            dfs(node.getChildren().get(ch), word + ch.toString(), words);
        }
    }
}
