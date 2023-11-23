package com.example.dictionary.controller;

import com.example.dictionary.word.Data;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class DictionaryController extends SuperController {
    @FXML
    private TextField wordToFind;
    @FXML
    private ListView<String> listView;
    @FXML
    private WebView definitionView;

    /**
     * Initializes the dictionary view components and loads the word list.
     */
    @Override
    protected void initComponents() {
        loadWordList();
    }

    /**
     * Handles the selection of a word from the list and displays its definition.
     *
     * @param newValue The selected word from the list.
     */
    private void handleSelectWord(String newValue) {
        if (newValue != null) {
            definitionView.getEngine().loadContent(Data.getInstance().getSubData().get(newValue).getDef());
        } else {
            definitionView.getEngine().loadContent("");
        }
    }

    /**
     * Initializes event for the list view selection and word input changes.
     */
    @Override
    protected void initEvents() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleSelectWord(newValue));
        wordToFind.textProperty().addListener((observable, oldValue, newValue) -> loadWordList());
    }

    /**
     * Loads the word list based on the entered text in the search field.
     */
    private void loadWordList() {
        listView.getItems().clear();
        listView.getItems().addAll(Data.getInstance().getSubTrie().getAllWordsStartWith(wordToFind.getText()));
    }
}
