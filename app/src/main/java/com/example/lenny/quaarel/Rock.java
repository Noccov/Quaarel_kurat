package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

public class Rock {

    private float x;
    private float y;

    private RectF rect;
    private Bitmap bitmap;


    float speed = 600;

    private float width;
    private float height;

    private boolean isActive;

    public Rock(Context context, int screenX){

        width = screenX / 20;
        height = width;
        isActive = false;

        rect = new RectF();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) width,
                (int) height,
                false);

        bitmap = makeTransparent(bitmap);
    }

    public boolean init(float startX, int startY){
        if(!isActive){
            x = startX;
            y = startY;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps){
        y = y - speed / fps;

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }

    public RectF getRect(){return rect;}

    public float getX(){return x;}
    public float getY(){return y;}

    public Bitmap getBitmap() {return bitmap;}
    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}

    public float getImpactPointY(){return y + height;}

    public void setSize(float newSize){
        height = height * newSize;
        width = width * newSize;
        updateBitmap();
    }

    public void updateBitmap(){
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) width,
                (int) height,
                false);

        bitmap = makeTransparent(bitmap);
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
