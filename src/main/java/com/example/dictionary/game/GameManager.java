package com.example.dictionary.game;

import com.example.dictionary.controller.UserController;
import com.example.dictionary.user.User;
import com.example.dictionary.user.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GameManager implements Serializable {
    public static final String DATA_PATH = "data/games.dat";
    private final ArrayList<GameInfo> playersHistory = new ArrayList<>();
    private static GameManager instance;

    /**
     * Retrieves the instance of the GameManager.
     *
     * @return The instance of the GameManager.
     */
    public static GameManager getInstance() {
        if (instance == null) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(DATA_PATH))) {
                instance = (GameManager) is.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    /**
     * Retrieves the best time taken by a player to win a specific game.
     *
     * @param gameId The ID of the game.
     * @param playerId The ID of the player.
     * @return The best time taken by the player to win the game.
     */
    public double getBestTime(int gameId, int playerId) {
        return playersHistory.stream().
                filter(gameInfo -> gameInfo.getPlayerId() == playerId
                        && gameInfo.getGameId() == gameId
                        && gameInfo.getStatus() == GameInfo.Status.WIN).
                map(GameInfo::getTime).min(Double::compare).orElse(Double.MAX_VALUE);
    }


    public ObservableList<User> getPlayersWon(int gameId) {
        ObservableList<User> players = FXCollections.observableArrayList(UserManager.getInstance().getUsers()).
                filtered(user -> GameManager.getInstance().getPlayersHistory().stream().
                        anyMatch(gameInfo ->
                                gameInfo.getPlayerId() == user.getId() &&
                                        gameInfo.getGameId() == gameId && gameInfo.getStatus() == GameInfo.Status.WIN
                        )).sorted(Comparator.comparingDouble(u -> GameManager.getInstance().getBestTime(gameId, u.getId())));
        return players;
    }

    /**
     * Retrieves the list of game history for players.
     *
     * @return The list of game history for players.
     */
    public ArrayList<GameInfo> getPlayersHistory() {
        return playersHistory;
    }


    public void addToPlayersHistory(GameInfo gameInfo) {
        playersHistory.add(gameInfo);
        UserController.getInstance().initAchievements();
    }


    /**
     * Writes game data to the specified file.
     */
    public static void writeData() {
        try (ObjectOutputStream is = new ObjectOutputStream(new FileOutputStream(DATA_PATH))) {
            is.writeObject(getInstance());
            System.out.println(getInstance().getPlayersHistory().size());
            System.out.println("wrote");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
