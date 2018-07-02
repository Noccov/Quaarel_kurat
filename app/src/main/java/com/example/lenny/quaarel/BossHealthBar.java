package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import java.util.Random;

public class BossHealthBar {

   // private RectF rect;

    private Bitmap bitmap_front;
    private Bitmap bitmap_back;

    private float frontLength;
    private float frontHeight;
    private float backLength;
    private float backHeight;

    private float x;
    private float y;

    private int speed = 200;
    private int initHealth;
    private int health;

    private boolean isActive;


    public BossHealthBar(Context context, int screenX) {

       // rect = new RectF();

        backLength = screenX - (screenX / 20);
        backHeight = backLength / 25;

        frontLength = backLength;
        frontHeight = backHeight;

        bitmap_front = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_front);
        bitmap_front = Bitmap.createScaledBitmap(bitmap_front,
                (int) (frontLength),
                (int) (frontHeight),
                false);
        bitmap_front = makeTransparent(bitmap_front);

        bitmap_back = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_back);
        bitmap_back = Bitmap.createScaledBitmap(bitmap_back,
                (int) (backLength),
                (int) (backHeight),
                false);
        bitmap_back = makeTransparent(bitmap_back);

    }
    public boolean init(Context context ,int screenX, int bossHealth){
        if(!isActive){
            backLength = screenX - (screenX / 20);
            backHeight = backLength / 25;

            frontLength = backLength;
            frontHeight = backHeight;

            bitmap_front = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_front);
            bitmap_front = Bitmap.createScaledBitmap(bitmap_front,
                    (int) (frontLength),
                    (int) (frontHeight),
                    false);
            bitmap_front = makeTransparent(bitmap_front);

            initHealth = bossHealth;
            health = initHealth;
            x = screenX / 2 - (frontLength/2);
            y = 0;
            isActive = true;
            return true;
        }
        return false;
    }


    public Bitmap getFrontBitmap() {return bitmap_front;}
    public Bitmap getBackBitmap() {return bitmap_back;}

    public float getX() {return x;}

    public float getY() {return y;}

    public void setX(float newX) {x = newX;}


    public void update(long fps) {
        if(y < frontHeight * 2) {
            y = y + speed / fps;
        }
    }

    public void gotHit(int strength){
        health = health - strength;

        if (health >= strength) {
            frontLength = backLength / initHealth * health;
        }
        bitmap_front = Bitmap.createScaledBitmap(bitmap_front,
                (int) (frontLength),
                (int) (frontHeight),
                false);
        bitmap_front = makeTransparent(bitmap_front);

    }

    public void setInactive(){isActive = false;}

    public boolean getStatus(){return isActive;}


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
