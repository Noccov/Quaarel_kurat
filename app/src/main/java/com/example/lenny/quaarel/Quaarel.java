package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Quaarel {

    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private float quaarelSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int quaarelMoving = STOPPED;


    public Quaarel(Context context, int screenX, int screenY){

        rect = new RectF();

        length = screenX/10;
        height = screenY/10;

        x = screenX / 2;
        y = screenY - height*2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.quaarel);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        quaarelSpeed = 350;
    }

    public RectF getRect() {return rect;}

    public Bitmap getBitmap() {return bitmap;}

    public float getX(){return x;}
    public float getY(){return y;}

    public float getLength(){ return length;}

    public void setMovementState(int state){quaarelMoving = state;}

    public void update(long fps){
        if(quaarelMoving == LEFT){
            x = x - quaarelSpeed / fps;
        }

        if(quaarelMoving == RIGHT){
            x = x + quaarelSpeed / fps;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }
}
