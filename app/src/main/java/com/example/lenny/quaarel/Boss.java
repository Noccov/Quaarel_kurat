package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.content.Context;


public class Boss {

    private float x;
    private float y;

    private RectF rect;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;
    private Bitmap bitmap_rage;

    float speed = 200;

    private int width;
    private int height;

    private int health = 20;
    private float maxHealth = 20;

    private boolean isActive;
    private boolean charge = false;
    private boolean chargeUp = false;
    //private boolean alreadyCharged = false;
    private int nextCharge = 60;

    private final int LEFT = 1;
    private final int RIGHT = 2;
    private final int NONE = 0;

    private int bossMoving = 0;
    private boolean invincible = true;
    private boolean isHit;
    private int cnt = 0;
    private boolean rage = false;


    public Boss(Context context, int screenX){

        //square with side 1/3 of screen width
        width = screenX / 3;
        height = width;
        x = screenX / 2 - (width/2);
        y = -height;
        isActive = false;

        rect = new RectF();

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_1);
        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                width,
                height,
                false);

        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_2);
        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                width,
                height,
                false);

        bitmap_rage = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_rage);
        bitmap_rage = Bitmap.createScaledBitmap(bitmap_rage,
                width,
                height,
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
        bitmap_rage = makeTransparent(bitmap_rage);
    }

    public boolean init(int screenX, int initHealth){
        if(!isActive){
            charge = false;
            chargeUp = false;
            //alreadyCharged = false;
            nextCharge = 60;
            isHit = false;
            bossMoving = 0;
            cnt = 0;
            invincible = true;
            health = initHealth;
            maxHealth = initHealth;
            x = screenX / 2 - (width/2);
            y = -height;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps, int screenX, int screenY){
        //Moving down
        if(!charge) {
            if (y < height / 2) {
                y = y + speed / fps;
                //Moving left and right
            } else if (bossMoving == RIGHT) {
                invincible = false;
                x = x + speed / fps;
            } else {
                invincible = false;
                x = x - speed / fps;
            }
        }else{
            if(!chargeUp){
                y = y + (speed * 5 / fps);
            }else{
                y = y - speed / fps;
            }
            if(y > screenY - height){
                chargeUp = true;
            }
            if(y < height/2){
                charge = false;
                chargeUp = false;
                invincible = true;
            }
        }
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

        //Hit sprite counter
        if(isHit) {
            cnt++;
            if (cnt > fps){
                isHit = false;
                cnt = 0;
            }
        }

        //Left/Right movement change
        if(x < 0){
            bossMoving = RIGHT;
        }else if(x > screenX - width){
            bossMoving = LEFT;
        }
    }

    public void gotHit(int strength){
        if(!invincible) {
            health = health - strength;
            if(!charge && health < ((maxHealth / 100) * nextCharge)){
                charge = true;
                //alreadyCharged = true;
                invincible = true;
                nextCharge = nextCharge - 20;
            }
            isHit = true;
            cnt = 0;
        }
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}

    public boolean getInvincible(){return invincible;}

    public int getHeight(){return height;}

    public boolean getCharge(){return charge;}

    public void setRage(boolean newRage){rage = newRage;}

    public boolean getRage(){return rage;}

    public Bitmap getBitmap() {
        if(rage){
            return bitmap_rage;
        }
        if(isHit){
            return bitmap_2;
        }else {
            return bitmap_1;
        }
    }


    public int getHealth(){return health;}

    // Convert transparentColor to be transparent in a Bitmap.
    public static Bitmap makeTransparent(Bitmap bit) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
            if (allpixels[i] == android.graphics.Color.BLACK)

                allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }
}
