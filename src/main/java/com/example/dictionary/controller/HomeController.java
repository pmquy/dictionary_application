package com.example.dictionary.controller;

import com.example.dictionary.Application;
import com.example.dictionary.api.TextToSpeech;
import com.example.dictionary.utils.Utils;
import com.example.dictionary.word.Data;
import com.example.dictionary.word.Word;
import com.example.dictionary.scene.SceneEnum;
import com.example.dictionary.stage.PrimaryWindow;
import com.example.dictionary.stage.WindowEnum;
import com.example.dictionary.user.UserManager;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

enum DailyTask {
    TASK_FIND(0,"Tìm kiếm 10 lần", 5, 10, "img1.png"),
    TASK_PLAY(1,"Chơi game 10 lần", 10, 10, "img2.png"),
    TASK_ADD(2,"Thêm 10 từ", 10, 10, "img3.png");
    private final int index;
    private final String name;
    private final int coin;
    private final int max;
    private final Image image;

    /**
     * Handles the event associated with the task.
     * Depending on the task state, switches scenes or updates user information.
     */
    public void handleEvent() {
        switch (getState()) {
            case 0 -> {
                switch (this) {
                    case TASK_FIND -> PrimaryWindow.getInstance().changeScene(SceneEnum.TRANSLATE);
                    case TASK_PLAY -> PrimaryWindow.getInstance().changeScene(SceneEnum.GAME);
                    case TASK_ADD -> HomeController.getInstance().handleAddNewWord();
                }
            }
            case 1 -> {
                UserManager.getInstance().getCurrentUser().setCoin(UserManager.getInstance().getCurrentUser().getCoin() + coin);
                UserManager.getInstance().getCurrentUser().getTasksTrack().set(index, true);
            }
        }
    }

    /**
     * Gets the current state of the task.
     *
     * @return An integer representing the state of the task.
     */
    public int getState() {
        if(UserManager.getInstance().getCurrentUser().getTasksCount().get(index) < max) {
            return 0;
        }
        return UserManager.getInstance().getCurrentUser().getTasksTrack().get(index) ? 2 : 1;
    }


    /**
     * Gets the coin reward for completing the task.
     *
     * @return The coin reward for the task.
     */
    public int getCoin() {
        return coin;
    }

    /**
     * Gets the name of the task.
     *
     * @return The name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximum count required to complete the task.
     *
     * @return The maximum count required to complete the task.
     */
    public int getMax() {
        return max;
    }

    /**
     * Gets the image associated with the task.
     *
     * @return The image associated with the task.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the current count of the task.
     *
     * @return The current count of the task.
     */
    public int getCurrent() {
        return UserManager.getInstance().getCurrentUser().getTasksCount().get(index);
    }

    /**
     * Constructor for DailyTask enum elements.
     *
     * @param index Index of the task.
     * @param name Name of the task.
     * @param coin Coin reward for the task.
     * @param max Maximum count required to complete the task.
     * @param img Image file associated with the task.
     */
    DailyTask(int index, String name, int coin, int max, String img) {
        this.name = name;
        this.coin = coin;
        this.max = max;
        this.image = new Image(getClass().getResourceAsStream(img));
        this.index = index;
    }
}


public class HomeController extends MainController {

    @FXML
    private VBox taskContainer;
    @FXML
    private Label welcome;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button speakBtn;
    @FXML
    private ImageView speakImg;
    @FXML
    private WebView randomDefinitions;
    @FXML
    private Label leftBtn;
    @FXML
    private Label rightBtn;
    @FXML
    private Label randomWords;
    @FXML
    private Button addBtn;
    @FXML
    private Button showDictionaryBtn;
    @FXML
    private ListView<String> listView;
    @FXML
    private WebView definitionView;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button editBtn;
    @FXML
    private TextField wordToFind;
    @FXML
    private Label searchBtn;
    @FXML
    private VBox div3;
    @FXML
    TextField word;
    @FXML
    HTMLEditor definition;
    @FXML
    Button saveBtn;
    @FXML
    Button exitBtn;
    private static HomeController instance;

    public static final int WORDS_EVERY_DAY = 5;

    private ArrayList<Word> wordsEveryDay;

    public static HomeController getInstance() {
        return instance;
    }

    /**
     * Initializes the HomeController.
     */
    @FXML
    public void initialize() {
        super.initialize();
        instance = this;
    }

    /**
     * Handles the selection of a word.
     *
     * @param word The selected word.
     */
    private void handleSelectWord(String word) {
        if (word != null) {
            deleteBtn.setVisible(true);
            editBtn.setVisible(true);
            searchBtn.setVisible(true);
            definitionView.getEngine().loadContent(UserManager.getInstance().getCurrentUser().getWords().get(word).getDef());
            speakBtn.setVisible(true);
        } else {
            deleteBtn.setVisible(false);
            editBtn.setVisible(false);
            speakBtn.setVisible(false);
            searchBtn.setVisible(false);
            definitionView.getEngine().loadContent("");
        }
    }

    /**
     * Handles the search for a word.
     */
    private void handelSearchWord() {
        PrimaryWindow.getInstance().changeScene(SceneEnum.TRANSLATE);
        TranslateController.getInstance().find(listView.getSelectionModel().getSelectedItem());
    }

    /**
     * Handles the deletion of a word.
     */
    private void handleDeleteWord() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có muốn xóa từ này không?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserManager.getInstance().getCurrentUser().removeWord(listView.getSelectionModel().getSelectedItem());
            loadWordList();
        }
    }

    /**
     * Handles the editing of a word.
     */
    private void handleEditWord() {
        showEditor();
        String txt = listView.getSelectionModel().getSelectedItem();
        saveBtn.setText("Save");
        word.setText(txt);
        definition.setHtmlText(UserManager.getInstance().getCurrentUser().getWords().get(txt).getDef());
    }

    /**
     * Handles the addition of a new word.
     */
    public void handleAddNewWord() {
        showEditor();
        saveBtn.setText("Add");
        word.setText("");
        definition.setHtmlText("");
    }

    /**
     * Hides the word editor UI component.
     */
    private void hideEditor() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), div3);
        transition.setToY(-1000);
        transition.play();
    }

    /**
     * Shows the word editor UI component.
     */
    private void showEditor() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), div3);
        transition.setToY(1000);
        transition.play();
    }

    /**
     * Handles saving and editing word definitions, as well as UI interactions related to word management.
     */
    private void handleSaveEditWord() {
        String newWord = word.getText().trim();
        String newDefinition = definition.getHtmlText().replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        if (newWord.length() == 0) {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
            return;
        }
        hideEditor();
        if (saveBtn.getText().equals("Save"))
            UserManager.getInstance().getCurrentUser().removeWord(listView.getSelectionModel().getSelectedItem());
        UserManager.getInstance().getCurrentUser().addWord(new Word(newWord, newDefinition));
        loadWordList();
        listView.getSelectionModel().select(newWord);
    }

    /**
     * Initializes and configures event handlers for UI elements.
     */
    protected void initEvents() {
        super.initEvents();
        saveBtn.setOnAction(e -> handleSaveEditWord());
        exitBtn.setOnAction(e -> hideEditor());
        rightBtn.setOnMouseClicked(event -> loadRandomWords(++currentWord));
        leftBtn.setOnMouseClicked(event -> loadRandomWords(--currentWord));
        randomWords.setOnMouseClicked(e -> TextToSpeech.textToSpeech(randomWords.getText()));
        listView.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> handleSelectWord(c));
        searchBtn.setOnMouseClicked(event -> handelSearchWord());
        deleteBtn.setOnAction(event -> handleDeleteWord());
        editBtn.setOnAction(event -> handleEditWord());
        addBtn.setOnAction(event -> handleAddNewWord());
        wordToFind.textProperty().addListener((a, b, c) -> loadWordList());
        showDictionaryBtn.setOnAction(event -> Application.getInstance().showWindow(WindowEnum.DICTIONARY));
        speakBtn.setOnAction(e -> TextToSpeech.textToSpeech(listView.getSelectionModel().getSelectedItem()));
        datePicker.valueProperty().addListener((a, b, c) -> handleDateChange(c));
    }

    /**
     * Handles date change event.
     *
     * @param date The selected date from the date picker.
     */
    private void handleDateChange(LocalDate date) {
        if (date != null) {
            if (date.isAfter(LocalDate.now())) {
                new Alert(Alert.AlertType.WARNING, "Hãy chờ đến " + date).show();
                return;
            }
            wordsEveryDay = Data.getInstance().getRandomWordsByDay(WORDS_EVERY_DAY, date);
            currentWord = 0;
            loadRandomWords(0);
        }
    }

    /**
     * Loads the random words to display.
     */
    private void loadRandomWords(int i) {
        randomWords.setText(wordsEveryDay.get(i).getWord());
        randomDefinitions.getEngine().loadContent(wordsEveryDay.get(i).getDef());
        leftBtn.setVisible(i != 0);
        rightBtn.setVisible(i != WORDS_EVERY_DAY - 1);
    }

    private int currentWord = 0;

    /**
     * Initializes the components, sets up drag functionality, and loads initial data.
     */
    protected void initComponents() {
        super.initComponents();
        Utils.Drag(div3);
        datePicker.valueProperty().setValue(LocalDate.now());
        speakImg.setImage(new Image(getClass().getResourceAsStream("speaker.png")));
    }

    /**
     * Loads the word list.
     */
    public void loadWordList() {
        listView.getItems().clear();
        listView.getItems().addAll(UserManager.getInstance().getCurrentUser().getTrie().getAllWordsStartWith(wordToFind.getText()));
    }

    /**
     * Displays a welcome message for the current user with animation effects.
     */
    private void welcomeUser() {
        welcome.setLayoutY(-welcome.getHeight());
        welcome.setOpacity(0);
        welcome.setText("Xin chào " + UserManager.getInstance().getCurrentUser().getUsername());

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), welcome);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(2);

        TranslateTransition transition = new TranslateTransition(Duration.millis(2000), welcome);
        transition.setByY(30);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);

        transition.play();
        fadeTransition.play();
    }

    /**
     * Initializes the daily task containers and their respective UI components based on the defined daily tasks.
     */
    public void initDailyTask() {
        taskContainer.getChildren().clear();
        for(DailyTask key : DailyTask.values()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                Node node = fxmlLoader.load(getClass().getResourceAsStream("dailyTask.fxml"));
                ((Label) node.lookup("#name")).setText(key.getName());
                ((Label) node.lookup("#detail")).setText(key.getCurrent() + "/" + key.getMax());
                ((Label) node.lookup("#gem")).setText("x" + key.getCoin());
                ((ImageView) node.lookup("#gemImg")).setImage(new Image(getClass().getResourceAsStream("gem.png")));
                ((ImageView) node.lookup("#img")).setImage(key.getImage());
                ((ProgressBar) node.lookup("#bar")).setProgress(1.0 * key.getCurrent() / key.getMax());
                Label btn = (Label) node.lookup("#btn");

                switch (key.getState()) {
                    case 0 -> {
                        btn.getStyleClass().clear();
                        btn.setText("Thực hiện");
                        btn.getStyleClass().add("btn-2");
                        btn.setOnMouseClicked(e -> {
                            key.handleEvent();
                        });
                    }
                    case 1 -> {
                        btn.getStyleClass().clear();
                        btn.setText("Nhận ngay");
                        btn.getStyleClass().add("btn-2");
                        btn.setOnMouseClicked(e -> {
                            key.handleEvent();
                            btn.getStyleClass().clear();
                            btn.setText("Đã nhận");
                            btn.getStyleClass().add("btn-22");
                        });
                    }
                    case 2 -> {
                        btn.getStyleClass().clear();
                        btn.setText("Đã nhận");
                        btn.getStyleClass().add("btn-22");
                    }
                }
                taskContainer.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Handles changes in the user's session by performing necessary updates in the interface.
     */
    @Override
    protected void handleUserChange() {
        super.handleUserChange();
        loadWordList();
        welcomeUser();
        initDailyTask();
    }
}