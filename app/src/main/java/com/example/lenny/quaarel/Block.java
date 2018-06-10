package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;


public class Block {

    private float x;
    private float y;

    private RectF rect;

    private Bitmap bitmap;

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

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_1);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                width,
                height,
                false);
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

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }

    public void gotHit(Context context){
        if(health == 2){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_2);
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    width,
                    height,
                    false);
            health--;
        }else if(health == 1){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone_3);
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    width,
                    height,
                    false);
            health--;
        }else{
            health = 2;
            setInActive();
        }
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}
    public Bitmap getBitmap() {return bitmap;}

    public float getImpactPointY(){
        return y + height;
    }
}
