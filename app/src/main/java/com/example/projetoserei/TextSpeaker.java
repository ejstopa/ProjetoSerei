package com.example.projetoserei;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextSpeaker {

    Context Context1;
    TextToSpeech textToSpeech;

    public TextSpeaker(Context c) {
        this.Context1 = c;

        textToSpeech = new TextToSpeech(Context1.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("pt-br"));
                    textToSpeech.setSpeechRate(1f);
                }
            }
        });

    }

    public int Speak(String text){

        return textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void Shutdown(){
        textToSpeech.shutdown();
    }

    public void Stop(){
        textToSpeech.stop();
    }
}
