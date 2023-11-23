package com.example.dictionary.controller;

import com.example.dictionary.Application;
import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.stage.PrimaryWindow;
import com.example.dictionary.user.UserManager;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class MainController extends SuperController{
    @FXML
    protected Circle userNav;
    @FXML
    protected Label translateNav;
    @FXML
    protected HBox dailyContainer;
    @FXML
    protected ImageView streakImg;
    @FXML
    protected Label gameNav;
    @FXML
    protected Label homeNav;
    @FXML
    protected Label nav1;
    @FXML
    protected Label nav2;
    @FXML
    protected Label nav3;
    @FXML
    protected VBox div2;
    @FXML
    protected ImageView nav1Img;
    @FXML
    protected ImageView nav2Img;
    @FXML
    protected ImageView nav3Img;

    /**
     * Initializes the user image displayed in the navigation bar.
     */
    protected void initUserImage() {
        ImagePattern imagePattern = new ImagePattern(UserManager.getInstance().getCurrentUser().getImage());
        userNav.setFill(imagePattern);
    }


    /**
     * Initializes the streak feature for tracking user login days and bonuses.
     * Generates a streak indicating the consecutive login days.
     * Sets the visual representation of daily login streaks, including earned bonuses and interactions for claiming bonuses.
     */
    protected void initStreak() {
        ArrayList<LocalDate> list = new ArrayList<>(UserManager.getInstance().getCurrentUser().getLoginDays());
        Collections.sort(list);
        int count = 1;
        int i = list.size() - 2;
        while (i >= 0 && list.get(i).equals(list.get(i + 1).minusDays(1))) {
            count++;
            i--;
        }
        Tooltip tooltip = new Tooltip("Chuỗi đăng nhập " + count);
        tooltip.setShowDelay(Duration.millis(200));
        Tooltip.install(streakImg, tooltip);
        count %= 7 + 1;
        dailyContainer.getChildren().clear();
        for (i = 1; i <= 7; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                Node node = fxmlLoader.load(getClass().getResourceAsStream("daily-1.fxml"));
                Label a = (Label) node.lookup("#day");
                a.setText("Ngày " + i);
                Label b = (Label) node.lookup("#coin");
                b.setText(String.valueOf(5 * i));

                if (i < count || (i == count && UserManager.getInstance().getCurrentUser().isReceiveCoin())) {
                    a.getStyleClass().add("btn-11");
                    b.getStyleClass().add("bg-white");
                } else {
                    a.getStyleClass().add("btn-22");
                    b.getStyleClass().add("bg-blue");
                }

                if (i == count && !UserManager.getInstance().getCurrentUser().isReceiveCoin()) {
                    Node c = node.lookup("#parent");
                    TranslateTransition transition = new TranslateTransition(Duration.millis(500), c);
                    transition.setByY(-10);
                    transition.setCycleCount(Animation.INDEFINITE);
                    transition.setAutoReverse(true);
                    transition.play();

                    ((Label) node.lookup("#t")).setText("x" + 5 * i);
                    ((ImageView) node.lookup("#img")).setImage(new Image(getClass().getResourceAsStream("gem.png")));

                    int finalI = i;
                    c.setOnMouseClicked(event -> {
                        transition.stop();
                        a.getStyleClass().remove("btn-22");
                        b.getStyleClass().remove("bg-blue");
                        a.getStyleClass().add("btn-11");
                        b.getStyleClass().add("bg-white");

                        TranslateTransition transition1 = new TranslateTransition(Duration.millis(1000), node.lookup("#temp"));
                        transition1.setCycleCount(2);
                        transition1.setByY(-60);
                        transition1.setAutoReverse(true);
                        transition1.play();
                        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), c);
                        rotateTransition.setAxis(Rotate.Y_AXIS);
                        rotateTransition.setToAngle(360);
                        rotateTransition.setCycleCount(2);
                        rotateTransition.play();

                        rotateTransition.setOnFinished(e -> {
                            c.setTranslateY(0);
                            UserManager.getInstance().getCurrentUser().setReceiveCoin();
                            UserManager.getInstance().getCurrentUser().setCoin(UserManager.getInstance().getCurrentUser().getCoin() + 5 * finalI);
                        });
                    });
                }

                dailyContainer.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Initializes the component settings for various navigation icons and visibility states.
     * Sets images for different navigation icons.
     */
    @Override
    protected void initComponents() {
        div2.setVisible(false);
        nav1Img.setImage(new Image(getClass().getResourceAsStream("user.png")));
        nav2Img.setImage(new Image(getClass().getResourceAsStream("setting.png")));
        nav3Img.setImage(new Image(getClass().getResourceAsStream("logout.png")));
        streakImg.setImage(new Image(getClass().getResourceAsStream("streak.png")));
    }

    /**
     * Handles the user's click action on the user navigation icon, toggling the visibility of a specific section (div2).
     */
    private void handleClickUserNav() {
        div2.setVisible(!div2.isVisible());
    }

    /**
     * Initializes the event handlers related to navigation and scene changes.
     * Configures actions upon clicking different navigation icons to switch scenes or perform specific actions.
     */
    @Override
    protected void initEvents() {
        streakImg.setOnMouseClicked(e -> dailyContainer.setVisible(!dailyContainer.isVisible()));
        translateNav.setOnMouseClicked(e -> PrimaryWindow.getInstance().changeScene(SceneEnum.TRANSLATE));
        gameNav.setOnMouseClicked(e -> PrimaryWindow.getInstance().changeScene(SceneEnum.GAME));
        userNav.setOnMouseClicked(e -> handleClickUserNav());
        nav1.setOnMouseClicked(e -> {
            PrimaryWindow.getInstance().changeScene(SceneEnum.USER);
            div2.setVisible(false);
        });
        homeNav.setOnMouseClicked(e -> PrimaryWindow.getInstance().changeScene(SceneEnum.HOME));

        nav2.setOnMouseClicked(e -> {
            PrimaryWindow.getInstance().changeScene(SceneEnum.THEME);
            div2.setVisible(false);
        });

        nav3.setOnMouseClicked(e -> {
            Application.getInstance().handleLogOut();
            div2.setVisible(false);
        });
    }

    /**
     * Handles changes related to the user, updates for the user image and login streak upon user modifications.
     */
    protected void handleUserChange() {
        initUserImage();
        initStreak();
    }
}
