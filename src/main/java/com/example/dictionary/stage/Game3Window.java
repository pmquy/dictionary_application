package com.example.dictionary.stage;

import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.scene.SuperScene;
import com.example.dictionary.user.UserManager;
import javafx.stage.Stage;

public class Game3Window extends Window {
    /**
     * Initializes the scenes associated with the Game3Window.
     */
    @Override
    protected void initScenes() {
        scenes.put(SceneEnum.GAME_3, new SuperScene(SceneEnum.GAME_3));
    }

    /**
     * Constructs a Game3Window object, setting up the Game3 scene and specific window behaviors.
     * Initializes the scene and applies the theme based on the current user's settings.
     */
    public Game3Window() {
        window = new Stage();
        window.setTitle("Game3");
        window.setResizable(false);
        changeScene(SceneEnum.GAME_3);
        window.setOnHiding(windowEvent -> {
            scenes.put(SceneEnum.GAME_3, new SuperScene(SceneEnum.GAME_3));
            scenes.get(SceneEnum.GAME_3).initTheme(SceneEnum.GAME_3, UserManager.getInstance().getCurrentUser().getTheme());
            changeScene(SceneEnum.GAME_3);
        });
    }
}
