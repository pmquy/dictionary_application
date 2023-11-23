package com.example.dictionary.controller;

import com.example.dictionary.api.TextToSpeech;
import com.example.dictionary.api.Translate;

public class Connector {
    /**
     * Execute a search operation for a given word.
     *
     * @param word The word to search for.
     */
    public void find(String word) {
        TranslateController.getInstance().find(word);
    }

    /**
     * Translates a word from English to Vietnamese.
     *
     * @param word The word to be translated.
     * @return The translated word, or an empty string if translation fails.
     */
    public String trans(String word) {
        try {
            return Translate.translate(word, "en", "vi");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Converts a word into speech.
     *
     * @param word The word to be converted into speech.
     */
    public void speak(String word) {
        TextToSpeech.textToSpeech(word);
    }
}
