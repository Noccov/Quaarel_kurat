package com.example.lenny.quaarel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.content.Context;


public class Boss {

    private float x;
    private float y;

    private RectF rect;

    private Bitmap bitmap_1;
    private Bitmap bitmap_2;

    float speed = 200;

    private int width;
    private int height;

    private int health = 20;

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

        bitmap_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_1);

        bitmap_1 = Bitmap.createScaledBitmap(bitmap_1,
                width,
                height,
                false);

        bitmap_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.liina_2);
        bitmap_2 = Bitmap.createScaledBitmap(bitmap_2,
                width,
                height,
                false);

        bitmap_1 = makeTransparent(bitmap_1);
        bitmap_2 = makeTransparent(bitmap_2);
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

    public void gotHit(){
            health--;
            isHit = true;
            cnt = 0;
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public RectF getRect(){return rect;}

    public boolean getStatus(){return isActive;}

    public void setInActive(){isActive = false;}
    public void setActive(){isActive = true;}

    public Bitmap getBitmap() {
        if(isHit){
            return bitmap_2;
        }else {
            return bitmap_1;
        }
    }

    public void setMovementState(int state){bossMoving = state;}

    public int getHealth(){return health;}

    public float getImpactPointY(){
        return y + height;
    }

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
