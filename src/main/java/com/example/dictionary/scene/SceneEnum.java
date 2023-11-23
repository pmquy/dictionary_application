package com.example.dictionary.scene;

public enum SceneEnum {
    HOME("home-view.fxml"),
    GAME("game-view.fxml"),
    GAME_1("game1-view.fxml"),
    TRANSLATE("translate-view.fxml"),
    GAME_2("game2-view.fxml"),
    GAME_3("game3-view.fxml"),
    DICTIONARY("dictionary-view.fxml"),
    USER("user-view.fxml"),
    LOGIN("login-view.fxml"),
    THEME("theme.fxml");
    private final String value;
    /**
     * Retrieves the value associated with the scene.
     *
     * @return The value of the scene.
     */
    public String getValue() {
        return value;
    }
    /**
     * Constructs a SceneEnum with a specific value.
     *
     * @param v The value associated with the scene.
     */
    SceneEnum(String v) {
        value = v;
    }
}
