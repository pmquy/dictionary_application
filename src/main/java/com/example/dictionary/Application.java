package com.example.dictionary;

import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.user.UserManager;
import com.example.dictionary.stage.*;
import javafx.stage.Stage;
import java.util.HashMap;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    public void changeTheme(int theme) {
        windows.forEach((k, v) -> v.changeTheme(theme));
    }

    private final HashMap<WindowEnum, Window> windows = new HashMap<>();
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    public void showWindow(WindowEnum type) {
        windows.get(type).show();
    }

    public void hideWindow(WindowEnum type) {
        windows.get(type).hide();
    }

    public void handleLogOut() {
        windows.forEach((k, v) -> v.hide());
        windows.get(WindowEnum.PRIMARY).show();
        PrimaryWindow.getInstance().changeScene(SceneEnum.LOGIN);
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        windows.put(WindowEnum.PRIMARY, new PrimaryWindow(stage));
        windows.put(WindowEnum.DICTIONARY, new DictionaryWindow());
        windows.put(WindowEnum.GAME_2, new Game2Window());
        windows.put(WindowEnum.GAME_3, new Game3Window());
        windows.put(WindowEnum.GAME_1, new Game1Window());

        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            UserManager.getInstance().writeData();
            System.exit(0);
        });
    }
}

