package edu.utep.cs.cs4381.tappydefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ButtonListener{
    HomeView hv;
    SoundEffect soundEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);  //used to display main

        hv = new HomeView(this);
        setContentView(hv);

        soundEffect = new SoundEffect(this);
        //SET_CLICK_LISTENER HERE
        hv.setDiscClickListener(this::squButtonClicked); //could also be just "this"
    }


    //INVOKE WHEN DETECT BUTTON SQU. CLICK
    @Override
    public void squButtonClicked() {
        startActivity(new Intent(this, GameActivity.class)); //TRANSFER TO NEXT PAGE
        //soundEffect.play(SoundEffect.Sound.START);
    }
}