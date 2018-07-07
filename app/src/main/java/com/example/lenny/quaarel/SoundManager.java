package com.example.lenny.quaarel;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.content.Context;

import java.io.IOException;

/**
 * Created by garyh on 11/07/2015.
 */
public class SoundManager {

    private  SoundPool ourSounds;
    private int bossMusic;
    private int loseHealth;
    private int die;
    private int on_hit;
    private int blaster;
    private int speed;
    private int godMode;
    private int booster;
    private int loseHealthStream;
    private int dieStream;
    private int blasterStream;
    private int onhitStream;
    private int bossStream;
    private int speedStream;
    private int godModeStream;
    private int boosterStream;
    private MediaPlayer mp;


    public SoundManager(Context context) {

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        ourSounds = new SoundPool.Builder()
                .setMaxStreams(15)
                .setAudioAttributes(audioAttributes)
                .build();
       // ourSounds.setOnLoadCompleteListener(new OnLoadCompleteListener() {
       //     @Override
       //     public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
       //     }
       // });
        bossMusic = ourSounds.load(context, R.raw.bossfight, 1);
        loseHealth = ourSounds.load(context, R.raw.quaarel_kurat_audio, 2);
        die = ourSounds.load(context, R.raw.dying_final_2, 2);
        on_hit = ourSounds.load(context, R.raw.on_hit, 2);
        blaster = ourSounds.load(context, R.raw.blaster_sound_final, 2);
        speed = ourSounds.load(context, R.raw.speed, 2);
        godMode = ourSounds.load(context, R.raw.godlike, 2);
        booster = ourSounds.load(context, R.raw.booster,2);

        //bgMusic = ourSounds.load(context, R.raw.kindakirjad,3);
        mp = MediaPlayer.create(context, R.raw.kindakirjad);
        mp.setLooping(true);
    }


    public void playMusic() {
        mp.start();
    }


    public void pauseMusic(){
        mp.pause();
    }
    public void hideMusic() {
        //ourSounds.stop(bgStream);
        mp.setVolume(0,0);
    }
    public void continueMusic(){
        mp.setVolume(1,1);
    }

    public void setMusicSpeed(float newSpeed){
        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(newSpeed));
    }

    public void playBoss() {
        bossStream = ourSounds.play(bossMusic, 0.9f, 0.9f, 1, 1, 1);
    }

    public void stopBoss() {
        ourSounds.stop(bossStream);
    }

    public void playLoseHealth(){loseHealthStream = ourSounds.play(loseHealth, 0.9f, 0.9f, 1, 0, 1);}

    public void playDie(){dieStream = ourSounds.play(die, 0.9f, 0.9f, 1, 0, 1);}

    public void playBlaster(){blasterStream = ourSounds.play(blaster, 0.9f, 0.9f,1, 0, 1);}

    public void playHit(){onhitStream = ourSounds.play(on_hit, 0.9f, 0.9f, 1, 0 , 1);}

    public void playSpeed(){speedStream = ourSounds.play(speed, 0.9f, 0.9f, 1,0,1);}

    public void playGodMode(){godModeStream = ourSounds.play(godMode, 0.9f,0.9f,1,0,1);}

    public void playBooster(){boosterStream = ourSounds.play(booster,0.9f,0.9f,1,0,1);}

}