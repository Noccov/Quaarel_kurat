package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public Block(Context context, int screenX, int screenY){

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

    public float getImpactPointY(){
        return y + height;
    }
}
