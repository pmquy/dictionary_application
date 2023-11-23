package com.example.dictionary.controller;

import com.example.dictionary.Application;
import com.example.dictionary.game.Game2;
import com.example.dictionary.game.Game3;
import com.example.dictionary.stage.WindowEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameController extends MainController {
    @FXML
    Button game1Nav;
    @FXML
    Button game2Nav;
    @FXML
    Button game3Nav;

    /**
     * Initializes the components.
     */
    @Override
    protected void initComponents() {
        super.initComponents();
    }

    /**
     * Initializes the events by calling the parent class's event initialization method.
     * Sets up event handling for navigation buttons to switch between game windows.
     */
    @Override
    protected void initEvents() {
        super.initEvents();
        game2Nav.setOnAction(event -> {
            Application.getInstance().showWindow(WindowEnum.GAME_2);
            currentGameId = Game2.GAME_ID;
        });
        game1Nav.setOnAction(event -> {
            Application.getInstance().showWindow(WindowEnum.GAME_1);
            currentGameId = Game2.GAME_ID;
        });
        game3Nav.setOnAction(event -> {
            Application.getInstance().showWindow(WindowEnum.GAME_3);
            currentGameId = Game3.GAME_ID;
        });
    }

    @FXML
    public void initialize() {
        super.initialize();
        instance = this;
    }


    /**
     * Represents the current active game's ID.
     */
    public static int currentGameId;

    /**
     * Retrieves the ID of the currently active game.
     *
     * @return The ID of the currently active game.
     */
    public static int getCurrentGameId() {
        return currentGameId;
    }

    /**
     * Singleton instance of the GameController class.
     */
    private static GameController instance;

    /**
     * Retrieves the singleton instance of the GameController.
     *
     * @return The singleton instance of the GameController.
     */
    public static GameController getInstance() {
        return instance;
    }
}
