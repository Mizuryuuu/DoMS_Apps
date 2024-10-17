package com.bijana.doms.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_euy);

        // code Thread
        Thread splashTime = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // intent code
                    Intent screenTime = new Intent(SplashScreen.this, LoginDoMS.class);
                    startActivity(screenTime);
                    finish();
                }
            }
        };
        splashTime.start();
    }
}