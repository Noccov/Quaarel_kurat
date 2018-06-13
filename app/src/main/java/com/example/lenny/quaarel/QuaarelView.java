package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

public class QuaarelView extends SurfaceView implements Runnable{

    //Random numbers
    Random generator = new Random();
    int randomNumber;

    //Needed for gameplay + canvas
    private Context context;
    private Thread gamethread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;

    //Timing
    private long fps;
    private long timeThisFrame;

    //Screen size measurement
    private int screenX;
    private int screenY;

    //Game objects
    private Quaarel quaarel;
    private Boss boss;
    private Block[] block = new Block[25];
    private Rock[] rock = new Rock[50];
    private Book book;
    private boolean newRow;
    private boolean newRock;
    private boolean bossFight = false;

    //Powerup-s
    private PowerupHealth powerupHealth;
    private PowerupSpeed powerupSpeed;
    private PowerupCloud powerupCloud;
    private Cloud cloud;
    private int speedEnd;
    private int speedCoef;
    private int bossTime = 250;

    //Sound
    private SoundManager soundManager;

    //Game variables
    private int lives = 3;
    private int score = 0;
    private int RowCnt = 0;
    private int rockCnt = 0;
    private int second = 0;
    private int cnt = 0;

    //For movement
    private float moveX;


    public QuaarelView(Context context, int x, int y){
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        soundManager = new SoundManager(context);

        prepareLevel();
    }

    private void prepareLevel(){
        //Initialize game variables
        bossFight = false;
        lives = 2;
        score = 0;
        newRow = false;
        newRock = false;
        cnt = 0;
        RowCnt = 0;
        rockCnt = 0;
        speedCoef = 1;

        //Create objects
        quaarel = new Quaarel(context, screenX, screenY);
        boss = new Boss(context, screenX);
        book = new Book(context, screenX);
        powerupHealth = new PowerupHealth(context, screenX);
        powerupSpeed = new PowerupSpeed(context, screenX);
        powerupCloud = new PowerupCloud(context, screenX);
        cloud = new Cloud(context, screenX);

        for(int i = 0; i < block.length; i++) {
            block[i] = new Block(context, screenX);
        }

        for(int i = 0; i < rock.length; i++){
            rock[i] = new Rock(context, screenX);
        }

    }

     @Override
     public void run(){
         while(playing){

             long startFrameTime = System.currentTimeMillis();

             if(!paused){update();}

             draw();

             timeThisFrame = System.currentTimeMillis() - startFrameTime;
             if(timeThisFrame >= 1){fps = 1000 / timeThisFrame;}
         }
     }

    private void update(){

        quaarel.update();
        score++;

        //If Quaarel is dead
        if (lives == 0) {
            paused = true;
            soundManager.stopMusic();
            prepareLevel();
        }

        //Count seconds played
        if (cnt >= fps){
            second++;
            cnt = 0;
        }else {cnt++;}

        //Every 2 seconds make new row of blocks
        if(second % 2 == 0 && cnt == 0){
            newRow = true;
            if(RowCnt < 2){
                RowCnt++;
            }else{RowCnt = 0;}
        }else{newRow = false;}

        if(score < speedEnd){
            speedCoef = 2;
        }else{
            speedCoef = 1;
        }

        //create new rock after every 10/20 scorepoints
        if (score % (20/speedCoef) == 0 && score > 10) {
            newRock = true;
            if (rockCnt < 49) {
                rockCnt++;
            } else {
                rockCnt = 0;
            }
        } else {
            newRock = false;
        }

        //switch hand position half of every new rock
        if(score % (10/speedCoef) == 0){
            quaarel.swichPos();
        }

        //start bossfight
        if(score == bossTime){
            bossFight = true;
            boss.init(screenX);
            soundManager.playMusic();
        }

        //initialize powerup
        if(score % 500 == 0 && score > 10){
            randomNumber = generator.nextInt(6);
            if(randomNumber < 2) {
                powerupHealth.init();
            }else if(randomNumber < 4){
                powerupSpeed.init();
            }else if(randomNumber < 7){
                powerupCloud.init();
            }
        }


        updateBlocks();
        updateRocks();
        updateBossFight();
        updatePowerups();

    }

    private void updateBlocks(){
        for(int i = 0; i < block.length; i++) {
            if (block[i].getStatus()) {
                block[i].update(fps);
            }

            if (block[i].getStatus()) {
                if (RectF.intersects(quaarel.getRect(), block[i].getRect())) {
                    lives--;
                    block[i].setInActive();
                }
            }

            if (block[i].getImpactPointY() > screenY) {
                block[i].setInActive();
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
    }

    private void updateRocks(){
        for(int i = 0; i < rock.length; i++) {
            if (rock[i].getStatus()){
                rock[i].update(fps);
                for(int j = 0; j < block.length; j++){
                    if(block[j].getStatus() && RectF.intersects(block[j].getRect(), rock[i].getRect())){
                        block[j].gotHit();
                        rock[i].setInActive();
                    }
                }
                if(boss.getStatus() && RectF.intersects(rock[i].getRect(), boss.getRect())){
                    boss.gotHit();
                    rock[i].setInActive();
                }
            }

            if(!rock[i].getStatus()) {
                if (newRock) {
                    rock[rockCnt].init(quaarel.getX() + (quaarel.getLength()/2), Math.round(quaarel.getY()));
                }
            }
            if(rock[i].getImpactPointY() < 0){
                rock[i].setInActive();
            }
        }
    }

    private void updateBossFight(){
        if(bossFight){
            boss.update(fps, screenX);
            if(boss.getHealth() < 1){
                bossFight = false;
                bossTime = score + 500;
                boss.setInActive();
                soundManager.stopMusic();
            }
            if(!book.getStatus() && score > 350){
                book.init(boss.getX(), boss.getY());
            }
        }

        if(book.getStatus()){
            book.update(fps);
            if(RectF.intersects(book.getRect(), quaarel.getRect())){
                lives--;
                book.setInActive();
            }
        }

        if(book.getImpactPointY() > screenY){
            book.setInActive();
        }
    }

    private void updatePowerups(){
        //Health
        if(powerupHealth.getStatus()){
            powerupHealth.update(fps);
            if(RectF.intersects(powerupHealth.getRect(), quaarel.getRect())){
                lives++;
                powerupHealth.setInactive();
            }
            if(powerupHealth.getImpactPointY() > screenY){
                powerupHealth.setInactive();
            }
        }

        //Speed
        if(powerupSpeed.getStatus()){
            powerupSpeed.update(fps);
            if(RectF.intersects(powerupSpeed.getRect(), quaarel.getRect())){
                speedEnd = score + 500;
                powerupSpeed.setInactive();
            }
            if(powerupSpeed.getImpactPointY() > screenY){
                powerupSpeed.setInactive();
            }
        }

        //Vape
        if(powerupCloud.getStatus()){
            powerupCloud.update(fps);
            if(RectF.intersects(powerupCloud.getRect(), quaarel.getRect())){
                cloud.init(screenY);
                powerupCloud.setInactive();
            }
            if(powerupCloud.getImpactPointY() > screenY){
                powerupCloud.setInactive();
            }
        }
        //Cloud (from Vape)
        if(cloud.getStatus()){
            cloud.update(fps);
            if(cloud.getImpactPointY() < 0){
                cloud.setInactive();
            }
        }
    }
    private void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));
            paint.setColor(Color.argb(255,255,255,255));

            //All drawings (if items are active)
            canvas.drawBitmap(quaarel.getBitmap(), quaarel.getX(), quaarel.getY(), paint);
            if(boss.getStatus()) {canvas.drawBitmap(boss.getBitmap(), boss.getX(), boss.getY(), paint);}
            if(book.getStatus()) {canvas.drawBitmap(book.getBitmap(), book.getX(), book.getY(), paint);}
            if(powerupHealth.getStatus()) {canvas.drawBitmap(powerupHealth.getBitmap(), powerupHealth.getX(), powerupHealth.getY(), paint);}
            if(powerupSpeed.getStatus()) {canvas.drawBitmap(powerupSpeed.getBitmap(), powerupSpeed.getX(), powerupSpeed.getY(), paint);}
            if(powerupCloud.getStatus()) {canvas.drawBitmap(powerupCloud.getBitmap(), powerupCloud.getX(), powerupCloud.getY(), paint);}
            for(int i = 0; i < block.length; i++) {
                if (block[i].getStatus()) {canvas.drawBitmap(block[i].getBitmap(), block[i].getX(), block[i].getY(), paint);}
            }
            for(int i = 0; i  < rock.length; i++){
                if (rock[i].getStatus()) {canvas.drawBitmap(rock[i].getBitmap(), rock[i].getX(), rock[i].getY(), paint);}
            }
            //Lives on top right corner
            for(int i = 0; i < lives; i++){
                canvas.drawBitmap(quaarel.getSmallBitmap(), screenX - ((i+1) * quaarel.getLength()/2) - (i*10) - 10, quaarel.getLength() - 50, paint);
            }

            //Score text
            paint.setTextSize(28);
            canvas.drawText("Score: " + score,10,50,paint);

            //Cloud is drawn last so it will be on top of everything else
            if(cloud.getStatus()) {canvas.drawBitmap(cloud.getBitmap(), cloud.getX(), cloud.getY(), paint);}

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
                moveX = motionEvent.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                if((quaarel.getX() > 0 && moveX > motionEvent.getX()) || (quaarel.getX() < screenX - quaarel.getLength() && moveX < motionEvent.getX()) ){
                    quaarel.setX(quaarel.getX() + motionEvent.getX() - moveX);
                }
                moveX = motionEvent.getX();
                break;

        }
        return true;
    }

}

