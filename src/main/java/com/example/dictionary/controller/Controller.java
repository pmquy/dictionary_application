package com.example.dictionary.controller;

import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.stage.PrimaryWindow;

public class Controller {

    /**
     * Handles the change of user.
     */
    public static void handleChangeUser() {
        GameController.getInstance().handleUserChange();
        HomeController.getInstance().handleUserChange();
        TranslateController.getInstance().handleUserChange();
        UserController.getInstance().handleUserChange();
        ThemeController.getInstance().handleUserChange();
        PrimaryWindow.getInstance().changeScene(SceneEnum.HOME);
    }

    /**
     * Handles the change of image.
     */
    public static void handleChangeImage() {
        HomeController.getInstance().initUserImage();
        GameController.getInstance().initUserImage();
        TranslateController.getInstance().initUserImage();
        UserController.getInstance().initUserImage();
        ThemeController.getInstance().initUserImage();
    }

    /**
     * Handles the change of streak.
     */
    public static void handleChangeStreak() {
        HomeController.getInstance().initStreak();
        GameController.getInstance().initStreak();
        TranslateController.getInstance().initStreak();
        UserController.getInstance().initStreak();
        ThemeController.getInstance().initStreak();
    }

    /**
     * Handles the change of statistics.
     */
    public static void handleChangeStatics() {
        UserController.getInstance().initAchievements();
        HomeController.getInstance().initDailyTask();
    }
}