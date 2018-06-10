package com.example.lenny.quaarel;

import android.graphics.RectF;

public class Rock {

    private float x;
    private float y;

    private RectF rect;

    float speed = 600;

    private int width;
    private int height;

    private boolean isActive;

    public Rock(int screenX){

        width = screenX / 10;
        height = width;
        isActive = false;

        rect = new RectF();
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

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}

    public float getImpactPointY(){return y + height;}
}
