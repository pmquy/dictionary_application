package com.example.dictionary.controller;

import com.example.dictionary.game.Game1;
import com.example.dictionary.game.GameManager;
import com.example.dictionary.user.User;
import com.example.dictionary.user.UserManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.stream.Collectors;

public class TableController {
    public static int MAX_PLAYER_SHOW = 5;
    @FXML
    TableView<User> topPlayer;
    @FXML
    TableColumn<User, Integer> sttCol;
    @FXML
    TableColumn<User, String> userCol;
    @FXML
    TableColumn<User, Double> timeCol;

    @FXML
    public void initialize() {
//        instance = this;
//        sttCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(topPlayer.getItems().indexOf(collumn.getValue()) + 1));
//        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
//        timeCol.setCellValueFactory(collumn -> new ReadOnlyObjectWrapper<>(
//                GameManager.getInstance().getBestTime(GameManager.getCurrentGameId(), collumn.getValue().getId())));
//        updateBXH();
    }

    public void updateBXH() {
//        ObservableList<User> players = FXCollections.observableArrayList(UserManager.getInstance().getUsers()).
//                filtered(user -> GameManager.getInstance().getPlayersHistory().stream().
//                        anyMatch(gameInfo -> gameInfo.getPlayerId() == user.getId() && gameInfo.getGameId() == GameManager.getCurrentGameId())).
//                sorted(Comparator.comparingDouble(u -> GameManager.getInstance().getBestTime(GameManager.getCurrentGameId(), u.getId())));
//        topPlayer.getItems().clear();
//        topPlayer.setItems(FXCollections.observableArrayList(
//                players.stream().filter(player -> players.indexOf(player) < MAX_PLAYER_SHOW).collect(Collectors.toList())));
    }

    private static TableController instance;

    public static TableController getInstance() {
        return instance;
    }

    public static void setInstance(TableController instance) {
        TableController.instance = instance;
    }
}
