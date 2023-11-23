package com.example.dictionary.stage;

import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.scene.SuperScene;
import javafx.stage.Stage;
import java.util.HashMap;

public abstract class Window {
    protected Stage window;

    protected final HashMap<SceneEnum, SuperScene> scenes = new HashMap<>();

    /**
     * Changes the scene displayed in the window based on the provided scene type.
     *
     * @param sceneType The type of scene to change to.
     */
    public void changeScene(SceneEnum sceneType) {
        window.setScene(scenes.get(sceneType).getScene());
        window.setMaximized(false);

        switch (sceneType) {
            case GAME, GAME_1, GAME_2, GAME_3 -> {
                window.setResizable(false);
            }
            default -> {
                window.setResizable(true);
            }
        }
    }

    /**
     * Changes the theme of all scenes associated with the window.
     *
     * @param theme The theme identifier to apply to the scenes.
     */
    public void changeTheme(int theme) {
        scenes.forEach((k, v) -> {
            v.getScene().getStylesheets().clear();
            v.initTheme(k, theme);
        });
    }

    /**
     * Initializes scenes associated with the window.
     */
    protected abstract void initScenes();

    /**
     * Displays the window.
     */
    public void show() {
        window.show();
    }

    /**
     * Displays the window at the specified position.
     *
     * @param x The x-coordinate of the window's position.
     * @param y The y-coordinate of the window's position.
     */
    public void show(double x, double y) {
        window.show();
    }

    /**
     * Hides the window.
     */
    public void hide() {
        window.hide();
    }

    /**
     * Retrieves the window instance.
     *
     * @return The stage representing the window.
     */
    public Stage getWindow() {
        return window;
    }

    /**
     * Default constructors.
     */
    public Window() {
        window = new Stage();
        initScenes();
    }

    /**
     * Constructor with title.
     * @param title The tile of window.
     */
    public Window(String title) {
        window = new Stage();
        window.setTitle(title);
        initScenes();
    }

    /**
     * Constructor of Window.
     * @param stage The stage to be associated with the window.
     */
    public Window(Stage stage) {
        window = stage;
        initScenes();
    }

    /**
     * Constructor of Window.
     * @param stage The stage to be associated with the window.
     * @param title The title to be set for the window.
     */
    public Window(Stage stage, String title) {
        window = stage;
        window.setTitle(title);
        initScenes();
    }
}
