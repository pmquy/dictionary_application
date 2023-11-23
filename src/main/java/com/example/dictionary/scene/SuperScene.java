package com.example.dictionary.scene;

import com.example.dictionary.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;

public class SuperScene {
    protected Scene scene;

    /**
     * Retrieves the Scene.
     *
     * @return The Scene object.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Initializes the theme for the scene based on the SceneEnum and theme number.
     *
     * @param type The SceneEnum representing the type of scene.
     * @param theme The theme number to be applied.
     */
    public void initTheme(SceneEnum type, int theme) {
        String path = type.getValue().replace("fxml", "css");
        File f = new File("data/css/theme-" + theme + "/" + path.replace("fxml", "css"));
        if(!f.exists())
            f = new File("data/css/theme-" + theme+"/common.css");
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
    }

    /**
     * Constructs a SuperScene based on the provided SceneEnum type.
     *
     * @param type The SceneEnum representing the type of scene.
     */
    public SuperScene(SceneEnum type) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            String path = type.getValue();
            Parent root = fxmlLoader.load(getClass().getResourceAsStream(path));
            scene = new Scene(root);
            if(type == SceneEnum.LOGIN)
                initTheme(type, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a SuperScene object.
     */
    public SuperScene() {

    }
}
