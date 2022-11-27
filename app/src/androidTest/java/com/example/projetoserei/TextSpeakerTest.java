package com.example.projetoserei;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TextSpeakerTest {

    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void speak() throws InterruptedException {
        TextSpeaker textSpeaker = new TextSpeaker(appContext);
        Thread.sleep(1000);
        Thread.sleep(1000);
        assertThat(textSpeaker.Speak("Teste conversão texto"), is(0));
    }
}