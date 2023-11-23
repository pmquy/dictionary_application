package com.example.dictionary.stage;

import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.scene.SuperScene;
import javafx.stage.Stage;

public class DictionaryWindow extends Window {
    /**
     * Constructs a DictionaryWindow object and initializes it with the Dictionary scene.
     */
    public DictionaryWindow() {
        super("Offline Dictionary");
        changeScene(SceneEnum.DICTIONARY);
    }

    /**
     * Initializes the scenes associated with the DictionaryWindow.
     */
    @Override
    protected void initScenes() {
        scenes.put(SceneEnum.DICTIONARY, new SuperScene(SceneEnum.DICTIONARY));
    }
}
