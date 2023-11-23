package com.example.dictionary.controller;

import com.example.dictionary.game.Game1;
import com.example.dictionary.game.Game2;
import com.example.dictionary.game.GameInfo;
import com.example.dictionary.game.GameManager;
import com.example.dictionary.scene.SuperScene;
import com.example.dictionary.user.User;
import com.example.dictionary.user.UserManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Game2Controller {
    /**
     * Singleton instance of the Game2Controller class.
     */
    private static Game2Controller instance;

    /**
     * Retrieves the singleton instance of the Game2Controller.
     *
     * @return The singleton instance of the Game2Controller.
     */
    public static Game2Controller getInstance() {
        return instance;
    }

    public static final int NUMBER_OF_QUESTIONS = 10;
    private static final int MAX_PLAYER_SHOW = 10;
    private Game2 game = new Game2();
    private ArrayList<String> list;
    private final AtomicLong time = new AtomicLong(0);
    @FXML
    GridPane grid;
    @FXML
    Button newGameBtn;
    @FXML
    Button pauseBtn;
    @FXML
    Button ruleBtn;
    @FXML
    VBox gameContent;
    @FXML
    VBox rule;
    @FXML
    Label timeLabel;
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> timeLabel.setText(String.valueOf((double) time.incrementAndGet() / 10))));
    @FXML
    TableView<User> topPlayer;
    @FXML
    TableColumn<User, Integer> sttCol;
    @FXML
    TableColumn<User, String> userCol;
    @FXML
    TableColumn<User, Double> timeCol;
    private int solvedQuestion = 0;
    private boolean isPaused;
    private ToggleButton clickedWord;
    private String clickedText;


    /**
     * Initializes the game interface and sets up event handlers.
     */
    @FXML
    public void initialize() {
        instance = this;
        newGameBtn.setOnAction(event -> newGame());
        ruleBtn.setOnAction(event -> {
            gameContent.setVisible(false);
            rule.setVisible(true);
        });
        rule.setOnMouseClicked(event -> {
            gameContent.setVisible(true);
            rule.setVisible(false);
        });
        pauseBtn.setOnAction(event -> handlePauseBtn());
        timeline.setCycleCount(Animation.INDEFINITE);

        for (int i = 0; i < 2 * NUMBER_OF_QUESTIONS; i++) {
            ToggleButton btn = (ToggleButton) grid.getChildren().get(i);
            btn.setVisible(false);
            int finalI = i;
            btn.setOnMouseClicked(event -> {
                if (clickedWord != null) {
                    if (game.checkAnswer(clickedText, list.get(finalI))) {
                        clickedWord.setVisible(false);
                        btn.setVisible(false);
                        this.solvedQuestion++;
                        if (this.solvedQuestion == NUMBER_OF_QUESTIONS)
                            finishGame();
                    } else {
                        btn.setSelected(false);
                    }
                    clickedWord = null;
                } else {
                    clickedWord = btn;
                    clickedText = list.get(finalI);
                }
            });
        }
        sttCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(topPlayer.getItems().indexOf(collumn.getValue()) + 1));
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        timeCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(
                GameManager.getInstance().getBestTime(Game2.GAME_ID, collumn.getValue().getId())));
        updateBXH();
    }

    /**
     * Starts a new game, generating a list of words and initializing the game interface.
     */
    private void newGame() {
        pauseBtn.setVisible(true);
        list = game.generate();

        if (list.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Không đủ số lượng từ").show();
            return;
        }

        for (int i = 0; i < 2 * NUMBER_OF_QUESTIONS; i++) {
            ToggleButton btn = (ToggleButton) grid.getChildren().get(i);
            btn.setText(list.get(i));
            btn.setVisible(true);
        }
        isPaused = false;
        pauseBtn.setVisible(true);
        newGameBtn.setVisible(false);
        timeline.play();
    }

    /**
     * Toggles between pausing and resuming the game.
     */
    @FXML
    void handlePauseBtn() {
        isPaused = !isPaused;
        if (isPaused) timeline.pause();
        else timeline.play();
        pauseBtn.setText(isPaused ? "Tiếp tục" : "Tạm dừng");
        grid.setVisible(!isPaused);
    }

    /**
     * If all questions are solved, records the player's win in the game history.
     * Updates the leaderboard.
     */
    private void finishGame() {
        if (solvedQuestion == NUMBER_OF_QUESTIONS) {
            double playedTime = 1.0 * time.get() / 10;
            GameManager.getInstance().addToPlayersHistory(new GameInfo(Game2.GAME_ID, playedTime, GameInfo.Status.WIN));
        }

        clickedWord = null;
        solvedQuestion = 0;
        time.set(0);
        timeline.stop();
        newGameBtn.setVisible(true);
        pauseBtn.setVisible(false);
        for (int i = 0; i < 2 * NUMBER_OF_QUESTIONS; i++) {
            grid.getChildren().get(i).setVisible(false);
        }
        updateBXH();
    }

    /**
     * Updates the leaderboard with player information and their best times for Game2.
     */
    public void updateBXH() {
        ObservableList<User> players = GameManager.getInstance().getPlayersWon(Game2.GAME_ID);
        topPlayer.getItems().clear();
        topPlayer.setItems(FXCollections.observableArrayList(
                players.stream().filter(player -> players.indexOf(player) < MAX_PLAYER_SHOW).collect(Collectors.toList())));
    }
}
