package com.example.dictionary.controller;

import com.example.dictionary.Application;
import com.example.dictionary.game.Game1;
import com.example.dictionary.game.GameInfo;
import com.example.dictionary.game.GameManager;
import com.example.dictionary.scene.SuperScene;
import com.example.dictionary.user.User;
import com.example.dictionary.user.UserManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Game1Controller {
    /**
     * Singleton instance of the Game1Controller class.
     */
    private final Game1 game1 = new Game1();

    /**
     * Retrieves the singleton instance of the Game1Controller.
     *
     * @return The singleton instance of the Game1Controller.
     */
    public static Game1Controller getInstance() {
        return instance;
    }

    private static Game1Controller instance;
    private final String BUTTON_COLOR = "#05386B";
    private final int MAX_PLAYER_SHOW = 5;

    @FXML
    Label quesLabel;
    @FXML
    ButtonBar ansSelections;
    @FXML
    Button skipBtn;
    @FXML
    Button checkBtn;
    @FXML
    Button newGameBtn;
    @FXML
    Button hintBtn;
    @FXML
    Button ruleBtn;
    @FXML
    ProgressBar progressBar;
    @FXML
    Label solved;
    @FXML
    Label fault;
    @FXML
    Label timeLabel;
    @FXML
    VBox rule;
    @FXML
    VBox gameContent;
    @FXML
    TableView<User> topPlayer;
    @FXML
    TableColumn<User, Integer> sttCol;
    @FXML
    TableColumn<User, String> userCol;
    @FXML
    TableColumn<User, Double> timeCol;
    @FXML
    ImageView hintImg;
    private int cntHint = 0;
    private final AtomicLong time = new AtomicLong(0);
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
            event -> timeLabel.setText(String.valueOf(1.0 * time.incrementAndGet() / 10))));

    /**
     * Initializes the Game1 view and sets up event handlers.
     */
    @FXML
    public void initialize() {
        instance = this;
        this.initComponents();
    }

    /**
     * Initializes the components and sets up event handlers for the Game1 view.
     */
    private void initComponents() {
        hintImg.setImage(new Image(getClass().getResourceAsStream("hint.png")));

        newGameBtn.setVisible(true);
        hintBtn.setVisible(false);
        solved.setVisible(false);
        fault.setVisible(false);
        checkBtn.setVisible(false);
        quesLabel.setVisible(false);
        progressBar.setVisible(false);
        timeline.setCycleCount(Animation.INDEFINITE);

        newGameBtn.setOnAction(actionEvent -> newGame());
        hintBtn.setOnAction(actionEvent -> hint());
        ruleBtn.setOnAction(actionEvent -> {
            rule.setVisible(true);
            gameContent.setVisible(false);
        });
        rule.setOnMouseClicked(mouseEvent -> {
            rule.setVisible(false);
            gameContent.setVisible(true);
        });
        skipBtn.setOnAction(actionEvent -> {
            game1.toNextQuestion();
            updateQuestion();
        });
        checkBtn.setText("Kiểm tra");
        checkBtn.setOnAction(actionEvent -> checkAns());
        sttCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(topPlayer.getItems().indexOf(collumn.getValue()) + 1));
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        timeCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(
                GameManager.getInstance().getBestTime(Game1.GAME_ID, collumn.getValue().getId())));
        updateBXH();
    }

    /**
     * Starts a new game session, initializing game elements.
     * Displays necessary UI elements, and begins the game.
     * If there are not enough words available for the game, it displays a warning.
     */
    private void newGame() {
        game1.init();
        cntHint = 0;
        progressBar.setProgress(0);
        if (!game1.isReady()) {
            new Alert(Alert.AlertType.WARNING, "Không đủ số lượng từ").show();
            return;
        }
        hintBtn.setText("x " + UserManager.getInstance().getCurrentUser().getHint());
        newGameBtn.setVisible(false);
        hintBtn.setVisible(true);
        updateQuestion();
        updateStatus();
        checkBtn.setVisible(true);
        skipBtn.setVisible(true);
        quesLabel.setVisible(true);
        timeLabel.setVisible(true);
        progressBar.setVisible(true);
        timeline.play();
    }

    /**
     * Stops the timeline, and updates game-related attributes.
     * Determines the game result based on remaining questions, records game history, and updates UI visibility accordingly.
     */
    private void finish() {
        timeline.stop();
        double playedTime = 1.0 * time.get() / 10;

        if(game1.questionRemain() == 0) {
            GameManager.getInstance().addToPlayersHistory(new GameInfo(Game1.GAME_ID, playedTime, GameInfo.Status.WIN));


        } else {
            GameManager.getInstance().addToPlayersHistory(new GameInfo(Game1.GAME_ID, playedTime, GameInfo.Status.LOSE));
        }
        newGameBtn.setVisible(true);
        solved.setVisible(false);
        fault.setVisible(false);
        checkBtn.setVisible(false);
        quesLabel.setVisible(false);
        hintBtn.setVisible(false);
        time.set(0);
        checkBtn.setOnAction(actionEvent -> checkAns());
        ansSelections.getButtons().forEach(btn -> btn.setVisible(false));
        updateBXH();
    }

    /**
     * Updates the UI with the current question and available answer options for the game.
     */
    private void updateQuestion() {
        skipBtn.setVisible(game1.questionRemain() > 1);
        checkBtn.setDisable(false);
        quesLabel.setText(game1.getQuestion());
        ansSelections.getButtons().clear();
        for (int i = 0; i < 3; i++) {
            ToggleButton btn = new ToggleButton(game1.getSelections().get(i));
            btn.setDisable(false);
            btn.setVisible(true);
            btn.setSelected(false);
            final int finalI = i;
            btn.setOnAction(actionEvent -> {
                ToggleButton selectedAns = getSelectedAns();
                if (selectedAns != null) getSelectedAns().setSelected(false);
                game1.selectAns(finalI);
            });
            ansSelections.getButtons().add(btn);
        }
    }

    /**
     * Updates the status indicators and progress bar in the game UI.
     */
    private void updateStatus() {
        progressBar.setVisible(true);
        progressBar.setProgress(game1.getProgress());
        solved.setVisible(true);
        fault.setVisible(true);
        solved.setText(String.format("Solved: %02d / %02d", game1.getSolved(), Game1.NUM_QUESTION));
        fault.setText(String.format("Fault: %02d / %02d", game1.getFault(), Game1.MAX_FAULT));
    }

    /**
     * Checks the user's answer in the game and updates the UI accordingly.
     */
    private void checkAns() {
        ansSelections.getButtons().get(game1.getAnswerIndex()).setStyle("-fx-background-color: lawngreen; -fx-text-fill: black");
        ansSelections.getButtons().forEach(btn -> {
            btn.setDisable(true);
            btn.setOpacity(1);
        });
        game1.submit();
        updateStatus();
        skipBtn.setVisible(false);
        hintBtn.setVisible(false);
        checkBtn.setText("Tiếp tục");
        if (game1.checkFinish()) {
            checkBtn.setOnAction(actionEvent -> finish());
        } else {
            checkBtn.setOnAction(actionEvent -> {
                game1.toNextQuestion();
                checkBtn.setText("Kiểm tra");
                checkBtn.setOnAction(actionEvent1 -> checkAns());
                hintBtn.setVisible(true);
                updateQuestion();
            });
        }
    }

    /**
     * Handles the logic behind providing hints during the game.
     */
    private void hint() {
        ArrayList<Integer> slt = new ArrayList<>(Arrays.asList(0, 1, 2));
        slt.removeIf(i ->
                !ansSelections.getButtons().get(i).isVisible()
                        || i == game1.getAnswerIndex());
        if (slt.isEmpty() || cntHint == Game1.MAX_HINT) {
            new Alert(Alert.AlertType.WARNING, "Không thể dùng ").show();
        } else if (UserManager.getInstance().getCurrentUser().getHint() == 0) {
            new Alert(Alert.AlertType.WARNING, "Hết rồi :((. Nạp tiền đi").show();
        } else {
            // giam hint
            UserManager.getInstance().getCurrentUser().setHint(UserManager.getInstance().getCurrentUser().getHint() - 1);
            hintBtn.setText("x " + UserManager.getInstance().getCurrentUser().getHint());
            Random rd = new Random();
            int i = slt.get(rd.nextInt(slt.size()));
            ansSelections.getButtons().get(i).setVisible(false);
            cntHint++;
        }
    }

    /**
     * Updates the leaderboard with the top players for Game1.
     */
    public void updateBXH() {
        ObservableList<User> players = GameManager.getInstance().getPlayersWon(Game1.GAME_ID);
        topPlayer.getItems().clear();
        topPlayer.setItems(FXCollections.observableArrayList(
                players.stream().filter(player -> players.indexOf(player) < MAX_PLAYER_SHOW).collect(Collectors.toList())));
    }

    /**
     * Retrieves the currently selected answer button.
     *
     * @return The ToggleButton representing the currently selected answer, or null if no answer is selected.
     */
    private ToggleButton getSelectedAns() {
        if (game1.getSelectedAns() >= 0) return (ToggleButton) ansSelections.getButtons().get(game1.getSelectedAns());
        else return null;
    }
}
