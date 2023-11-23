package com.example.dictionary.word;

import java.util.Objects;

public class Word {
    private String word;
    private String def;

    /**
     * Constructs a Word object with a word and its definition.
     *
     * @param word The word itself.
     * @param def The definition of the word.
     */
    public Word(String word, String def) {
        this.word = word.trim();
        this.def = def.trim();
    }

    /**
     * Retrieves the word.
     *
     * @return The word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the word.
     *
     * @param word The word to set.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Retrieves the definition of the word.
     *
     * @return The definition.
     */
    public String getDef() {
        return def;
    }

    /**
     * Sets the definition of the word.
     *
     * @param def The definition to set for the word.
     */
    public void setDef(String def) {
        this.def = def;
    }

    /**
     * Checks if two Word objects are equal based on their word and definition.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) && Objects.equals(def, word1.def);
    }

    /**
     * Generates a hash code for the Word object based on its word and definition.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(word, def);
    }
}
