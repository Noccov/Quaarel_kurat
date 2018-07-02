package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import java.util.Random;

public class PowerupSizeBig {

    private RectF rect;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;
    private Bitmap bitmap_3;
    private Bitmap bitmap_4;
    private int cnt = 0;

    private float length;
    private float height;

    private float x;
    private float y;

    private Random generator = new Random();
    private float randomNumber;

    private int speed = 300 ;

    private boolean isActive;



    public PowerupSizeBig(Context context, int screenX) {

        rect = new RectF();

        length = screenX / 25;
        height = length;

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                (int) (length),
                (int) (height),
                false);
        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                (int) (length*2),
                (int) (height*2),
                false);
        bitmap_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        bitmap_3 = Bitmap.createScaledBitmap(bitmap_3,
                (int) (length*3),
                (int) (height*3),
                false);
        bitmap_4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        bitmap_4 = Bitmap.createScaledBitmap(bitmap_4,
                (int) (length*4),
                (int) (height*4),
                false);
        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
        bitmap_3 = makeTransparent(bitmap_3);
        bitmap_4 = makeTransparent(bitmap_4);

    }
    public boolean init(){
        if(!isActive){
            randomNumber = generator.nextInt((Math.round(length) * 5) - Math.round(length));
            x = randomNumber;
            y = 0;
            isActive = true;
            return true;
        }
        return false;
    }

    public RectF getRect() {return rect;}

    public Bitmap getBitmap() {
        if(cnt < 10) {
            return bitmap_1;
        }else if(cnt < 20)
        {
            return bitmap_2;
        }else if(cnt < 30){
            return bitmap_3;
        }else{
            return bitmap_4;
        }
    }

    public float getX() {return x;}

    public float getY() {return y;}

    public void setX(float newX) {x = newX;}

    public void update(long fps) {
        y = y + speed / fps;
        if(cnt < 40){
            cnt++;
        }else{
            cnt=0;
        }
        rect.top = y;
        rect.bottom = y + (height * 4);
        rect.left = x;
        rect.right = x + (length * 4);
    }

    public void setInactive(){isActive = false;}

    public boolean getStatus(){return isActive;}

    public float getImpactPointY(){return y + height;}

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
