package com.example.dictionary.stage;

import com.example.dictionary.scene.*;
import javafx.stage.Stage;

public class PrimaryWindow extends Window {

    private static PrimaryWindow instance;

    /**
     * Retrieves the instance of the PrimaryWindow.
     *
     * @return The instance of the PrimaryWindow.
     */
    public static PrimaryWindow getInstance() {
        return instance;
    }

    /**
     * Constructs the PrimaryWindow with the provided stage.
     * Initializes the scenes associated with the application.
     *
     * @param stage The primary stage of the application.
     */
    public PrimaryWindow(Stage stage) {
        super(stage, "Dictionary Application");
        instance = this;
        changeScene(SceneEnum.LOGIN);
    }


    /**
     * Initializes the scenes associated with the PrimaryWindow.
     */
    @Override
    public void initScenes() {
        scenes.put(SceneEnum.LOGIN, new SuperScene(SceneEnum.LOGIN));
        scenes.put(SceneEnum.GAME, new SuperScene(SceneEnum.GAME));
        scenes.put(SceneEnum.TRANSLATE, new SuperScene(SceneEnum.TRANSLATE));
        scenes.put(SceneEnum.USER, new SuperScene(SceneEnum.USER));
        scenes.put(SceneEnum.HOME, new SuperScene(SceneEnum.HOME));
        scenes.put(SceneEnum.THEME, new SuperScene(SceneEnum.THEME));
    }
}

