package edu.utep.cs.cs4381.tappydefender;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity{
    GameView gameView;
    SoundEffect soundEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Display for screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        soundEffect = new SoundEffect(this);

        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);

    }

    ///////LIFE-CYCLE
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //hits first to create before gameview
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
