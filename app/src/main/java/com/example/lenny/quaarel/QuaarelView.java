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

import java.util.Random;

public class QuaarelView extends SurfaceView implements Runnable{

    //Random numbers
    Random generator = new Random();
    int randomNumber;

    //Needed for gameplay + canvas
    private Context context;
    private Thread gamethread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing = false;
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
    private Hand hand;
    private Boss boss;
    private BossHealthBar bossHealthBar;
    private int bossHealth = 25;
    private Block[] block = new Block[25];
    private Rock[] rock = new Rock[50];
    private Book book;
    private boolean newRow;
    private boolean newRock;
    private boolean bossFight = false;
    private int strength = 1;
    private PauseButton pauseButton;
    private float gameSpeed = 1;

    //Powerup-s
    private PowerupHealth powerupHealth;
    private PowerupSpeed powerupSpeed;
    private PowerupCloud powerupCloud;
    private PowerupSizeBig powerupSizeBig;
    private PowerupGodMode powerupGodMode;
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
    private int blockHealth = 0;
    private boolean godMode;
    private int godModeEnd = 0;

    //For movement
    private float moveX;
    private float moveRightPos;
    private float moveLeftPos;
    private boolean moveRight;


    public QuaarelView(Context context, int x, int y){
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        soundManager = new SoundManager(context);
        prepareLevel();
        gamethread = new Thread(this);
    }

    public void prepareLevel(){
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
        speedEnd = 0;
        bossTime = 750;
        strength = 1;
        godMode = false;
        godModeEnd = 0;
        gameSpeed = 1;

        //Create objects
        quaarel = new Quaarel(context, screenX, screenY);
        hand = new Hand(context, screenX, screenY);
        boss = new Boss(context, screenX);
        bossHealthBar = new BossHealthBar(context, screenX);
        book = new Book(context, screenX);
        powerupHealth = new PowerupHealth(context, screenX);
        powerupSpeed = new PowerupSpeed(context, screenX);
        powerupCloud = new PowerupCloud(context, screenX);
        powerupSizeBig = new PowerupSizeBig(context, screenX);
        powerupGodMode = new PowerupGodMode(context, screenX);
        cloud = new Cloud(context, screenX);
        pauseButton = new PauseButton(context, screenX);

        for(int i = 0; i < block.length; i++) {
            block[i] = new Block(context, screenX);
        }

        for(int i = 0; i < rock.length; i++){
            rock[i] = new Rock(context, screenX);
        }
        paused = true;
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
             if(timeThisFrame > 0){
                 fps = 1000 / (long) (timeThisFrame * gameSpeed);
             }
         }
     }

    private void update(){

        quaarel.update();
        if(hand.getRight()) {
            hand.setX(quaarel.getX() + quaarel.getLength() - (quaarel.getLength() / 10));
        }else{
            hand.setX(quaarel.getX() - hand.getLength());
        }
        hand.update();

        score++;

        //If Quaarel is dead
        if (lives < 1) {
            paused = true;
            soundManager.stopMusic();
            prepareLevel();
        }

        if (score == godModeEnd){
            godMode = false;
        }

        //Count seconds played
        if (cnt >= fps){
            second++;
            cnt = 0;
        }else {cnt++;}

        //Every 2 seconds make new row of blocks
        if(second % 4 == 0 && cnt == 0){
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
        if (score % (30/(int) Math.ceil(speedCoef + (gameSpeed/20))) == 0 && score > 10) {
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
        if(score % (15/(int) Math.ceil(speedCoef + (gameSpeed/20))) == 0){
            hand.swichPos();
        }

        //start bossfight
        if(score == bossTime){
            bossFight = true;
            boss.init(screenX, bossHealth);
            bossHealthBar.init(context, screenX, bossHealth);
            soundManager.playMusic();
        }

        //initialize powerup
        if(score % 200 == 0 && score > 10){
            randomNumber = generator.nextInt(50);
            if(randomNumber < 2) {
                powerupHealth.init();
            }else if(randomNumber < 4){
                powerupSpeed.init();
            }else if(randomNumber < 6){
                powerupCloud.init();
            }else if(randomNumber < 8){
                powerupSizeBig.init();
            }else{
                powerupGodMode.init();
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
                if (RectF.intersects(quaarel.getRect(), block[i].getRect()) || RectF.intersects(hand.getRect(), block[i].getRect())) {
                    if(!godMode) {
                        lives--;
                        block[i].setInActive();
                        godMode = true;
                        godModeEnd = score + 100;
                    }
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
                    blockHealth = generator.nextInt((strength * 5)) + 1;
                    if (randomNumber != i) {
                        if (!block[(RowCnt * 7) + i].getStatus()) {
                            block[(RowCnt * 7) + i].init(i * screenX / 7, 0, blockHealth, (strength*5)+1);
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
                        block[j].gotHit(strength);
                        rock[i].setInActive();
                    }
                }
                if(boss.getStatus() && RectF.intersects(rock[i].getRect(), boss.getRect())){
                    boss.gotHit(strength);
                    bossHealthBar.gotHit(strength);
                    rock[i].setInActive();
                }
            }

            if(!rock[i].getStatus()) {
                if (newRock) {
                    if(hand.getRight()) {
                        rock[rockCnt].init(hand.getX() + (hand.getLength() / 2), Math.round(hand.getY()));
                    }else{
                        rock[rockCnt].init(hand.getX(), Math.round(hand.getY()));
                    }
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
                bossTime = score + 1000;
                boss.setInActive();
                bossHealthBar.setInactive();
                soundManager.stopMusic();
                gameSpeed = gameSpeed + (float) (0.2/gameSpeed);
            }
            if(!book.getStatus() && score > 350){
                book.init(boss.getX(), boss.getY());
            }
        }

        if(book.getStatus()){
            book.update(fps);
            if(RectF.intersects(book.getRect(), quaarel.getRect())){
                if(!godMode) {
                    lives--;
                    book.setInActive();
                    godMode = true;
                    godModeEnd = score + 100;
                }
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
            if(RectF.intersects(powerupHealth.getRect(), quaarel.getRect()) || RectF.intersects(powerupHealth.getRect(), hand.getRect())){
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
            if(RectF.intersects(powerupSpeed.getRect(), quaarel.getRect()) || RectF.intersects(powerupSpeed.getRect(), hand.getRect())){
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
            if(RectF.intersects(powerupCloud.getRect(), quaarel.getRect()) || RectF.intersects(powerupCloud.getRect(), hand.getRect())){
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

        //Size big
        if(powerupSizeBig.getStatus()){
            powerupSizeBig.update(fps);
            if(RectF.intersects(powerupSizeBig.getRect(), quaarel.getRect()) || RectF.intersects(powerupSizeBig.getRect(), hand.getRect())){
                for(int i = 0; i < rock.length; i++){
                    rock[i].setSize((float) (1 + (0.4/strength)));
                }
                hand.setSize((float) (1 + (0.2/strength)));
                powerupSizeBig.setInactive();
                strength++;
            }
            if(powerupSizeBig.getImpactPointY() > screenY){
                powerupSizeBig.setInactive();
            }
        }

        //God Mode
        if(powerupGodMode.getStatus()){
            powerupGodMode.update(fps);
            if(RectF.intersects(powerupGodMode.getRect(), quaarel.getRect()) || RectF.intersects(powerupGodMode.getRect(), hand.getRect())){
                godMode = true;
                godModeEnd = score + 300;
                powerupGodMode.setInactive();
            }
            if(powerupGodMode.getImpactPointY() > screenY){
                powerupGodMode.setInactive();
            }
        }
    }
    private void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));
            paint.setColor(Color.argb(255,255,255,255));

            paint.setTextSize(28);

            //All drawings (if items are active)
            if(!godMode || (score % 20 < 11) ) {
                canvas.drawBitmap(quaarel.getBitmap(), quaarel.getX(), quaarel.getY(), paint);
                canvas.drawBitmap(hand.getBitmap(), hand.getX(), hand.getY(), paint);
            }
            if(boss.getStatus()) {canvas.drawBitmap(boss.getBitmap(), boss.getX(), boss.getY(), paint);}
            if(bossHealthBar.getStatus()) {canvas.drawBitmap(bossHealthBar.getBackBitmap(), bossHealthBar.getX(), bossHealthBar.getY(), paint);}
            if(bossHealthBar.getStatus()) {canvas.drawBitmap(bossHealthBar.getFrontBitmap(), bossHealthBar.getX(), bossHealthBar.getY(), paint);}
            if(book.getStatus()) {canvas.drawBitmap(book.getBitmap(), book.getX(), book.getY(), paint);}
            if(powerupHealth.getStatus()) {canvas.drawBitmap(powerupHealth.getBitmap(), powerupHealth.getX(), powerupHealth.getY(), paint);}
            if(powerupSpeed.getStatus()) {canvas.drawBitmap(powerupSpeed.getBitmap(), powerupSpeed.getX(), powerupSpeed.getY(), paint);}
            if(powerupCloud.getStatus()) {canvas.drawBitmap(powerupCloud.getBitmap(), powerupCloud.getX(), powerupCloud.getY(), paint);}
            if(powerupSizeBig.getStatus()) {canvas.drawBitmap(powerupSizeBig.getBitmap(), powerupSizeBig.getX(), powerupSizeBig.getY(), paint);}
            if(powerupGodMode.getStatus()) {canvas.drawBitmap(powerupGodMode.getBitmap(), powerupGodMode.getX(), powerupGodMode.getY(), paint);}
            for(int i = 0; i < block.length; i++) {
                if (block[i].getStatus()) {
                    canvas.drawBitmap(block[i].getBitmap(), block[i].getX(), block[i].getY(), paint);
                    if(block[i].getHealth() < (block[i].getMaxHealth() / 3)){
                        paint.setColor(Color.RED);
                    }else if(block[i].getHealth() < (block[i].getMaxHealth() / 3 * 2)){
                        paint.setColor(Color.YELLOW);
                    }else{
                        paint.setColor(Color.GREEN);
                    }
                    canvas.drawText(String.valueOf(block[i].getHealth()), block[i].getX() + (block[i].getWidth() / 2) - 14, block[i].getY() - 14, paint);
                }

            }
            for(int i = 0; i  < rock.length; i++){
                if (rock[i].getStatus()) {canvas.drawBitmap(rock[i].getBitmap(), rock[i].getX(), rock[i].getY(), paint);}
            }
            //Lives on top right corner
            for(int i = 0; i < lives; i++){
                canvas.drawBitmap(quaarel.getSmallBitmap(), screenX - ((i+1) * quaarel.getLength()/2) - (i*10) - 10, quaarel.getLength() - 50, paint);
            }

            //Score text
            paint.setColor(Color.WHITE);
            paint.setTextSize(28);
            canvas.drawText("Score: " + score,10,50,paint);

            //Pause button
            canvas.drawBitmap(pauseButton.getBitmap(),screenX - (screenX / 10), screenY / 11, paint);


            //Cloud is drawn last so it will be on top of everything else
            if(cloud.getStatus()) {canvas.drawBitmap(cloud.getBitmap(), cloud.getX(), cloud.getY(), paint);}

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        playing = false;
        paused = true;
        try {
            gamethread.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }
        soundManager.stopMusic();
    }

    public void resume(){
        playing = true;
        //paused = false;
        gamethread = new Thread(this);
        gamethread.start();
        if (bossFight) {
            soundManager.playMusic();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){


            /*case MotionEvent.PointerCoords:
                if ((MotionEvent.getX() > screenX - (screenX / 10)) && (MotionEvent.getX() < screenX) && (MotionEvent.getY()> (screenY / 11)) && (MotionEvent.getY() < ((screenY / 11) - (screenX / 10))))
                {
                    pause();
                    break;
                }*/

            case MotionEvent.ACTION_DOWN:

                    paused = false;
                    moveX = motionEvent.getX();
                    break;


            case MotionEvent.ACTION_MOVE:
                if((hand.getX() > 0 && moveX > motionEvent.getX()) || (quaarel.getX() < screenX - (quaarel.getLength() + hand.getLength()) && moveX < motionEvent.getX()) ){
                    quaarel.setX(quaarel.getX() + motionEvent.getX() - moveX);
                }
                if(motionEvent.getX() > moveX && !moveRight){
                    moveRightPos = quaarel.getX() + (screenX/8);
                    moveRight = true;
                }else if(motionEvent.getX() < moveX && moveRight){
                    moveLeftPos = quaarel.getX() - (screenX/8);
                    moveRight = false;
                }
                if(quaarel.getX() > moveRightPos && moveRight){
                    quaarel.setRight(true);
                    hand.setRight(true);
                }else if(quaarel.getX() < moveLeftPos && !moveRight){
                    quaarel.setRight(false);
                    hand.setRight(false);
                }
                moveX = motionEvent.getX();
                break;

        }
        return true;
    }

}

