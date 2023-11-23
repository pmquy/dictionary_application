package com.example.dictionary.controller;

import com.example.dictionary.Application;
import com.example.dictionary.achievement.AchievementEnum;
import com.example.dictionary.user.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class UserController extends MainController {
    @FXML
    private Circle chooseImg;
    @FXML
    private Label username;
    @FXML
    private Label gem;

    public static UserController getInstance() {
        return instance;
    }

    private static UserController instance;
    @FXML
    private VBox achievements;
    @FXML
    private Button removeUserBtn;
    @FXML
    private Circle userImg;
    @FXML
    private Button changePasswordBtn;
    @FXML
    private ImageView gemImg;
    @FXML
    private ImageView hintImg;
    @FXML
    private ImageView gemImg1;
    @FXML
    private ImageView hintImg1;
    @FXML
    private ImageView gemImg2;
    @FXML
    private ImageView hintImg2;
    @FXML
    private ImageView gemImg3;
    @FXML
    private ImageView hintImg3;
    @FXML
    private ImageView gemImg4;
    @FXML
    private ImageView hintImg4;
    @FXML
    private ImageView transferImg1;
    @FXML
    private ImageView transferImg2;
    @FXML
    private ImageView transferImg3;
    @FXML
    private ImageView transferImg4;
    @FXML
    private Circle pwImg1;
    @FXML
    private Circle pwImg2;
    @FXML
    private Circle pwImg3;
    @FXML
    private TextField pw1;
    @FXML
    private PasswordField pw2;
    @FXML
    private PasswordField pw3;

    @FXML
    private Button transferBtn1;
    @FXML
    private Button transferBtn2;
    @FXML
    private Button transferBtn3;
    @FXML
    private Button transferBtn4;

    @FXML
    private Label hint;
    @FXML
    private ImageView binImg;
    @FXML
    private Label streak1;
    @FXML
    private ImageView streakImg1;

    /**
     * Initializes various image components in the user interface.
     */
    @Override
    protected void initComponents() {
        super.initComponents();
        Image img1 = new Image(getClass().getResourceAsStream("gem.png"));
        gemImg.setImage(img1);
        gemImg1.setImage(img1);
        gemImg2.setImage(img1);
        gemImg3.setImage(img1);
        gemImg4.setImage(img1);

        img1 = new Image(getClass().getResourceAsStream("hint.png"));
        hintImg.setImage(img1);
        hintImg1.setImage(img1);
        hintImg2.setImage(img1);
        hintImg3.setImage(img1);
        hintImg4.setImage(img1);

        img1 = new Image(getClass().getResourceAsStream("transfer.png"));
        transferImg1.setImage(img1);
        transferImg2.setImage(img1);
        transferImg3.setImage(img1);
        transferImg4.setImage(img1);

        ImagePattern imagePattern = new ImagePattern(new Image(getClass().getResourceAsStream("password.png")));
        pwImg1.setFill(imagePattern);
        pwImg2.setFill(imagePattern);
        pwImg3.setFill(imagePattern);

        chooseImg.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("pen.png"))));
        streakImg1.setImage(new Image(getClass().getResourceAsStream("streak.png")));
        binImg.setImage(new Image(getClass().getResourceAsStream("bin.png")));
    }

    /**
     * Handles the process of changing the user's password by verifying the input passwords
     * and updating the user's password if the verification is successful.
     */
    private void handleChangePassword() {
        if (pw1.getText().equals("") || pw2.getText().equals("") || pw3.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
            return;
        }
        if (!UserManager.getInstance().getCurrentUser().getPassword().equals(pw1.getText())) {
            new Alert(Alert.AlertType.WARNING, "Mật khẩu không chính xác").show();
            return;
        }
        if (!pw3.getText().equals(pw2.getText())) {
            new Alert(Alert.AlertType.WARNING, "Mật khẩu không khớp").show();
            return;
        }
        UserManager.getInstance().getCurrentUser().setPassword(pw2.getText());
        new Alert(Alert.AlertType.INFORMATION, "Đổi mật khẩu thành công").show();
    }

    /**
     * Initializes the event handling.
     */
    @Override
    protected void initEvents() {
        super.initEvents();
        changePasswordBtn.setOnAction(e -> handleChangePassword());
        chooseImg.setOnMouseClicked(e -> handleChooseImg());
        removeUserBtn.setOnAction(event -> handleRemoveUser());
        transferBtn1.setOnAction(e -> handleTransfer(20, 1));
        transferBtn2.setOnAction(e -> handleTransfer(100, 10));
        transferBtn3.setOnAction(e -> handleTransfer(200, 30));
        transferBtn4.setOnAction(e -> handleTransfer(1000, 200));
    }

    /**
     * Handles the transfer of coins and hints between the user's resources based on specified values.
     * Updates the user's coin and hint counts after successful transfer and displays a confirmation message.
     *
     * @param coin The amount of coins to transfer.
     * @param hint The amount of hints to receive.
     */
    public void handleTransfer(int coin, int hint) {
        int c = UserManager.getInstance().getCurrentUser().getCoin();
        int h = UserManager.getInstance().getCurrentUser().getHint();
        if (c < coin) {
            new Alert(Alert.AlertType.WARNING, "Hết tiền rồi :((").show();
            return;
        }
        UserManager.getInstance().getCurrentUser().setCoin(c - coin);
        UserManager.getInstance().getCurrentUser().setHint(h + hint);
        new Alert(Alert.AlertType.INFORMATION, "Đã đổi thành công").show();
    }

    /**
     * Initializes the controller when the view is loaded by setting the instance to this instance.
     */
    @Override
    public void initialize() {
        super.initialize();
        instance = this;
    }

    /**
     * Handles the display of the user menu.
     */
    public void handleClickMenu() {
        if (removeUserBtn.isVisible()) {
            removeUserBtn.setVisible(false);
            changePasswordBtn.setVisible(false);
        } else {
            removeUserBtn.setVisible(true);
            changePasswordBtn.setVisible(true);
        }
    }

    /**
     * Initializes the user achievements in the UI.
     */
    public void initAchievements() {
        achievements.getChildren().clear();
        for (AchievementEnum value : AchievementEnum.values()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                HBox hBox = fxmlLoader.load(getClass().getResourceAsStream("achievement.fxml"));
                ((ImageView) hBox.lookup("#img")).setImage(value.getImage());
                ((Label) hBox.lookup("#name")).setText(value.getName());
                ((Label) hBox.lookup("#description")).setText(value.getDescription());
                ((Label) hBox.lookup("#detail")).setText(value.getCurrent() + "/" + value.getMax());
                ((ProgressBar) hBox.lookup("#bar")).setProgress(1.0 * value.getCurrent() / value.getMax());
                achievements.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes the user's profile image.
     */
    @Override
    protected void initUserImage() {
        super.initUserImage();
        ImagePattern imagePattern = new ImagePattern(UserManager.getInstance().getCurrentUser().getImage());
        userImg.setFill(imagePattern);
    }

    /**
     * Initializes the user's details in the UI.
     */
    public void initUserDetail() {
        gem.setText("x" + UserManager.getInstance().getCurrentUser().getCoin());
        hint.setText("x" + UserManager.getInstance().getCurrentUser().getHint());
        username.setText(UserManager.getInstance().getCurrentUser().getUsername());
    }


    /**
     * Handles changes related to the user's account.
     */
    @Override
    public void handleUserChange() {
        super.handleUserChange();
        initUserDetail();
        initAchievements();

        ArrayList<LocalDate> list = new ArrayList<>(UserManager.getInstance().getCurrentUser().getLoginDays());
        Collections.sort(list);
        int count = 1;
        int i = list.size() - 2;
        while (i >= 0 && list.get(i).equals(list.get(i + 1).minusDays(1))) {
            count++;
            i--;
        }
        streak1.setText("x" + count);
    }

    /**
     * Handles the process of removing the current user's account confirmed and performs a logout from the application.
     */
    private void handleRemoveUser() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có muốn xóa tài khoản này không?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserManager.getInstance().remove();
            Application.getInstance().handleLogOut();
        }
    }

    /**
     * Handles the selection of a new image for the user's profile.
     * Allows the user to select an image file (in JPG format) to set as their profile picture.
     */
    private void handleChooseImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null)
            UserManager.getInstance().getCurrentUser().setImage(file.getPath());
    }
}