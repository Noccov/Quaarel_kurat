package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.content.Context;


public class Block {

    private float x;
    private float y;

    private RectF rect;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;
    private Bitmap bitmap_3;

    float speed = 300;

    private int width;
    private int height;

    private int health = 2;

    private boolean isActive;

    public Block(Context context, int screenX){

        width = screenX / 7;
        height = width;
        isActive = false;

        rect = new RectF();

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_1);
        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                width,
                height,
                false);
        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_2);
        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                width,
                height,
                false);
        bitmap_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_3);
        bitmap_3 = Bitmap.createScaledBitmap(bitmap_3,
                width,
                height,
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
        bitmap_3 = makeTransparent(bitmap_3);

    }

    public boolean init(float startX, float startY){
        if(!isActive){
            x = startX;
            y = startY;
            health = 2;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps){
        y = y + speed / fps;

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }

    public void gotHit(){
        if(health > 0){
            health--;
        }else{
            setInActive();
        }
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}
    public Bitmap getBitmap() {
        if(health == 2){
            return bitmap_1;
        }else if(health == 1){
            return bitmap_2;
        }else{
            return bitmap_3;
        }
    }

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
