package com.example.lenny.quaarel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.graphics.Point;
import android.app.Activity;
import android.view.View;
import android.widget.Button;


public class QuaarelActivity extends Activity {

    QuaarelView quaarelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        setContentView(R.layout.activity_quaarel);
        quaarelView = new QuaarelView(this, size.x, size.y);
    }

    public void startGame(View view)
    {
        setContentView(quaarelView);
        quaarelView.prepareLevel();
        quaarelView.resume();
    }
    public void resumeGame (View view){
        setContentView(quaarelView);
        quaarelView.resume();

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        quaarelView.pause();
        setContentView(R.layout.activity_quaarel);
    }
}
