package com.example.lenny.quaarel;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.content.Context;

/**
 * Created by garyh on 11/07/2015.
 */
public class SoundManager {

    private  SoundPool ourSounds;
    private int bossMusic;
    private int stream;


    public SoundManager(Context context) {

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        ourSounds = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        ourSounds.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            }
        });
        bossMusic = ourSounds.load(context, R.raw.bossfight, 1);

    }


    public void playMusic() {
         stream = ourSounds.play(bossMusic, 0.9f, 0.9f, 1, 0, 1);
    }

    public void stopMusic() {
        ourSounds.stop(stream);
    }

}