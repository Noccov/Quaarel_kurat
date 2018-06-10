package com.example.lenny.quaarel;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

public class QuaarelView extends SurfaceView implements Runnable{

    Random generator = new Random();

    private Context context;

    private Thread gamethread = null;

    private SurfaceHolder ourHolder;

    private volatile boolean playing;

    private boolean paused = true;

    private Canvas canvas;
    private Paint paint;

    private long fps;

    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private Quaarel quaarel;

    private Boss boss;

    private Block[] block = new Block[25];

    private Rock[] rock = new Rock[50];

    private int lives = 3;
    private int score = 0;
    private int RowCnt = 0;
    private int rockCnt = 0;
    private int second = 0;
    private int cnt = 0;

    private boolean bossFight = false;

    public QuaarelView(Context context, int x, int y){
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }

    private void prepareLevel(){
        quaarel = new Quaarel(context, screenX, screenY);

        boss = new Boss(context, screenX, screenY);

        for(int i = 0; i < block.length; i++) {
            block[i] = new Block(context, screenX, screenY);
        }

        for(int i = 0; i < rock.length; i++){
            rock[i] = new Rock(screenX);
        }
    }

    @Override
    public void run(){
        while(playing){

            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000 / timeThisFrame;
            }

        }
    }

    private void update(){
        boolean newRow;
        boolean newRock;
        int randomNumber;


        quaarel.update(fps);
        score++;

        if (cnt >= fps){
            second++;
            cnt = 0;
        }else {cnt++;}

        if(second % 2 == 0 && cnt == 0){
            newRow = true;
            if(RowCnt < 2){
                RowCnt++;
            }else{RowCnt = 0;}
        }else{newRow = false;}

        if(score % 20 == 0){
            newRock = true;
            if(rockCnt < 49){
                rockCnt++;
            }else{rockCnt = 0;}
        }else{newRock = false;}

        if(score == 250){
            bossFight = true;
        }

        if(bossFight){
            boss.update(context, fps, screenX);
        }


        for(int i = 0; i < rock.length; i++) {
            if (rock[i].getStatus()){
                rock[i].update(fps);
                for(int j = 0; j < block.length; j++){
                    if(block[j].getStatus() && RectF.intersects(block[j].getRect(), rock[i].getRect())){
                        block[j].gotHit(context);
                        rock[i].setInActive();
                    }
                }
                if(RectF.intersects(rock[i].getRect(), boss.getRect())){
                    boss.gotHit(context);
                    rock[i].setInActive();
                }
            }

          if(!rock[i].getStatus()) {
              if (newRock) {
                  rock[rockCnt].init(quaarel.getX(), Math.round(quaarel.getY()));
              }
          }
           if(rock[i].getImpactPointY() < 0){
               rock[i].setInActive();
           }
        }


        if(!bossFight) {
            randomNumber = generator.nextInt(7);

            for (int i = 0; i < 7; i++) {
                if (newRow) {
                    if (randomNumber != i) {
                        if (!block[(RowCnt * 7) + i].getStatus()) {
                            block[(RowCnt * 7) + i].init(i * screenX / 7, 0);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < block.length; i++) {
            if (block[i].getStatus()) {
                block[i].update(fps);
            }


            if (block[i].getStatus()) {
                if (RectF.intersects(quaarel.getRect(), block[i].getRect())) {
                    lives--;
                    block[i].setInActive();
                    //prepareLevel();
                    if (lives == 0) {
                        paused = true;
                        lives = 3;
                        score = 0;
                        prepareLevel();
                    }
                }
            }

            if (block[i].getImpactPointY() > screenY) {
                block[i].setInActive();
            }
        }

    }

    private void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,26,128,182));

            paint.setColor(Color.argb(255,255,255,255));

            canvas.drawBitmap(quaarel.getBitmap(), quaarel.getX(), quaarel.getY(), paint);

            canvas.drawBitmap(boss.getBitmap(), boss.getX(), boss.getY(), paint);

            for(int i = 0; i < block.length; i++) {
                if (block[i].getStatus()) {
                    canvas.drawBitmap(block[i].getBitmap(), block[i].getX(), block[i].getY(), paint);
                }
            }

            for(int i = 0; i  < rock.length; i++){
                if (rock[i].getStatus()) {
                    canvas.drawRect(rock[i].getRect(), paint);
                }
            }

            for(int i = 0; i < lives; i++){
                canvas.drawBitmap(quaarel.getSmallBitmap(), screenX - ((i+1) * quaarel.getLength()/2) - (i*10) - 10, quaarel.getLength() - 50, paint);
            }


            paint.setTextSize(28);
            canvas.drawText("Score: " + score,10,50,paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        playing = false;
        try {
            gamethread.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }

    public void resume(){
        playing = true;
        gamethread = new Thread(this);
        gamethread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:

                paused = false;

                if(motionEvent.getX() > screenX / 2){
                    quaarel.setMovementState(quaarel.RIGHT);
                } else{
                    quaarel.setMovementState(quaarel.LEFT);
                }


                break;

            case MotionEvent.ACTION_UP:

                quaarel.setMovementState(quaarel.STOPPED);

                break;

        }
        return true;
    }

}
