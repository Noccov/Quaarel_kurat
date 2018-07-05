package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;
import android.graphics.Color;

public class PowPow {

    private float x;
    private float y;


    private int width;
    private int height;

    private Bitmap bitmap;

    private boolean isActive;


    public PowPow(Context context, int screenX){

        width = screenX / 10;
        height = width ;
        isActive = false;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.powpow);
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

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}

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
