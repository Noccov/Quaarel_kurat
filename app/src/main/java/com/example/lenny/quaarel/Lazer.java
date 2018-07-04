package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;
import android.graphics.Color;

public class Lazer {

    private float x;
    private float y;

    private RectF rect;

    float speed = 1000;

    private int width;
    private int height;

    private Bitmap bitmap;

    private boolean isActive;

    private int cnt = 0;

    public Lazer(Context context, int screenX){

        width = screenX / 6;
        height = width / 2;
        isActive = false;
        rect = new RectF();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lazer);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                width,
                height,
                false);

        bitmap = makeTransparent(bitmap);
    }


    public boolean init(float startX, float startY){
        if(!isActive){
            x = startX;
            y = startY;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps){
        y = y + speed / fps;

        cnt++;
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}

    public float getImpactPointY(){return y + height;}

    public Bitmap getBitmap() {return bitmap;}

    public float getX(){return x;}
    public float getY(){return y;}


    // Convert transparentColor to be transparent in a Bitmap.
    public static Bitmap makeTransparent(Bitmap bit) {
        int width =  bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int [] allpixels = new int [ myBitmap.getHeight()*myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(),myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        for(int i =0; i<myBitmap.getHeight()*myBitmap.getWidth();i++){
            if( allpixels[i] == android.graphics.Color.BLACK)

                allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }
}
