package com.example.dictionary.stage;

import com.example.dictionary.controller.Game1Controller;
import com.example.dictionary.game.Game1;
import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.scene.SuperScene;
import com.example.dictionary.user.UserManager;
import javafx.stage.Stage;

public class Game1Window extends Window {
    /**
     * Initializes the scenes associated with the Game1Window.
     */
    @Override
    protected void initScenes() {
        scenes.put(SceneEnum.GAME_1, new SuperScene(SceneEnum.GAME_1));
    }

    /**
     * Constructs a Game1Window object, setting up the Game1 scene and specific window behaviors.
     * Initializes the scene and applies the theme based on the current user's settings.
     */
    public Game1Window() {
        super("Game1");
        window.setResizable(false);
        changeScene(SceneEnum.GAME_1);
        window.setOnHiding(windowEvent -> {
            scenes.put(SceneEnum.GAME_1, new SuperScene(SceneEnum.GAME_1));
            scenes.get(SceneEnum.GAME_1).initTheme(SceneEnum.GAME_1, UserManager.getInstance().getCurrentUser().getTheme());
            changeScene(SceneEnum.GAME_1);
        });
    }
}
