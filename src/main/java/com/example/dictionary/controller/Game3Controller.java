package com.example.dictionary.controller;

import com.example.dictionary.game.Game1;
import com.example.dictionary.game.Game3;
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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Game3Controller {
    @FXML
    Label inform;
    @FXML
    Label meaning;
    @FXML
    HBox guessWord;
    @FXML
    HBox input;
    @FXML
    Button newGame;
    @FXML
    Button hintBtn;
    @FXML
    Button nextBtn;
    @FXML
    Button pauseBtn;
    @FXML
    Button ruleBtn;
    @FXML
    VBox gameContent;
    @FXML
    VBox rule;
    @FXML
    ProgressBar bar;
    @FXML
    Label timeLabel;
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

    private boolean isPaused = false;

    private final AtomicLong time = new AtomicLong(0);
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
            event -> timeLabel.setText(String.valueOf((double) time.incrementAndGet() / 10))));

    /**
     * Initializes the Game3 interface with event handlers, visibility configurations, and leaderboard updates.
     */
    @FXML
    void initialize() {
        instance = this;
        timeline.setCycleCount(Animation.INDEFINITE);
        meaning.setVisible(false);
        hintImg.setImage(new Image(UserController.getInstance().getClass().getResourceAsStream("hint.png")));
        hintBtn.setVisible(false);
        newGame.setOnAction(event -> newGame());
        hintBtn.setOnAction(event -> hint());
        nextBtn.setOnAction(event -> nextQuestion());
        ruleBtn.setOnAction(event -> {
            gameContent.setVisible(false);
            rule.setVisible(true);
        });
        rule.setOnMouseClicked(event -> {
            gameContent.setVisible(true);
            rule.setVisible(false);
        });
        pauseBtn.setOnAction(actionEvent -> {
            isPaused = !isPaused;
            pauseBtn.setText(isPaused ? "Tiếp tục" : "Dừng");
            guessWord.setVisible(!isPaused);
            input.setVisible(!isPaused);
            nextBtn.setVisible(!isPaused);
            hintBtn.setVisible(!isPaused);
            meaning.setVisible(!isPaused);
            if (isPaused) timeline.pause();
            else timeline.play();
        });
        sttCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(topPlayer.getItems().indexOf(collumn.getValue()) + 1));
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        timeCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(
                GameManager.getInstance().getBestTime(Game3.GAME_ID, collumn.getValue().getId())));
        updateBXH();
    }

    private final Game3 game = new Game3();

    /**
     * Initiates a new game in the Game3 interface.
     */
    private void newGame() {
        game.newGame();
        if (!game.isReady()) {
            new Alert(Alert.AlertType.WARNING, "Không đủ số lượng từ").show();
            return;
        }
        newGame.setVisible(false);
        meaning.setVisible(true);
        timeline.play();
        timeLabel.setVisible(true);
        nextQuestion();
        bar.setProgress(0);
        isPaused = false;
        pauseBtn.setVisible(true);
        pauseBtn.setText("Dừng");
        nextBtn.setVisible(true);
        hintBtn.setVisible(true);
        hintBtn.setText("x " + UserManager.getInstance().getCurrentUser().getHint());
        cntHint = 0;
    }

    /**
     * Handles the completion of the game in the Game3 interface.
     * Update the leaderboard.
     */
    private void finish() {
        newGame.setVisible(true);
        pauseBtn.setVisible(false);
        nextBtn.setVisible(false);
        timeline.pause();
        double playedTime = 1.0 * time.get() / 10;
        GameManager.getInstance().addToPlayersHistory(new GameInfo(Game3.GAME_ID, playedTime, GameInfo.Status.WIN));
        time.set(0);
        updateBXH();
    }

    /**
     * Handles the display of the next question in the Game3 interface.
     */
    private void nextQuestion() {
        game.nextQuestion();
        meaning.setText(game.getMeaning());
        inform.setText("");
        guessWord.getChildren().clear();
        input.getChildren().clear();
        List<String> list = Arrays.asList(game.getGuessWord().split(""));
        Collections.shuffle(list);

        for (String s : list) {
            Button btn = new Button();
            btn.setText(s);
            btn.setOnAction(event -> {
                btn.setVisible(false);
                Button temp = new Button();
                temp.setText(btn.getText());
                temp.setOnAction(event1 -> {
                    btn.setVisible(true);
                    guessWord.getChildren().remove(temp);
                });
                guessWord.getChildren().add(temp);

                if (guessWord.getChildren().size() == game.getGuessWord().length()) {
                    StringBuilder playerGuess = new StringBuilder();
                    guessWord.getChildren().stream().map(button -> ((Button) button).getText()).
                            reduce(playerGuess, (StringBuilder::append), StringBuilder::append);
                    if (game.isGuessedWord(playerGuess.toString())) {
                        game.increaseSolvedQuestion();
                        bar.setProgress(game.getProgress());
                        if (game.isFinished())
                            finish();
                        else {
                            nextQuestion();
                        }
                    } else {
                        inform.setText("Wrong");
                    }
                }
            });
            input.getChildren().add(btn);
        }
    }

    /**
     * Updates the leaderboard with player information and their best times for Game3.
     */
    public void updateBXH() {
        ObservableList<User> players = GameManager.getInstance().getPlayersWon(Game3.GAME_ID);
        topPlayer.getItems().clear();
        topPlayer.setItems(FXCollections.observableArrayList(
                players.stream().filter(player -> players.indexOf(player) < MAX_PLAYER_SHOW).collect(Collectors.toList())));
    }

    /**
     * Provides a hint to the user based on their current progress in the game.
     * Checks various conditions and provides hints accordingly.
     */
    private void hint() {
        StringBuilder playerGuess = new StringBuilder();
        guessWord.getChildren().stream().map(button -> ((Button) button).getText()).
                reduce(playerGuess, (StringBuilder::append), StringBuilder::append);
        if (game.isGuessedWord(playerGuess.toString())) {
            new Alert(Alert.AlertType.WARNING, "Đáp án đã đúng, không cần dùng :))").show();
        } else if (UserManager.getInstance().getCurrentUser().getHint() <= 0) {
            new Alert(Alert.AlertType.WARNING, "Hết tiền rồi :((").show();
        } else if (cntHint == Game3.MAX_HINT) {
            new Alert(Alert.AlertType.WARNING, "Đã dùng hết lượt").show();
        } else if (game.getGuessWord().startsWith(playerGuess.toString())) {
            input.getChildren().stream().filter(Node::isVisible).map(btn -> (Button) btn).filter(btn -> {
                playerGuess.append(btn.getText());
                if (game.getGuessWord().startsWith(playerGuess.toString())) {
                    btn.setStyle("-fx-background-color: #66FF66");
                    UserManager.getInstance().getCurrentUser().setHint(UserManager.getInstance().getCurrentUser().getHint() - 1);
                    hintBtn.setText("x " + UserManager.getInstance().getCurrentUser().getHint());
                    return true;
                }
                playerGuess.deleteCharAt(playerGuess.length() - 1);
                return false;
            }).findFirst();
        } else {
            new Alert(Alert.AlertType.WARNING, "Không thể tính toán").show();
        }
    }

    private static final int MAX_PLAYER_SHOW = 10;

    /**
     * Singleton instance of the Game3Controller class.
     */
    private static Game3Controller instance;

    /**
     * Retrieves the singleton instance of the Game3Controller.
     *
     * @return The singleton instance of the Game3Controller.
     */
    public static Game3Controller getInstance() {
        return instance;
    }

    public static void setInstance(Game3Controller instance) {
        Game3Controller.instance = instance;
    }
}