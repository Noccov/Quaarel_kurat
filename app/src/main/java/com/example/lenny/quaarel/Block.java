package com.example.lenny.quaarel;

import android.graphics.RectF;


public class Block {

    private float x;
    private float y;

    private RectF rect;

    float speed = 500;

    private int width;
    private int height;

    private boolean isActive;

    public Block(int screenX, int screenY){

        width = screenX / 7;
        height = width;
        isActive = false;

        rect = new RectF();
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

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}


    public float getImpactPointY(){
        return y + height;
    }
}
