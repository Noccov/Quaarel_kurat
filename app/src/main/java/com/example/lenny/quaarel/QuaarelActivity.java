package com.example.lenny.quaarel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.graphics.Point;
import android.app.Activity;


public class QuaarelActivity extends Activity {

    QuaarelView quaarelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        quaarelView = new QuaarelView(this, size.x, size.y);
        setContentView(quaarelView);


    }

    @Override
    protected void onResume(){
        super.onResume();
        quaarelView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();

        quaarelView.pause();
    }
}
