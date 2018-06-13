package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import java.util.Random;

public class PowerupHealth {

    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private Random generator = new Random();
    private float randomNumber;

    private int speed = 300 ;

    private boolean isActive;



    public PowerupHealth(Context context, int screenX) {

        rect = new RectF();

        length = screenX / 15;
        height = length;


        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.quaarel);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
        bitmap = makeTransparent(bitmap);

    }
    public boolean init(){
        if(!isActive){
            randomNumber = generator.nextInt((Math.round(length) * 15) - Math.round(length));
            x = randomNumber;
            y = 0;
            isActive = true;
            return true;
        }
        return false;
    }

    public RectF getRect() {return rect;}

    public Bitmap getBitmap() {return bitmap;}


    public float getX() {return x;}
    public float getY() {return y;}

    public void setX(float newX) {x = newX;}


    public void update(long fps) {
        y = y + speed / fps;

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }

    public void setInactive(){isActive = false;}

    public boolean getStatus(){return isActive;}

    public float getImpactPointY(){
        return y + height;
    }

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
