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

    private Block[] block = new Block[10];

    private int lives = 3;
    private int cnt = 0;

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

        for(int i = 0; i < block.length; i++) {
            block[i] = new Block(screenX, screenY);
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
        boolean bumped = false;
        boolean newRow = false;
        int randomNumber;


        quaarel.update(fps);

        if (cnt >= fps * 3){
            newRow = true;
            cnt = 0;
        }else{
            newRow = false;
            cnt++;
        }

        randomNumber = generator.nextInt(7);

        for(int i = 0; i < block.length; i++) {
            if (block[i].getStatus()) {
                block[i].update(fps);
            }


            if (block[i].getStatus()) {
                if (RectF.intersects(quaarel.getRect(), block[i].getRect())) {
                    lives--;
                    //prepareLevel();
                    if (lives == 0) {
                        paused = true;
                        lives = 3;
                        prepareLevel();
                    }
                }

            }
            if(newRow) {
               if(randomNumber != i) {
                   if (!block[i].getStatus()) {
                       block[i].init(i * screenX / 7, 0);
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

            canvas.drawBitmap(quaarel.getBitmap(), quaarel.getX(), screenY-120, paint);

            for(int i = 0; i < block.length; i++) {
                if (block[i].getStatus()) {
                    canvas.drawRect(block[i].getRect(), paint);
                }
            }

            canvas.drawText("Lives: " + lives,10,50,paint);

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
