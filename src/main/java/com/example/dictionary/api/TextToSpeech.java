package com.example.dictionary.api;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.concurrent.Task;

public class TextToSpeech {
    static {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    /**
     * Converts the given text into speech.
     *
     * @param text The text to be converted into speech.
     */
    public static void textToSpeech(String text) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Voice voice = VoiceManager.getInstance().getVoice("kevin");
                if (voice != null) {
                    voice.allocate();
                    voice.setPitch(150.0f);  // Adjust the pitch
                    voice.setRate(150.0f);   // Adjust the speech rate
                    voice.setVolume(3.0f);   // Adjust the volume
                    voice.speak(text);
                }
                return null;
            }
        };
        new Thread(task).start();

    }
}
