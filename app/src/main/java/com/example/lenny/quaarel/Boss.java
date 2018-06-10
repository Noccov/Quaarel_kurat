package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;


public class Boss {

    private float x;
    private float y;

    private RectF rect;

    private Bitmap bitmap;

    float speed = 200;

    private int width;
    private int height;

    private int health = 50;

    private boolean isActive;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int bossMoving = 0;
    private boolean isHit;
    private int cnt = 0;


    public Boss(Context context, int screenX, int screenY){

        width = screenX / 3;
        height = width;
        x = screenX / 2 - (width/2);
        y = -height;
        isActive = false;

        rect = new RectF();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_1);

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

    public void update(Context context, long fps, int screenX){
        if(y < height / 2) {
            y = y + speed / fps;
        }else if(bossMoving == RIGHT){
            x = x + speed / fps;
        }else{
            x = x - speed / fps;
        }
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

        if(isHit) {
            cnt++;
            if (cnt > fps){
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_1);
                bitmap = Bitmap.createScaledBitmap(bitmap,
                        width,
                        height,
                        false);
                health--;
                isHit = false;
                cnt = 0;
            }
        }

        if(x < 0){
            bossMoving = RIGHT;
        }else if(x > screenX - width){
            bossMoving = LEFT;
        }
    }

    public void gotHit(Context context){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_2);
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    width,
                    height,
                    false);
            health--;
            isHit = true;
            cnt = 0;
            if(health < 1){
                setInActive();
            }
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}
    public Bitmap getBitmap() {return bitmap;}

    public void setMovementState(int state){bossMoving = state;}

    public float getImpactPointY(){
        return y + height;
    }
}
