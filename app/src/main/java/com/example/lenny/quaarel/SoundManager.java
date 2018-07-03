package com.example.lenny.quaarel;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.content.Context;

/**
 * Created by garyh on 11/07/2015.
 */
public class SoundManager {

    private  SoundPool ourSounds;
    private int bossMusic;
    private int bgMusic;
    private int bgStream;
    private int bossStream;
    private MediaPlayer mp;


    public SoundManager(Context context) {

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        ourSounds = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
       // ourSounds.setOnLoadCompleteListener(new OnLoadCompleteListener() {
       //     @Override
       //     public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
       //     }
       // });
        bossMusic = ourSounds.load(context, R.raw.bossfight, 1);
        //bgMusic = ourSounds.load(context, R.raw.kindakirjad,3);


        mp = MediaPlayer.create(context, R.raw.kindakirjad);
        mp.setLooping(true);
    }


    public void playMusic() {
         //bgStream = ourSounds.play(bgMusic, 0.9f, 0.9f, 1, 1, 1);
        mp.start();
    }

    public void stopMusic(){
        mp.stop();
    }
    public void hideMusic() {
        //ourSounds.stop(bgStream);
        mp.setVolume(0,0);
    }
    public void continueMusic(){
        mp.setVolume(1,1);
    }

    public void playBoss() {
        bossStream = ourSounds.play(bossMusic, 0.9f, 0.9f, 1, 1, 1);
    }

    public void stopBoss() {
        ourSounds.stop(bossStream);
    }

}