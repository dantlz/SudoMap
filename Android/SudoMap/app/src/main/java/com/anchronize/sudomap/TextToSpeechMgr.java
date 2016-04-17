package com.anchronize.sudomap;

/**
 * Created by jasonlin on 4/17/16.
 */

import android.app.Activity;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Helper class used for managing the TextToSpeech engine (from Houndify sample)
 */
class TextToSpeechMgr implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;

    public TextToSpeechMgr( Activity activity ) {
        textToSpeech = new TextToSpeech( activity, this );
    }

    @Override
    public void onInit( int status ) {
        // Set language to use for playing text
        if ( status == TextToSpeech.SUCCESS ) {
            int result = textToSpeech.setLanguage(Locale.US);
        }
    }

    public void shutdown() {
        textToSpeech.shutdown();
    }
    /**
     * Play the text to the device speaker
     *
     * @param textToSpeak
     */
    public void speak( String textToSpeak ) {
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null);
    }
}