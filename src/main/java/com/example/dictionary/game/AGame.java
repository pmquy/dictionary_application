package com.example.dictionary.game;

import java.io.*;
import java.util.Map;

public abstract class AGame implements Serializable {
    private transient boolean isReady = true;

    /**
     * Checks if the game is ready to be played.
     *
     * @return True if the game is ready, otherwise false.
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Sets the readiness state of the game.
     *
     * @param ready The readiness state to set for the game.
     */
    public void setReady(boolean ready) {
        isReady = ready;
    }
}
