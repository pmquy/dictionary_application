package com.example.dictionary.controller;

import com.example.dictionary.api.TextToSpeech;
import com.example.dictionary.api.Translate;
import com.example.dictionary.user.UserManager;
import com.example.dictionary.utils.Utils;
import com.example.dictionary.word.Word;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import netscape.javascript.JSObject;

import java.util.ArrayList;


public class TranslateController extends MainController {
    private static final Connector connector = new Connector();
    @FXML
    private TextField word;
    @FXML
    private HTMLEditor definition;
    @FXML
    private Button saveBtn;
    @FXML
    private VBox div3;
    @FXML
    private Button editBtn;
    @FXML
    private ListView<String> suggestions;
    @FXML
    private Button translateBtn;
    @FXML
    private Button addBtn;
    @FXML
    private TextField wordToTranslate;
    @FXML
    private WebView meaningView;
    @FXML
    private WebView detail;
    @FXML
    private Button speakBtn;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private ImageView speakImg;
    @FXML
    private ImageView loadingImg;

    /**
     * Finds and sets the word for translation, triggering translation if a valid word is provided.
     *
     * @param word The word to be translated.
     */
    public void find(String word) {
        if (word != null && word.trim().length() > 0) {
            wordToTranslate.setText(word);
            translateBtn.fire();
        }
    }

    /**
     * Retrieves the instance of the TranslateController.
     *
     * @return The TranslateController instance.
     */
    public static TranslateController getInstance() {
        return instance;
    }

    private static TranslateController instance;

    private static final ArrayList<String> langs = new ArrayList<>();

    static {
        langs.add("ANH-VIỆT");
        langs.add("VIỆT-ANH");
    }

    private String meaning = "";

    /**
     * Initializes the components and settings for translation.
     */
    @Override
    protected void initComponents() {
        super.initComponents();
        Utils.Drag(div3);
        speakImg.setImage(new Image(getClass().getResourceAsStream("speaker.png")));
        loadingImg.setImage(new Image(getClass().getResourceAsStream("loading.png")));

        transition = new RotateTransition(Duration.millis(1000), loadingImg);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAxis(Rotate.Z_AXIS);
        transition.setByAngle(360);

        type.getItems().addAll(langs);
        type.setValue(langs.get(0));
    }

    RotateTransition transition;

    /**
     * Retrieves suggestions for a given string.
     *
     * @param c The string for which suggestions are sought.
     */
    private void getSuggestions(String c) {
        if (c.trim().length() > 0) {
            try {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() {
                        ArrayList<String> res = Translate.getSuggestions(c.trim(), langs.indexOf(type.getValue()));
                        Platform.runLater(() -> {
                            suggestions.getItems().clear();
                            suggestions.getItems().addAll(res);
                        });
                        return null;
                    }
                };
                new Thread(task).start();
            } catch (Exception ignored) {

            }
        } else {
            suggestions.getItems().clear();
        }
    }

    /**
     * Handles various translation-related functionalities.
     */
    private void handleTranslate() {
        String word = wordToTranslate.getText();

        if (!word.equals("")) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        String translatedWord;
                        String detailContent;

                        if (type.getValue().equals(langs.get(0))) {
                            translatedWord = Translate.translate(word, "en", "vi");
                            detailContent = Translate.getDetail(word);
                        } else {
                            translatedWord = Translate.translate(word, "vi", "en");
                            detailContent = Translate.getDetail(translatedWord);
                        }
                        meaning = translatedWord;

                        Platform.runLater(() -> {
                            meaningView.getEngine().loadContent(translatedWord);
                            detail.getEngine().loadContent(detailContent);
                            detail.getEngine().getLoadWorker().stateProperty().addListener((a, b, c) -> {
                                if (c == Worker.State.SUCCEEDED) {
                                    JSObject window = (JSObject) detail.getEngine().executeScript("window");
                                    window.setMember("javaConnector", connector);
                                }
                            });
                            UserManager.getInstance().getCurrentUser().handleSearchWord();
                        });

                    } catch (Exception e) {
                        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "Lỗi mạng").show());
                    } finally {
                        transition.stop();
                        loadingImg.setVisible(false);
                    }
                    return null;
                }
            };

            transition.play();
            loadingImg.setVisible(true);
            new Thread(task).start();
        } else
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
    }

    /**
     * Initiates the speech synthesis of the word being translated.
     */
    private void speakWord() {
        if (!wordToTranslate.getText().equals(""))
            TextToSpeech.textToSpeech(wordToTranslate.getText());
        else {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
        }
    }

    /**
     * Adds a word to the user's list with its translation.
     */
    private void handleAddWord() {
        if (!wordToTranslate.getText().equals("") && !meaning.equals("")) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Đã thêm thành công");
            a.show();
            UserManager.getInstance().getCurrentUser().addWord(new Word(wordToTranslate.getText(), "<html>" + meaning + "</html>"));
            HomeController.getInstance().loadWordList();
        } else
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
    }

    /**
     * Handles editing of a word's translation.
     */
    private void handleEditWord() {
        if (!wordToTranslate.getText().trim().equals("") && !meaning.trim().equals("")) {
            showEditor();
            word.setText(wordToTranslate.getText());
            definition.setHtmlText(meaning);
        } else {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
        }
    }

    /**
     * Hides the word editor by animating its transition out of view.
     */
    private void hideEditor() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), div3);
        transition.setToX(-1000);
        transition.play();
    }

    /**
     * Displays the word editor by animating its transition into view.
     */
    private void showEditor() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), div3);
        transition.setToX(1000);
        transition.play();
    }

    /**
     * Saves the edited word and its translation, updates the displayed content, and hides the editor after saving the changes.
     */
    private void handleSaveEditWord() {
        String newWord = word.getText().trim();
        String newDefinition = definition.getHtmlText().replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        if (newWord.length() == 0) {
            new Alert(Alert.AlertType.WARNING, "Không được để trống").show();
            return;
        }
        hideEditor();
        wordToTranslate.setText(newWord);
        meaningView.getEngine().loadContent(newDefinition);
        meaning = newDefinition;
    }

    /**
     * Initializes the event handlers for various UI components related to translation and editing.
     */
    @Override
    protected void initEvents() {
        super.initEvents();
        wordToTranslate.textProperty().addListener((a, b, c) -> getSuggestions(c));
        suggestions.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> find(c));
        translateBtn.setOnAction(event -> handleTranslate());
        speakBtn.setOnAction(event -> speakWord());
        addBtn.setOnAction(event -> handleAddWord());
        editBtn.setOnAction(event -> handleEditWord());
        saveBtn.setOnAction(event -> handleSaveEditWord());
    }

    /**
     * Initializes the controller upon its initialization, setting it as an instance of itself.
     */
    @Override
    public void initialize() {
        super.initialize();
        instance = this;
    }
}



