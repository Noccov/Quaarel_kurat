package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;
import android.graphics.Color;

public class Book {

    private float x;
    private float y;

    private RectF rect;

    float speed = 400;

    private int width;
    private int height;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;
    private Bitmap bitmap_3;
    private Bitmap bitmap_4;

    private boolean isActive;

    private int cnt = 0;

    public Book(Context context, int screenX){

        width = screenX / 10;
        height = width;
        isActive = false;
        rect = new RectF();

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_1);
        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                width,
                height,
                false);

        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_2);
        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                width,
                height,
                false);

        bitmap_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_3);
        bitmap_3 = Bitmap.createScaledBitmap(bitmap_3,
                width,
                height,
                false);

        bitmap_4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_4);
        bitmap_4 = Bitmap.createScaledBitmap(bitmap_4,
                width,
                height,
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
        bitmap_3 = makeTransparent(bitmap_3);
        bitmap_4 = makeTransparent(bitmap_4);
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

    public Bitmap getBitmap() {
        cnt++;
        if (cnt < 30) {
            return bitmap_1;
        }else if(cnt < 60){
            return bitmap_2;
        }else if(cnt < 90){
            return bitmap_3;
        }else if(cnt < 120){
            return bitmap_4;
        }else{cnt = 0; return bitmap_1;}
    }

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
