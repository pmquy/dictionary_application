package com.example.dictionary.game;

import com.example.dictionary.user.UserManager;

import java.io.Serializable;

public class GameInfo implements Serializable {
    public enum Status{
        WIN,
        LOSE;
    }

    private int gameId;
    private int playerId;
    private double time;
    private Status status;

    /**
     * Constructs a GameInfo object with specified game ID, player ID, time, and status.
     *
     * @param gameId The ID of the game.
     * @param playerId The ID of the player.
     * @param time The time taken to play the game.
     * @param status The status of the game (WIN or LOSE).
     */
    public GameInfo(int gameId, int playerId, int time, Status status) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.time = time;
        this.status = status;
    }

    /**
     * Constructs a GameInfo object with specified game ID, time, and status for the current player.
     *
     * @param gameId The ID of the game.
     * @param time The time taken to play the game.
     * @param status The status of the game (WIN or LOSE).
     */
    public GameInfo(int gameId, double time, Status status) {
        this.gameId = gameId;
        this.playerId = UserManager.getInstance().getCurrentUser().getId();
        this.time = time;
        this.status = status;
    }

    /**
     * Retrieves the ID of the game.
     *
     * @return The ID of the game.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the ID of the game.
     *
     * @param gameId The ID of the game to be set.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Retrieves the ID of the player associated with the game.
     *
     * @return The ID of the player associated with the game.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Sets the ID of the player associated with the game.
     *
     * @param playerId The ID of the player to be associated with the game.
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Retrieves the time taken to play the game.
     *
     * @return The time taken to play the game.
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the time taken to play the game.
     *
     * @param time The time taken to play the game.
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Retrieves the status of the game (WIN or LOSE).
     *
     * @return The status of the game (WIN or LOSE).
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the game (WIN or LOSE).
     *
     * @param status The status of the game to be set (WIN or LOSE).
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}