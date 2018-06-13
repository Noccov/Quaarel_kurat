package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.content.Context;


public class Cloud {

    private float x = 0;
    private float y;

    private RectF rect;

    private Bitmap bitmap;

    float speed = 200;

    private int width;
    private int height;

    private boolean isActive;

    public Cloud(Context context, int screenX){

        width = screenX;
        height = width;
        isActive = false;

        rect = new RectF();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                width,
                height,
                false);

        bitmap = makeTransparent(bitmap);

    }

    public boolean init(int screenY){
        if(!isActive){
            y = screenY;
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


    public float getX(){return x;}
    public float getY(){return y;}

    public boolean getStatus(){return isActive;}

    public void setInactive(){isActive = false;}
    public Bitmap getBitmap() {return bitmap;}

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
