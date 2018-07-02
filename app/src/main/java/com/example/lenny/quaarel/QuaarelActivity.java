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

        //Button button = (Button)findViewById(R.id.PauseButton);
        //button.setOnClickListener(this);

        setContentView(R.layout.activity_quaarel);
        quaarelView = new QuaarelView(this, size.x, size.y);
        //setContentView(quaarelView);
    }

    public void startGame(View view)
    {
        setContentView(quaarelView);
        //quaarelView.setPlaying();
        quaarelView.prepareLevel();
        quaarelView.resume();
    }
    public void resumeGame (View view){
        setContentView(quaarelView);
        quaarelView.resume();
        //onResume();

    }

    @Override
    protected void onResume(){
        super.onResume();

        //QuaarelView.soundManager.playMusic();
        //setContentView(quaarelView);
        //quaarelView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        quaarelView.pause();
        setContentView(R.layout.activity_quaarel);
    }

    /*@Override
    public void onClick(View v) {

    }

        protected void onCreate(Bundle savedValues) {
        ...
            Button button = (Button)findViewById(R.id.corky);
            button.setOnClickListener(this);
        }

        // Implement the OnClickListener callback
        ///public void onClick(View v) {
            // do something when the button is clicked
        }
    ...
    }
*/
}
