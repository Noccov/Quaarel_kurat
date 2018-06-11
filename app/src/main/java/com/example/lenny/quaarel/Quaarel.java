package com.example.lenny.quaarel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

public class Quaarel {

    RectF rect;

    private Bitmap bitmap;
    private Bitmap smallBitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private float quaarelSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int quaarelMoving = STOPPED;


    public Quaarel(Context context, int screenX, int screenY) {

        rect = new RectF();

        length = screenX / 10;
        height = screenY / 10;

        x = screenX / 2;
        y = screenY - height * 2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.quaarel);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
        bitmap = makeTransparent(bitmap);
        smallBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length / 2),
                (int) (height / 2),
                false);

        quaarelSpeed = 350;
    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getSmallBitmap() {
        return smallBitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float newX) {
        x = newX;
    }

    public float getLength() {
        return length;
    }

    public void setMovementState(int state) {
        quaarelMoving = state;
    }

    public void update(long fps) {
        if (quaarelMoving == LEFT) {
            x = x - quaarelSpeed / fps;
        }

        if (quaarelMoving == RIGHT) {
            x = x + quaarelSpeed / fps;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
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
            if (allpixels[i] == android.graphics.Color.GREEN)

                allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }
}
