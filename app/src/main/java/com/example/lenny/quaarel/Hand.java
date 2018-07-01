package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

public class Hand {
    private RectF rect;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;
    private Bitmap smallBitmap;

    private float length;
    private float height;

    private float x;
    private float y;


    //private float quaarelSpeed;
    private boolean handPos;

    public Hand(Context context, int screenX, int screenY) {

        rect = new RectF();

        length = screenX / 7;
        height = length/2;

        x = screenX / 2 + (length/2);
        y = screenY - height * 2;

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_1);

        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                (int) (length),
                (int) (height),
                false);

        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_2);

        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                (int) (length),
                (int) (height),
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);

        //quaarelSpeed = 350;
    }

    public RectF getRect() {return rect;}

     public void swichPos(){handPos = !handPos;}

    public Bitmap getBitmap() {
             if(handPos) {return bitmap_1;
              }else{return bitmap_2;}
    }


    public float getX() {return x;}

    public float getY() {return y;}

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
        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                (int) (length),
                (int) (height),
                false);

        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                (int) (length),
                (int) (height),
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
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
