package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

public class Hand {
    private RectF rect;

    private Bitmap bitmap_1_r;
    private Bitmap bitmap_2_r;
    private Bitmap bitmap_1_l;
    private Bitmap bitmap_2_l;
    private Bitmap smallBitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private boolean moveRight = true;
    private boolean handPos;

    public Hand(Context context, int screenX, int screenY) {

        rect = new RectF();

        length = screenX / 15;
        height = length * 2;

        x = screenX / 2 + (length / (float) 1.3);
        y = screenY - height * (float) 1.7;

        bitmap_1_r = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_1);
        bitmap_1_r = Bitmap.createScaledBitmap(bitmap_1_r,
                (int) (length),
                (int) (height),
                false);

        bitmap_2_r = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_2);
        bitmap_2_r = Bitmap.createScaledBitmap(bitmap_2_r,
                (int) (length),
                (int) (height),
                false);

        bitmap_1_l = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_1_l);
        bitmap_1_l = Bitmap.createScaledBitmap(bitmap_1_l,
                (int) (length),
                (int) (height),
                false);

        bitmap_2_l = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_2_l);
        bitmap_2_l = Bitmap.createScaledBitmap(bitmap_2_l,
                (int) (length),
                (int) (height),
                false);

        bitmap_1_r = makeTransparent(bitmap_1_r);
        bitmap_2_r = makeTransparent(bitmap_2_r);
        bitmap_1_l = makeTransparent(bitmap_1_l);
        bitmap_2_l = makeTransparent(bitmap_2_l);

        //quaarelSpeed = 350;
    }

    public RectF getRect() {return rect;}

     public void swichPos(){handPos = !handPos;}

    public Bitmap getBitmap() {
             if(handPos) {
                 if(moveRight){
                     return bitmap_1_r;
                 }else{
                     return bitmap_1_l;
                 }
              }else {
                 if (moveRight) {
                     return bitmap_2_r;

                 } else {
                     return bitmap_2_l;
                 }
             }
    }


    public float getX() {return x;}

    public float getY() {return y;}

    public boolean getRight() {return moveRight;}

    public void setRight(boolean setRight){
        if (setRight){
            moveRight = true;
        }else{
            moveRight = false;
        }
    }

    public void setX(float newX) {x = newX;}

    public float getLength() {return length;}

    public void setX(Integer QuaarelX){
        x = QuaarelX;
    }

    public void setSize(float newSize){
        height = height * newSize;
        length = length * newSize;
        updateBitmap();
        y = y + height - (height * newSize);
    }

    private void updateBitmap(){
        bitmap_1_r = Bitmap.createScaledBitmap(bitmap_1_r,
                (int) (length),
                (int) (height),
                false);

        bitmap_2_r = Bitmap.createScaledBitmap(bitmap_2_r,
                (int) (length),
                (int) (height),
                false);
        bitmap_1_l = Bitmap.createScaledBitmap(bitmap_1_l,
                (int) (length),
                (int) (height),
                false);

        bitmap_2_l = Bitmap.createScaledBitmap(bitmap_2_l,
                (int) (length),
                (int) (height),
                false);

        bitmap_1_r = makeTransparent(bitmap_1_r);
        bitmap_2_r = makeTransparent(bitmap_2_r);
        bitmap_1_l = makeTransparent(bitmap_1_l);
        bitmap_2_l = makeTransparent(bitmap_2_l);
    }

    public void update() {
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
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
