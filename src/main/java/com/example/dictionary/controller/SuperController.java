package com.example.dictionary.controller;

import javafx.fxml.FXML;

public abstract class SuperController {
    /**
     * Initializes the controller by calling specific methods for initializing components and event handling.
     */
    @FXML
    public void initialize() {
        initEvents();
        initComponents();
    }

    /**
     * Abstract method to initialize components.
     */
    protected abstract void initComponents();

    /**
     * Abstract method to initialize event handling.
     */
    protected abstract void initEvents();
}
