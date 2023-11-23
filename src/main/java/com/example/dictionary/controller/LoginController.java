package com.example.dictionary.controller;

import com.example.dictionary.user.UserManager;
import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.stage.PrimaryWindow;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoginController extends SuperController {

    @FXML
    private Circle lUsernameImg;
    @FXML
    private Circle sUsernameImg;
    @FXML
    private Circle lPasswordImg;
    @FXML
    private Circle sPasswordImg;
    @FXML
    private Circle sPassword1Img;
    @FXML
    private Label signupLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private VBox signupView;
    @FXML
    private VBox loginView;
    @FXML
    private TextField lUsername;
    @FXML
    private Button lLoginBtn;
    @FXML
    private PasswordField lPassword;
    @FXML
    private Label bar;
    @FXML
    private Button sCreateBtn;
    @FXML
    private TextField sUsername;
    @FXML
    private PasswordField sPassword1;
    @FXML
    private PasswordField sPassword;

    /**
     * Handles the navigation to the login view by animating the transition between login and signup views.
     */
    private void handleNavLogin() {

        if (!loginView.isVisible()) {
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(bar);
            transition.setDuration(Duration.millis(500));
            transition.setToX(loginLabel.getLayoutX());
            loginLabel.setStyle("-fx-text-fill:#05386B;");
            signupLabel.setStyle("-fx-text-fill:black;");
            transition.play();
            signupView.setVisible(false);
            loginView.setVisible(true);
        }
    }

    /**
     * Handles the navigation to the signup view by animating the transition between login and signup views.
     */
    private void handleNavSignup() {
        if (!signupView.isVisible()) {
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(bar);
            transition.setDuration(Duration.millis(500));
            transition.setToX(signupLabel.getLayoutX());
            transition.play();
            loginLabel.setStyle("-fx-text-fill:black;");
            signupLabel.setStyle("-fx-text-fill:#05386B;");
            signupView.setVisible(true);
            loginView.setVisible(false);
        }
    }

    /**
     * Handles the login process by using the entered username and password fields to authenticate the user.
     */
    private void handleLogin() {
        boolean res = UserManager.getInstance().login(lUsername.getText(), lPassword.getText());
        if (!res) {
            new Alert(Alert.AlertType.WARNING, "Tên đăng nhập hoặc mật khẩu không chính xác").show();
        }
    }

    /**
     * Handles the creation of a new user account by verifying the entered username and password fields.
     */
    private void handleCreate() {
        if (sUsername.getText().equals("") || sPassword1.getText().equals("") || sPassword.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
            return;
        }

        if (sPassword1.getText().equals(sPassword.getText())) {
            if (!UserManager.getInstance().create(sUsername.getText(), sPassword.getText())) {
                new Alert(Alert.AlertType.WARNING, "Tên đăng nhập đã tồn tại").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Mật khẩu không khớp").show();
        }
    }

    /**
     * Initializes images for user and password icons used in the login and signup views.
     */
    private void initImages() {
        ImagePattern user = new ImagePattern(new Image(getClass().getResourceAsStream("user.png")));
        ImagePattern pw = new ImagePattern(new Image(getClass().getResourceAsStream("password.png")));
        lUsernameImg.setFill(user);
        sUsernameImg.setFill(user);
        lPasswordImg.setFill(pw);
        sPassword1Img.setFill(pw);
        sPasswordImg.setFill(pw);
    }

    /**
     * Initializes the components required for the login and signup views.
     */
    @Override
    protected void initComponents() {
        initImages();
    }

    /**
     * Initializes the event handlers for user interactions within the login and signup views.
     */
    @Override
    protected void initEvents() {
        loginLabel.setOnMouseClicked(event -> handleNavLogin());
        signupLabel.setOnMouseClicked(event -> handleNavSignup());
        lLoginBtn.setOnAction(event -> handleLogin());
        sCreateBtn.setOnAction(event -> handleCreate());
    }
}